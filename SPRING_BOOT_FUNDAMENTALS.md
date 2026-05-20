# Spring Boot Fundamentals: Annotations and Request Flow

This guide explains the core concepts of Spring Boot, focusing on annotations and how API requests are handled from start to finish.

---

## What Are Annotations?

Annotations are special markers in Java that provide information about your code. They are **not part of the code itself** and do **not directly affect the code logic**, but they **provide instructions to frameworks** (like Spring) on how to treat the code.

Annotations start with the `@` symbol and are placed above class declarations, method declarations, or field declarations.

### Simple Analogy

Think of annotations like **metadata tags** or **labels**. If you're storing files in a cabinet, you might label a folder with "IMPORTANT" or "ARCHIVED". The label doesn't change the content of the files, but it tells anyone handling the cabinet how to treat those files.

Similarly, when you write `@RestController` above a class, you're not adding code logic. You're telling Spring: "Treat this class as a REST controller."

---

## Key Annotations in Your Project

### 1. `@RestController`

**Location**: Above a class declaration (in your `TaskController`)

**What it does**: 
- Marks the class as a REST API controller.
- Tells Spring that every method in this class will **return data directly** (as JSON), not HTML views.
- It's a combination of two other annotations: `@Controller` and `@ResponseBody`.

**Example**:
```java
@RestController
public class TaskController {
    // Methods here return data (JSON), not views
}
```

When you access an endpoint in this controller, Spring automatically converts the returned object to JSON for you.

---

### 2. `@RequestMapping`

**Location**: Above a class or method declaration

**What it does**: 
- Defines the base URL path for all endpoints in the controller.
- All routes in the controller will start with this path.

**Example**:
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    // All methods here will start with /api/tasks
}
```

If you have a method with `@GetMapping`, it will combine with the `@RequestMapping` path. For example:
- `@GetMapping` → `GET /api/tasks`
- `@GetMapping("/{id}")` → `GET /api/tasks/{id}`

---

### 3. `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`

**Location**: Above method declarations

**What they do**: 
- Map HTTP requests to specific methods.
- `@GetMapping`: Handles `GET` requests (retrieve data).
- `@PostMapping`: Handles `POST` requests (create new data).
- `@PutMapping`: Handles `PUT` requests (update existing data).
- `@DeleteMapping`: Handles `DELETE` requests (remove data).

**Example**:
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @GetMapping
    public List<Task> getAllTasks() {
        // Handles: GET /api/tasks
    }
    
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable long id) {
        // Handles: GET /api/tasks/1, /api/tasks/2, etc.
    }
    
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        // Handles: POST /api/tasks
    }
}
```

---

### 4. `@PathVariable` ⭐ (Important for you)

**Location**: Above method parameters

**What it does**: 
- Extracts values from the URL path and passes them to the method.
- The value inside `{...}` in the URL becomes a parameter in your method.

**Example**:

```java
@GetMapping("/{id}")
public Task getTaskById(@PathVariable long id) {
    // If someone calls: GET /api/tasks/5
    // The value 5 is extracted from the URL path and passed as 'id'
    return taskService.getTaskById(id);
}
```

**How it works**:
1. User makes a request: `GET http://localhost:8080/api/tasks/5`
2. Spring sees the URL pattern `/{id}` and identifies that `5` is the value for `id`.
3. Spring passes `5` to the `id` parameter in your method.
4. Your method receives `id = 5`.

**Another example with multiple path variables**:
```java
@GetMapping("/{userId}/tasks/{taskId}")
public Task getTaskForUser(@PathVariable long userId, @PathVariable long taskId) {
    // If someone calls: GET /api/users/10/tasks/3
    // userId = 10, taskId = 3
}
```

---

### 5. `@RequestBody` ⭐ (Important for you)

**Location**: Above method parameters

**What it does**: 
- Automatically converts the JSON body of the HTTP request into a Java object.
- Takes the raw JSON data and "deserializes" it into your object.

