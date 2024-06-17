package com.isi.todo.controller

import com.isi.todo.client.userService.UserDetailsService
import com.isi.todo.core.ApiResponse
import com.isi.todo.core.ResponseHandler
import com.isi.todo.db.entity.Task
import com.isi.todo.db.model.TaskStatus
import com.isi.todo.dto.*
import com.isi.todo.service.BoardService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1")
class TodoController(
    private val boardService: BoardService
) {

    @GetMapping("/test")
    fun test(): String{
        return "testing.."
    }

}