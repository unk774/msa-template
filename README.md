# msa-application

Template project with microservices

Build:
gradle clean build  
gradle jibDockerBuild  

Run:
./docker-compose.yml  
docker-compose up -d  

Swagger:
http://localhost:8080/api-docs/store/swagger-ui/index.html - Storage service
http://localhost:8081/api-docs/auth/swagger-ui/index.html - Authentication service
http://localhost:8082/api-docs/gateway/swagger-ui/index.html - API Gateway
http://localhost:8083/api-docs/notification/swagger-ui/index.html - User notification service