**Example**:

```java
@PostMapping
public Task createTask(@RequestBody Task task) {
    // If someone sends: POST /api/tasks with JSON body:
    // {
    //     "name": "Learn Spring Boot",
    //     "description": "Complete the tutorial"
    // }
    // Spring automatically creates a Task object with those values
    return taskService.createTask(task);
}
```

**How it works**:
1. User sends a POST request with JSON body:
   ```json
   {
       "name": "Learn Spring Boot",
       "description": "Complete the tutorial"
   }
   ```
2. Spring intercepts the request and sees `@RequestBody Task task`.
3. Spring takes the JSON and converts it to a `Task` object (matching field names).
4. Your method receives a fully populated `Task` object.

**Important**: The JSON field names **must match** the field names in your Java class. In your `Task` class:
```java
public class Task {
    private Long id;
    private String name;           // Matches JSON key "name"
    private String description;    // Matches JSON key "description"
}
```

---

### 6. `@Service`

**Location**: Above a class declaration

**What it does**: 
- Marks the class as a "Service" component.
- Tells Spring that this class contains business logic.
- Spring will automatically create and manage an instance of this class.

**Example** (in `TaskService.java`):
```java
@Service
public class TaskService {
    // Contains business logic for managing tasks
}
```

---

### 7. Dependency Injection (Constructor Injection)

**Location**: In the constructor of a class that needs dependencies

**What it does**: 
- Automatically provides required objects (dependencies) to your class.
- Spring creates instances of services and injects them where needed.

**Example** (in `TaskController.java`):
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskService taskService;
    
    // Constructor with dependency injection
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
}
```

**How it works**:
1. Spring sees that `TaskController` needs a `TaskService`.
2. Spring finds the `TaskService` (marked with `@Service`).
3. Spring creates an instance of `TaskService`.
4. Spring passes it to the constructor of `TaskController`.
5. Now `TaskController` can use `taskService` in its methods.

---

## Complete Request Flow Diagram

Here's how a request travels through your entire application:

```
1. USER REQUEST
   └─> Sends: GET http://localhost:8080/api/tasks/1

2. SPRING ROUTER
   └─> Matches the URL pattern to a controller method
   └─> Finds: TaskController.getTaskById()

3. ANNOTATION PROCESSING
   └─> Sees: @PathVariable long id
   └─> Extracts the path variable: id = 1
   └─> Passes it to the method

4. DEPENDENCY INJECTION
   └─> Injects TaskService into TaskController
   └─> Controller can now use taskService

5. METHOD EXECUTION
   └─> TaskController.getTaskById(id) is called
   └─> Calls: taskService.getTaskById(1)

6. SERVICE LOGIC
   └─> TaskService searches the in-memory list
   └─> Returns: Task object with id=1

7. RESPONSE CONVERSION
   └─> Spring sees @RestController
   └─> Converts Task object to JSON
   └─> Sends JSON response to client

8. CLIENT RECEIVES
   └─> Gets back: { "id": 1, "name": "...", "description": "..." }
```

---

## Example: Complete Request-Response Cycle

Let's trace a complete example: **Creating a new task (POST request)**

### Step 1: Client sends POST request

```bash
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
    "name": "Buy groceries",
    "description": "Milk, eggs, bread"
}
```

### Step 2: Spring Router matches the request

Spring looks at all controllers and sees:
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        // This method matches!
    }
}
```

### Step 3: Spring processes annotations

- Sees `@PostMapping` → This is a POST request handler
- Sees `@RequestBody Task task` → Convert JSON to Task object
  ```json
  {
      "name": "Buy groceries",
      "description": "Milk, eggs, bread"
  }
  ```
  becomes:
  ```java
  Task task = new Task();
  task.setName("Buy groceries");
  task.setDescription("Milk, eggs, bread");
  ```

