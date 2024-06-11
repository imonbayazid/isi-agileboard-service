package com.isi.todo.core

import org.springframework.http.HttpStatus

open class CustomException(
    val code: HttpStatus,
    override val message: String
) : RuntimeException(message)

class ResourceNotFoundException(message: String) : CustomException(HttpStatus.NOT_FOUND, message)

class BadRequestException(message: String) : CustomException(HttpStatus.BAD_REQUEST, message)
