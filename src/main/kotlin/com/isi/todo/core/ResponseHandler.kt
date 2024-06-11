package com.isi.todo.core

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiResponse(
    val status: HttpStatus,
    val data: Any?,
    val result: Boolean
)

object ResponseHandler {
    fun generateResponse(status: HttpStatus, data: Any?, result: Boolean): ResponseEntity<ApiResponse> {
        return ResponseEntity(ApiResponse(status, data, result), status)
    }
}