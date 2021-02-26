# README
This application implements a simple project that handles South African phone numbers.

The application implements the following use cases:

1. Import csv with id and phone numbers via UI form
2. View imported numbers distinguishing between valids, invalids but fixed, invalids and not recoverable
3. Import csv with id and phone numbers via REST API (for example there is the folder /curl that has a simple sh script to send the csv to this app)
4. UI form to test single phone number with same validation.

# Chosen technologies

Backend: **Java + Spring Boot**

DB:  **H2** (whose schema is created automatically, the data is erased at every file import)

Frontend: **Thymeleaf** and **Material UI** library

# Requirements
* Java 15
* Maven 3.6.3
* Unix os

# Importing and running the project
Import the project as a Maven project in your preferred IDE.

To run the application simply run in the IDE the class **SouthAfricanPhoneNumbersValidatorApplication**.
The application listens on http://localhost:8080.

To run the JUnit test cases simply run in the IDE every class under the **src/test/** folder

### H2 Database

To connect to the database via web interface go to http://localhost:8080/h2-console after at least one run.
username: sa
password: password
