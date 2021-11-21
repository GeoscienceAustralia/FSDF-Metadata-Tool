## Configuring OpenID Connect in Amazon Web Services

OpenID Connect (OIDC) allows your GitHub Actions workflows to access resources in Amazon Web Services (AWS), without needing to store the AWS credentials as long-lived GitHub secrets.

## Steps to Configure

1. Create Policy in AWS IAM as given below; IAM > Policies > Create Policy

`{ "Version": "2012-10-17", "Statement": [ { "Effect": "Allow", "Action": [ "s3:ListBucket" ], "Resource": [ "arn:aws:s3:::<bucket-name>" ] }, { "Effect": "Allow", "Action": [ "s3:PutObject", "s3:GetObject", "s3:DeleteObject" ], "Resource": [ "arn:aws:s3:::<bucket-name>/*" ] } ] }`

Ex: given bucket-name as atlas-war-files

2. Create Identity provider; IAM > Identity providers > Add Provider > Choose 'OpenID Connect'

   - Provider URL: https://token.actions.githubusercontent.com
   - Audience: sts.amazonaws.com
   - Click 'Get thumbprint'
   - Click 'Add provider'

3. Again go to IAM > Identity providers > Choose the created one

4. Click Assign role

5. Choose 'Create a new role'

6. A new window opens and make sure 'Web Identity' option is selected by default and the Identity provider must be the one created in previous step

   - Choose the default Audience
   - Click Next: Permissions
   - Attach the Policy created in step 1
   - Provide the Role name as 'github-s3-copy-role' and description (optional)
   - Click 'Create role'

7. Go to the Roles and select the role created

   - Copy the Role ARN

8. Open Github repository
   - Create a new Repository Secret with name 'ASSUME_ROLE_ARN' and paste the Role ARN

## Reference

https://docs.github.com/en/actions/deployment/security-hardening-your-deployments/configuring-openid-connect-in-amazon-web-services
