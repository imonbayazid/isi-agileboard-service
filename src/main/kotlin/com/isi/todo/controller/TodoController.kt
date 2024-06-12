package com.isi.todo.controller

import com.isi.todo.core.ApiResponse
import com.isi.todo.core.ResponseHandler
import com.isi.todo.db.model.Task
import com.isi.todo.db.model.TaskStatus
import com.isi.todo.dto.*
import com.isi.todo.service.BoardService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.util.*

@RestController
@RequestMapping("/boards/v1")
class TodoController(private val boardService: BoardService, private val restTemplate: RestTemplate) {

    @GetMapping("/test")
    fun test(): String{
        return "testing.."
    }

    @GetMapping
    fun getAllBoards(): ResponseEntity<ApiResponse> {
        val boards = boardService.getAllBoards()
        return ResponseHandler.generateResponse(HttpStatus.OK, boards, true)
    }

    @PostMapping
    fun createBoard(@RequestBody boardDto: BoardDto): ResponseEntity<ApiResponse> {
        val createdBoard = boardService.createBoard(boardDto.name, boardDto.description)
        return ResponseHandler.generateResponse(HttpStatus.CREATED, createdBoard, true)
    }

    @GetMapping("/{id}")
    fun getBoard(@PathVariable id: UUID): ResponseEntity<ApiResponse> {
        val board = boardService.getBoard(id)
        val tasksWithUserDetails = board.tasks.map { task ->
            val userResponse = restTemplate.getForObject("https://randomuser.me/api/?seed=${task.user}", UserResponse::class.java)
            TaskResponse(task.id, task.name, task.description, task.status, userResponse?.results?.firstOrNull()?.toUser())
        }
        val res = BoardResponse(board.id, board.name, board.description, tasksWithUserDetails)
        return ResponseHandler.generateResponse(HttpStatus.OK, res, true)
    }

    @DeleteMapping("/{id}")
    fun deleteBoard(@PathVariable id: UUID): ResponseEntity<ApiResponse> {
        boardService.deleteBoard(id)
        return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null, true)
    }


    @GetMapping
    fun getAllTasks(@PathVariable id: UUID): ResponseEntity<ApiResponse> {
        val tasks = boardService.getTask(id)
        return ResponseHandler.generateResponse(HttpStatus.OK, tasks, true)
    }

    @PostMapping("/{id}/tasks")
    fun createTask(@PathVariable id: UUID, @RequestBody taskDto: TaskDto): ResponseEntity<ApiResponse> {
        val createdTask = boardService.createTask(id, taskDto.name, taskDto.description, taskDto.user)
        return ResponseHandler.generateResponse(HttpStatus.CREATED, createdTask, true)
    }

    @PutMapping("/tasks/{id}")
    fun updateTask(@PathVariable id: UUID, @RequestBody task: Task) : ResponseEntity<ApiResponse> {
        val updatedTask = boardService.updateTask(id, task)
        return ResponseHandler.generateResponse(HttpStatus.OK, updatedTask, true)
    }

    @PatchMapping("/tasks/{id}")
    fun patchTask(@PathVariable id: UUID, @RequestBody task: Map<String, Any>): ResponseEntity<ApiResponse> {
        val existingTask = boardService.getTask(id)
        val updatedTask = existingTask.copy(
            name = task["name"] as? String ?: existingTask.name,
            description = task["description"] as? String ?: existingTask.description,
            user = UUID.fromString(task["user"] as? String ?: existingTask.user.toString()),
            status = TaskStatus.valueOf(task["status"] as? String ?: existingTask.status.name)
        )
        val res = boardService.updateTask(id, updatedTask)
        return ResponseHandler.generateResponse(HttpStatus.OK, res, true)
    }

    @DeleteMapping("/tasks/{id}")
    fun deleteTask(@PathVariable id: UUID) : ResponseEntity<ApiResponse> {
        boardService.deleteTask(id)
        return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT, null, true)
    }
 
    @PostMapping("/webhooks/user-deleted")
    fun handleUserDeleted(@RequestBody payload: UserWebhookPayload): ResponseEntity<ApiResponse> {
        val id= UUID.fromString(payload.data.user)
        boardService.handleUserDeleted(id)
        return ResponseHandler.generateResponse(HttpStatus.OK, null, true)
    }

}