## Configuring OpenID Connect in Amazon Web Services

OpenID Connect (OIDC) allows your GitHub Actions workflows to access resources in Amazon Web Services (AWS), without needing to store the AWS credentials as long-lived GitHub secrets.

## Steps to Configure

1. Create Policy in AWS IAM as given below; IAM > Policies > Create Policy

`{ "Version": "2012-10-17", "Statement": [ { "Effect": "Allow", "Action": [ "s3:ListBucket" ], "Resource": [ "arn:aws:s3:::<bucket-name>" ] }, { "Effect": "Allow", "Action": [ "s3:PutObject", "s3:GetObject", "s3:DeleteObject" ], "Resource": [ "arn:aws:s3:::<bucket-name>/*" ] } ] }`

Ex: given bucket-name as atlas-war-files

2. Create Identity provider; IAM > Identity providers > Add Provider > Choose OpenID Connect

   - Provider URL: https://token.actions.githubusercontent.com
   - Audience: https://github.com/GeoscienceAustralia

3. Create Role in AWS IAM and attach above created Policy; IAM > Roles > Create Role

   - Select 'Web identity'
   - Select token.actions.githubusercontent.com from Identity Provider
   - Select thre github Audience

4. Attach the Policy created in step 1

5. Add a subject (sub) condition
   - Edit the trust relationship to add the sub field; Roles > Select Previously create Role > Select 'Trust relationships' and Click 'Edit trust relationships'
   -

## Reference

https://docs.github.com/en/actions/deployment/security-hardening-your-deployments/configuring-openid-connect-in-amazon-web-services
