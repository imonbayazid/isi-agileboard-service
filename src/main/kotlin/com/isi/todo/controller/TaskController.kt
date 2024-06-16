package com.isi.todo.controller

import com.isi.todo.client.userService.UserDetailsService
import com.isi.todo.core.ApiResponse
import com.isi.todo.core.ResponseHandler
import com.isi.todo.db.entity.Task
import com.isi.todo.db.model.TaskStatus
import com.isi.todo.dto.TaskDto
import com.isi.todo.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1")
class TaskController(
    private val taskService: TaskService
) {

    @GetMapping("/boards/{id}/tasks")
    fun getAllTasks(@PathVariable id: String): ResponseEntity<ApiResponse> {
        val boardId = UUID.fromString(id)
        val tasks = taskService.getTask(boardId)
        return ResponseHandler.generateResponse(HttpStatus.OK, tasks, true)
    }

    @PostMapping("/boards/{id}/tasks")
    fun createTask(@PathVariable id: String, @RequestBody taskDto: TaskDto): ResponseEntity<ApiResponse> {
        val boardId = UUID.fromString(id)
        val userId = UUID.fromString(taskDto.userId)
        val createdTask = taskService.createTask(boardId, taskDto.name, taskDto.description, userId )
        // why getting task inside board
        return ResponseHandler.generateResponse(HttpStatus.CREATED, createdTask, true)
    }

    @PutMapping("/tasks/{id}")
    fun updateTask(@PathVariable id: String, @RequestBody task: Task) : ResponseEntity<ApiResponse> {
        val taskId = UUID.fromString(id)
        val updatedTask = taskService.updateTask(taskId, task)
        return ResponseHandler.generateResponse(HttpStatus.OK, updatedTask, true)
    }

    @PatchMapping("/tasks/{id}")
    fun patchTask(@PathVariable id: String, @RequestBody task: Map<String, Any>): ResponseEntity<ApiResponse> {
        val taskId = UUID.fromString(id)
        val existingTask = taskService.getTask(taskId)
        val updatedTask = existingTask.copy(
            name = task["name"] as? String ?: existingTask.name,
            description = task["description"] as? String ?: existingTask.description,
            userId = UUID.fromString(task["user"] as? String ?: existingTask.userId.toString()),
            status = TaskStatus.valueOf(task["status"] as? String ?: existingTask.status.name)
        )
        val res = taskService.updateTask(taskId, updatedTask)
        return ResponseHandler.generateResponse(HttpStatus.OK, res, true)
    }

    @DeleteMapping("/tasks/{id}")
    fun deleteTask(@PathVariable id: String) : ResponseEntity<ApiResponse> {
        val taskId = UUID.fromString(id)
        taskService.deleteTask(taskId)
        return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null, true)
    }

}