# Example Environment Configuration Files

## Local Development (.env.local)

Create this file in the project root for local development:

```
# IBM MQ Configuration
MQ_CONNECTION_NAME=localhost(1414)
MQ_USER=app
MQ_PASSWORD=passw0rd
MQ_QUEUE_MANAGER=QM1
MQ_CHANNEL=DEV.APP.SVRCONN

# Spring Boot Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=default

# Application Configuration
APP_MQ_REQUEST_QUEUE=DEV.QUEUE.1
APP_MQ_RESPONSE_QUEUE=DEV.QUEUE.2
APP_MQ_ERROR_QUEUE=DEV.QUEUE.ERROR
APP_MQ_DEAD_LETTER_QUEUE=DEV.DLQ

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_IBMMQ=DEBUG

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m
```

## Production (.env.prod)

For production deployment:

```
# IBM MQ Configuration - Production
MQ_CONNECTION_NAME=mq-prod.example.com(1414)
MQ_USER=prod-app-user
MQ_PASSWORD=CHANGE_THIS_SECURE_PASSWORD
MQ_QUEUE_MANAGER=PROD.QM1
MQ_CHANNEL=PROD.APP.SVRCONN

# Spring Boot Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# Application Configuration
APP_MQ_REQUEST_QUEUE=PROD.QUEUE.1
APP_MQ_RESPONSE_QUEUE=PROD.QUEUE.2
APP_MQ_ERROR_QUEUE=PROD.QUEUE.ERROR
APP_MQ_DEAD_LETTER_QUEUE=PROD.DLQ

# Logging
LOGGING_LEVEL_ROOT=WARN
LOGGING_LEVEL_IBMMQ=INFO

# Java Options for Production
JAVA_OPTS=-Xmx1024m -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200

# SSL/TLS Configuration (optional)
SSL_ENABLED=false
SSL_KEYSTORE_PATH=/path/to/keystore.p12
SSL_KEYSTORE_PASSWORD=keystore_password

# Monitoring
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics,prometheus
```

## Testing (.env.test)

For testing environment:

```
# IBM MQ Configuration - Test
MQ_CONNECTION_NAME=localhost(1414)
MQ_USER=test-user
MQ_PASSWORD=test-password
MQ_QUEUE_MANAGER=TEST.QM1
MQ_CHANNEL=TEST.APP.SVRCONN

# Spring Boot Configuration
SERVER_PORT=8081
SPRING_PROFILES_ACTIVE=test

# Application Configuration
APP_MQ_REQUEST_QUEUE=TEST.QUEUE.1
APP_MQ_RESPONSE_QUEUE=TEST.QUEUE.2
APP_MQ_ERROR_QUEUE=TEST.QUEUE.ERROR
APP_MQ_DEAD_LETTER_QUEUE=TEST.DLQ

# Logging
LOGGING_LEVEL_ROOT=DEBUG
LOGGING_LEVEL_IBMMQ=DEBUG

# Java Options
JAVA_OPTS=-Xmx256m -Xms128m
```

## Staging (.env.staging)

For staging environment:

```
# IBM MQ Configuration - Staging
MQ_CONNECTION_NAME=mq-staging.example.com(1414)
MQ_USER=staging-app-user
MQ_PASSWORD=STAGING_PASSWORD
MQ_QUEUE_MANAGER=STAGING.QM1
MQ_CHANNEL=STAGING.APP.SVRCONN

# Spring Boot Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=staging

# Application Configuration
APP_MQ_REQUEST_QUEUE=STAGING.QUEUE.1
APP_MQ_RESPONSE_QUEUE=STAGING.QUEUE.2
APP_MQ_ERROR_QUEUE=STAGING.QUEUE.ERROR
APP_MQ_DEAD_LETTER_QUEUE=STAGING.DLQ

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_IBMMQ=INFO

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m -XX:+UseG1GC
```

## AWS Deployment (.env.aws)

For AWS deployment:

```
# AWS Environment
AWS_REGION=us-east-1
AWS_PROFILE=production

# IBM MQ Configuration on AWS MQ
MQ_CONNECTION_NAME=b-1a2b3c4d-5e6f7g8h-1.mq.us-east-1.amazonaws.com(1414)
MQ_USER=admin
MQ_PASSWORD=AWS_GENERATED_PASSWORD
MQ_QUEUE_MANAGER=QM1
MQ_CHANNEL=DEV.APP.SVRCONN

# Spring Boot Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# Application Configuration
APP_MQ_REQUEST_QUEUE=AWS.QUEUE.1
APP_MQ_RESPONSE_QUEUE=AWS.QUEUE.2
APP_MQ_ERROR_QUEUE=AWS.QUEUE.ERROR
APP_MQ_DEAD_LETTER_QUEUE=AWS.DLQ

# Logging to CloudWatch
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_IBMMQ=INFO

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m -XX:+UseG1GC
```

## Azure Deployment (.env.azure)

For Azure deployment:

```
# Azure Environment
AZURE_SUBSCRIPTION_ID=your-subscription-id
AZURE_RESOURCE_GROUP=spring-mq-group

# IBM MQ Configuration
MQ_CONNECTION_NAME=your-mq-service.servicebus.windows.net(1414)
MQ_USER=admin
MQ_PASSWORD=AZURE_PASSWORD
MQ_QUEUE_MANAGER=QM1
MQ_CHANNEL=DEV.APP.SVRCONN

# Spring Boot Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# Application Configuration
APP_MQ_REQUEST_QUEUE=AZURE.QUEUE.1
APP_MQ_RESPONSE_QUEUE=AZURE.QUEUE.2
APP_MQ_ERROR_QUEUE=AZURE.QUEUE.ERROR
APP_MQ_DEAD_LETTER_QUEUE=AZURE.DLQ

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_IBMMQ=INFO

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m
```

## Google Cloud Deployment (.env.gcp)

For Google Cloud deployment:

```
# GCP Environment
GCP_PROJECT_ID=your-project-id
GCP_REGION=us-central1

# IBM MQ Configuration
MQ_CONNECTION_NAME=your-mq-instance:1414
MQ_USER=admin
MQ_PASSWORD=GCP_PASSWORD
MQ_QUEUE_MANAGER=QM1
MQ_CHANNEL=DEV.APP.SVRCONN

# Spring Boot Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# Application Configuration
APP_MQ_REQUEST_QUEUE=GCP.QUEUE.1
APP_MQ_RESPONSE_QUEUE=GCP.QUEUE.2
APP_MQ_ERROR_QUEUE=GCP.QUEUE.ERROR
APP_MQ_DEAD_LETTER_QUEUE=GCP.DLQ

# Logging to Cloud Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_IBMMQ=INFO

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m
```

## Usage Instructions

### For Local Development

1. Copy `.env.local` to your project root
2. Update with your local IBM MQ details
3. Source the file:
   ```bash
   source .env.local
   # or on Windows
   export $(cat .env.local | xargs)
   ```
4. Run application:
   ```bash
   mvn spring-boot:run
   ```

### For Docker Compose

Add to `docker-compose.yml`:
```yaml
env_file:
  - .env.local
```

### For Cloud Deployment

Set environment variables in your cloud platform:
- **AWS**: Elastic Beanstalk Configuration
- **Azure**: App Service Configuration
- **GCP**: Cloud Run Environment Variables
- **Kubernetes**: ConfigMap/Secrets

### Security Notes

⚠️ **IMPORTANT**: 
- Never commit `.env.*` files with actual passwords to version control
- Use `.gitignore` to exclude these files
- For production, use secrets management services:
  - AWS Secrets Manager
  - Azure Key Vault
  - Google Secret Manager
  - Kubernetes Secrets

Template `.env.example` (safe to commit):
```
MQ_CONNECTION_NAME=localhost(1414)
MQ_USER=app
MQ_PASSWORD=CHANGE_ME
MQ_QUEUE_MANAGER=QM1
MQ_CHANNEL=DEV.APP.SVRCONN
```

