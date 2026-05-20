package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.models.Task;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task getTaskById(long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Task createTask(Task task) {
        task.setId(counter.incrementAndGet());
        tasks.add(task);
        return task;
    }

    public Task updateTask(long id, Task updatedTask) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            return task;
        }
        return null;
    }

    public boolean deleteTask(long id) {
        return tasks.removeIf(task -> task.getId() == id);
    }
}
