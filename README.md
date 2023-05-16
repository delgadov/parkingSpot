# Parking Spot Management System
The Parking Spot Management System is a Java-based application that allows users to manage parking spots in a parking lot. It provides functionality to create, update, retrieve, and delete parking spots.

## Features
* Create a new parking spot with details such as spot number, license plate, car brand, and more.
* Update the information of an existing parking spot.
* Retrieve information about a specific parking spot by its ID.
* Delete a parking spot from the system.
* List all parking spots available in the system.

## Technologies Used
* Java
* Spring Boot
* Spring Data JPA
* Postgres (or any other database of your choice)
* Maven (or Gradle) for dependency management

## Getting Started
To run the Parking Spot Management System locally, follow these steps:

1. Clone the repository: `git clone https://github.com/delgadov/parkingSpot.git`
2. Set up the database and update the database configuration in `application.properties`.
3. Build the project using your preferred build tool (Maven or Gradle).
4. Run the application.
5. Access the application in your browser at `http://localhost:8080` (or the specified port).

  ## API Endpoints
The Parking Spot Management System provides the following RESTFUL API endpoints:

* **`GET /rest/api/v1/parking-spot`:** Retrieve a list of all parking spots.
* **`GET /rest/api/v1/parking-spot/{id}`:** Retrieve details of a specific parking spot by its ID.
* **`POST /rest/api/v1/parking-spot`:** Create a new parking spot.
* **`PUT /rest/api/v1/parking-spot/{id}`:** Update the details of a parking spot.
* **`DELETE /rest/api/v1/parking-spot/{id}`:** Delete a parking spot by its ID.
Refer to the API documentation for detailed information about request/response formats and parameters.
