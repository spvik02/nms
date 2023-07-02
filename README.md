# nms 
create jar
```
./gradlew clean build -x test
```
up docker
```
docker-compose up
```
exported postman collection is [here](https://github.com/spvik02/nms/blob/3ab0a292990e18aa17a235eb0f140a1fe364e881/src/main/resources/static/clt%20nms.postman_collection.json)

swagger openapi 3.0 will be available on this path http://localhost:8080/swagger-ui/index.html 

### about application.yaml

The initial profiles are setted to dev, basic, cache. 

basic profile is mostly for hibernate search and pageable. 

dev for work with db installed on machine. In this profile liquibase is disabled. You can change dev to prod profile to use docker instead. In prod liquibase is enabled and reindex for hibernate search set to true. Please, note that the port in prod profile has been changed to 8181

redis profile is for redis cache. cache profile is for LFU/LRU
