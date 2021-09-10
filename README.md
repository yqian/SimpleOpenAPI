This project is demonstration of Spring Boot with OpenAPI



## Config

1. Add openapi-generator-maven-plugin in pom.xml, will generate OpenAPI code during Maven build.

2. Use Liquibase to manage h2 in memory database

3. Define URL path "simple-openapi" in application yaml file

4. Spring Boot Actuator, end point is http://localhost:8080/simple-openapi/actuator/health


## Test

1. Start the project `mvn spirng-boot:run`

2. Use curl command

* Test "GET"

`curl http://localhost:8080/simple-openapi/orders/1`
` will display the order pre-populated during build

* Test "POST"

`curl -H "Content-Type: application/json" -X "POST" -d '{"sale":false,"orderId":2,"products":[{"productId":"2","name":"orange","price":0.25,"quantity":3}]}";' http://localhost:8080/simple-openapi/orders` will failed because H2 database does not generate the primary key.

3. Verify result

To verify it, access h2 database `http://localhost:8080/simple-openapi/h2_console`
