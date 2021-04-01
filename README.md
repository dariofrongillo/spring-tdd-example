# Spring TDD example for Italiancoders


# GOAL
The goal of this project is to show to italiancoders community  how to test correctly a Spring Boot API application reporting some typical  Test scenarios

## API Features
Our HTTP application expose the following endpoint
 - GET /products/{id} to retrieve a certain Product
 - POST /products to insert a new Product
 -  GET /stock/{id} to get the stock amount related an existing product. In order to get Amount Data our Application use a 3rd party API 

## Test Isolation
The concept behind this test implementation is to test independently and isolated each layer of our application:
 - Controller Layer using mockmvc and mocking service layer
 - Service layer mocking repository layer. In order to test stock service i have choosen to mock the GET /stock API using  WireMock a simulator for HTTP-based APIs. Some might consider it a **service virtualization** tool or a mock server.  It enables you to stay productive when an API you depend on doesn't exist or isn't complete. It supports testing of edge cases and failure modes that the real API won't reliably produce. And because it's fast it can reduce your build time from hours down to minutes.
 -  Repository Layer using H2 as in memory database and DB UNIT a unit testing tool used to test relational database interactions  in Java.  DBUnit lets us define and load our test dataset in a simple declarative way in order to prepare the dataset for test scenarios.