### Step 4: Dependency injection happens

Spring injects `TaskService` into `TaskController`:
```java
public TaskController(TaskService taskService) {
    this.taskService = taskService;  // Injected by Spring
}
```

### Step 5: Method is executed

```java
public Task createTask(@RequestBody Task task) {
    return taskService.createTask(task);
}
```

This calls the service:
```java
public Task createTask(Task task) {
    task.setId(counter.incrementAndGet());  // Sets id = 1 (or next number)
    tasks.add(task);
    return task;
}
```

### Step 6: Response is converted to JSON

Spring sees `@RestController` and automatically converts the returned Task object to JSON:

```json
{
    "id": 1,
    "name": "Buy groceries",
    "description": "Milk, eggs, bread"
}
```

### Step 7: Response is sent to client

The client receives the response with status `200 OK` and the JSON body.

---

## All Annotations Summary

| Annotation | Location | Purpose |
|---|---|---|
| `@RestController` | Class | Mark as REST API controller |
| `@RequestMapping` | Class/Method | Define base URL path |
| `@GetMapping` | Method | Handle GET requests |
| `@PostMapping` | Method | Handle POST requests |
| `@PutMapping` | Method | Handle PUT requests |
| `@DeleteMapping` | Method | Handle DELETE requests |
| `@PathVariable` | Parameter | Extract value from URL path |
| `@RequestBody` | Parameter | Convert JSON request body to object |
| `@Service` | Class | Mark as service (business logic) |
| `@Autowired` | Field | Inject dependency (old way) |
| Constructor | Constructor | Inject dependency (recommended way) |

---

## How Your To-Do API Works

### GET all tasks
```
GET /api/tasks
→ TaskController.getAllTasks()
→ taskService.getAllTasks()
→ Returns List<Task>
→ Converted to JSON → Sent to client
```

### GET a specific task
```
GET /api/tasks/1
→ @PathVariable extracts id=1
→ TaskController.getTaskById(1)
→ taskService.getTaskById(1)
→ Returns Task with id=1
→ Converted to JSON → Sent to client
```

### POST create new task
```
POST /api/tasks
Body: { "name": "...", "description": "..." }
→ @RequestBody converts JSON to Task object
→ TaskController.createTask(task)
→ taskService.createTask(task)
→ Service assigns ID and adds to list
→ Returns Task object
→ Converted to JSON → Sent to client
```

### PUT update task
```
PUT /api/tasks/1
Body: { "name": "...", "description": "..." }
→ @PathVariable extracts id=1
→ @RequestBody converts JSON to Task object
→ TaskController.updateTask(1, task)
→ taskService.updateTask(1, task)
→ Service updates the task
→ Returns updated Task object
→ Converted to JSON → Sent to client
```

### DELETE a task
```
DELETE /api/tasks/1
→ @PathVariable extracts id=1
→ TaskController.deleteTask(1)
→ taskService.deleteTask(1)
→ Service removes task from list
→ Returns 204 No Content status
```

---

## Key Takeaways

1. **Annotations are instructions for Spring**: They tell Spring how to handle your code without adding logic.

2. **@PathVariable extracts URL values**: When you have `/tasks/{id}`, the `id` part is extracted and passed to your method.

3. **@RequestBody converts JSON**: When the client sends JSON, Spring automatically converts it to a Java object.

4. **Request flow**: User Request → Router → Controller Method → Service Logic → Response Conversion → Send to Client.

5. **Dependency Injection**: Spring automatically provides dependencies (like services) to your classes through constructors.

6. **Layered Architecture**: Controllers handle HTTP, Services handle logic, Models represent data. This separation makes code clean and maintainable.

---

## Next Steps

Now that you understand annotations and request flow:
- Try making requests with Thunder Client and observe how the data flows through your layers.
- Experiment by changing field names in JSON and see what happens.
- Try adding a new field to the Task model and see how it propagates through the layers.
