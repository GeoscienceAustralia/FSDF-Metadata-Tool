package org.fao.geonet.services.zip;

import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.UserSession;
import jeeves.server.context.ServiceContext;
import org.fao.geonet.Constants;
import org.fao.geonet.Util;
import org.fao.geonet.ZipUtil;
import org.fao.geonet.domain.AbstractMetadata;
import org.fao.geonet.kernel.SelectionManager;
import org.fao.geonet.kernel.datamanager.IMetadataUtils;
import org.fao.geonet.utils.BinaryFile;
import org.jdom.Element;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Set;

import static org.fao.geonet.kernel.SelectionManager.*;

public class Export implements Service {
    private ServiceConfig _config;

    public void init(Path appPath, ServiceConfig params) throws Exception {
        this._config = params;
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        Path file = Files.createTempFile("zip-", ".zip");

        String bucket = Util.getParam(params, SELECTION_BUCKET, SELECTION_METADATA);
        UserSession session = context.getUserSession();
        SelectionManager selectionManger = SelectionManager.getManager(session);
        Set<String> uuids = selectionManger.getSelection(bucket);
        selectionManger.close(SelectionManager.SELECTION_METADATA);
        selectionManger.addAllSelection(SelectionManager.SELECTION_METADATA, uuids);

        FileSystem zipFs = ZipUtil.createZipFs(file);

        for (String uuid : uuids) {
            System.out.println(uuid);
            AbstractMetadata md = context.getBean(IMetadataUtils.class).findOneByUuid(uuid);
            System.out.println(md.getData().length());
            Files.write(zipFs.getPath("/" + uuid + ".xml"), md.getData().getBytes(Constants.CHARSET));
        }

        zipFs.close();
        String fname = String.valueOf(Calendar.getInstance().getTimeInMillis());
        return BinaryFile.encode(200, file, "export-" + fname + ".zip", true).getElement();
    }
}
