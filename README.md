# Online Grocery Store Application

## Description

Online Grocery Store for Order, discount, breakout and price overview

## Tools

- Java 17
- Spring boot 3.4.2
- Openapi - Swagger
- Maven
- H2
- PostgreSQL
- Docker

## How to run Application locally using H2 Database

1. Clone or download zip project - https://github.com/NeeleshJaiswal/online-grocery-store/tree/main
2. Ensure Annotation Processors is enabled for @lombok. If using Intellij Idea - Goto
   `Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Annotation profile for account-opening-service` ->
   Select `online-grocery-store-app` Check `Obtain processors from project classpath`
3. `mvn spring-boot:run` OR click Run on the `OnlineGroceryStoreApplication` if using Intellij

- Swagger UI - http://localhost:8080/swagger-ui/index.html. API doc - `http://localhost:8080/v3/api-docs`
- H2 database will be available at `http://localhost:8080/h2-console`. JDBC URL `jdbc:h2:mem:onlinegrocery`, Username =
  `sa`, Password = `password`.

## Run with Docker

1. Clone or download zip project - https://github.com/NeeleshJaiswal/online-grocery-store/tree/main
2. Download docker engine or docker desktop https://www.docker.com/products/docker-desktop/
3. Build Docker Image `docker build -t online-grocery-store-app .`
4. Start with Docker Compose `docker-compose up --build`
5. This will start the application at http://localhost:8080 and a PostgreSQL database on localhost:5432.

- For Swagger UI - `http://localhost:8080/swagger-ui/index.html`.
- Api docs - `http://localhost:8080/v3/api-docs`

- To drop containers and volumes - `docker-compose down -v`

## Testing

To launch your application's tests, run:

- ` .mvn test `
- Postman collection
  https://grey-meadow-550876.postman.co/workspace/Public-Demo~c3eaf9df-aefd-4cfa-90cc-6380f687b248/collection/2857489-46ca3c03-c861-4fad-add7-b66c3bcbf00c?action=share&creator=2857489

## Notes

Here are some considerations and assumptions made during the development process:

- The development process strictly followed the documentation and the detailed API specifications provided.
- The discount rules are currently hardcoded, but they could be improved by creating APIs that allow you to add or modify discounts easily.
- During development and testing, I used [``DataLoader``](https://github.com/NeeleshJaiswal/online-grocery-store/blob/main/src/main/java/com/payaut/onlinegrocerystore/config/DataLoader.java#L16) to set up product prices and quantities. This could be made better by creating APIs that let you dynamically add new products, update prices, or adjust quantities.

````
itemRepository.save(Item.builder()
                    .withItemType(ItemType.VEGETABLE)
                    .withName("Vegetables")
                    .withUnitPrice(0.01) // new unit price
                    .withQuantity(1) // new quantity for new price
                    .withDetails("Price per gram")
                    .build());
````
