package com.isi.todo.core

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

data class ErrorResponse(
    val code: Int,
    val msg: String,
    val status: HttpStatus
)

@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomExceptions(ex: CustomException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = ex.code.value(),
            msg = ex.message,
            status = ex.code
        )
        return ResponseEntity(errorResponse, ex.code)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            msg = ex.localizedMessage ?: "Unexpected error",
            status = HttpStatus.INTERNAL_SERVER_ERROR
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}