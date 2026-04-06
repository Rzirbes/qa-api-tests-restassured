# qa-api-tests-restassured

API test automation project using Rest Assured, JUnit 5, Allure Reports and Postman collections.

## Overview

This project demonstrates automated API testing using Java and Rest Assured against the public Restful Booker API.

It includes validation of core CRUD operations, authentication, and response assertions, along with detailed reporting using Allure.

## Technologies

- Java
- Rest Assured
- JUnit 5
- Allure Report
- Postman

## Test Coverage

The following scenarios are covered:

- Create booking
- Get all booking IDs
- Get booking IDs by filters (firstname and lastname)
- Get booking by ID
- Authentication

## Project Structure

```
src/
  test/
    java/
      BookingTests.java

postman/
  restful-booker_collection.json

pom.xml
```

# Postman
The Postman collection is available in the postman folder and can be imported to execute the same requests manually.

## Notes
- The tests use a public API: https://restful-booker.herokuapp.com
- Logging is enabled for requests and responses
- Allure integration is configured for detailed reporting


## Author
Rômulo Zirbes
