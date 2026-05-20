# To-Do App API Test Bodies

This file contains example JSON bodies for testing the Spring Boot To-Do application API.

---

### 1. Create a New Task

*   **Method**: `POST`
*   **URL**: `http://localhost:8080/api/tasks`
*   **Body**:

```json
{
    "name": "My First Task",
    "description": "Learn how to use the POST endpoint."
}
```

---

### 2. Update an Existing Task

*   **Method**: `PUT`
*   **URL**: `http://localhost:8080/api/tasks/{id}` (e.g., `http://localhost:8080/api/tasks/1`)
*   **Body**:

```json
{
    "name": "My Updated Task Name",
    "description": "This task has been updated."
}
```

---
