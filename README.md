# Transferz development challenge
###### v1.2

# How to run this application

### Setup Docker compose. Make sure that Docker daemon is on.
```shell
cd dev-containers
docker-compose up -d  
```

### Run application from project root (or run it from IDE)
```shell
mvn clean package
java -jar target/dev-assignment-be.0.1.jar
```






## Introduction
This is development challenge for Java developer candidates at transferz. The goal of this task is to understand your way of thinking and problem-solving skills around composing new services, database design, SQL composing, understandign of multi-threading and unit-testing.

We are expecting that this task should take no more than 2 hours (including reading this documentation). Any result is important for us, if you feel that something is not solved it is not a problem. TODOs and comments in the source code are welcome.

Please fork this repository into your own GitHub account and send us the link to the repository when you have finished.

## The challenge
During high tide the dikes in The Netherlands have given way and we need to evacuate residents as soon as possible. One way to do this is by airlifting people out from Schiphol Airport. As this is an emergency, we're not concerned with where passengers will be taken, as long as we get them out as soon as possible. Each airplane used in the evacuation routine can have a maximum capacity of 150 passengers.

Please create a REST API service which handles data about airports, flights and passengers. At this time there is no need to think about scaling and running multiple instances of this service - assume that it will be running as a single instance. 

## You should:
* use Java 17, Spring Boot 2.6+, Maven 3.5+ (if you prefer Gradle that's also fine)
* select a relational database like H2, MariaDB, PostgreSQL, etc.
* create relevant tables for airports, flights and passengers (and another DB entities if needed) 
* create relevant DAOs, services and controllers
* add business logic to accomplish the tasks below
* ensure the application has at least 80% test coverage
* change the initial design to get better performance if needed
* ensure the Maven build is successful
* ensure the final JAR file is runnable
                       
## Database description

* Airport table should have at least:
  * `name` column as varchar(255)
  * `code` column as varchar(20)
  * `country` column as varchar(60)
* Flight table should have at least:
  * `code` column as varchar(20)
  * `originAirportCode` column as varchar(20)
  * `destinationAirportCode` column as varchar(20)
  * `departureDateTime` column as <your choice type>
  * `arrivalDateTime` column as <your choice type>
* Passenger table should have at least:
  * `name` column as varchar(1024)
  * `flightCode` column as varchar(20)

During the database design, please think about additional constraints and type of `id` field and way of its generation for better performance and security.

If you feel the database structure above is insufficient to cover the requirements you are free to change the structure as you see fit.

## REST API description 

We would like to see these endpoints:
* Retrieve all airports with pagination and optional filtering on name and code
* Add a new airport with validation for fields and unique code and name
* Add new passenger on a flight with validation for fields and unique name per flight
      
## Business logic

Additional functionality needed: 
* On each "Add new passenger" REST request, the service should calculate the amount of passengers per flight:
  * If the amount of passengers is 150 (amount externally configurable) or higher, the flight should depart and new passengers should be booked on the next flight
  * The destination of the flight should be auto-selected based on the available airports

## Notes
* Please implement all part of the service with performance in mind
* Keep in mind that the service can receive parallel requests
* Remember that good tests cover negative scenarios as well
* You have any questions feel free to ask them, we are here to help you
* Do not forget to re-read the task to be sure every topic is covered
* Good luck!
