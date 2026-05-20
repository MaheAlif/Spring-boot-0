# Spring Boot To-Do List Demo

This is a simple to-do list application built with Spring Boot. It provides a REST API for performing CRUD (Create, Read, Update, Delete) operations on tasks.

## Prerequisites

- Java 17 or later
- Maven (or you can use the included Maven wrapper)

## How to Run the Project

You can run this project using the Maven wrapper provided.

### On Windows:

Open a command prompt or PowerShell and run the following command from the project's root directory:

```shell
.\mvnw.cmd spring-boot:run
```

### On macOS and Linux:

Open a terminal and run the following command from the project's root directory:

```shell
./mvnw spring-boot:run
```

The application will start and be accessible at `http://localhost:8080`.

## API Endpoints

The API provides the following endpoints for managing tasks.

### Create a Task
- **Method**: `POST`
- **URL**: `/api/tasks`
- **Body**:
  ```json
  {
      "name": "My First Task",
      "description": "Learn how to use the POST endpoint."
  }
  ```

### Get All Tasks
- **Method**: `GET`
- **URL**: `/api/tasks`

### Get a Single Task
- **Method**: `GET`
- **URL**: `/api/tasks/{id}`

### Update a Task
- **Method**: `PUT`
- **URL**: `/api/tasks/{id}`
- **Body**:
  ```json
  {
      "name": "My Updated Task Name",
      "description": "This task has been updated."
  }
  ```

### Delete a Task
- **Method**: `DELETE`
- **URL**: `/api/tasks/{id}`
