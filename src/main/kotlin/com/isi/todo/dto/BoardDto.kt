package com.isi.todo.dto

import java.util.*

data class BoardDto(val id: UUID?, val name: String, val description: String?)
data class BoardResponse(val id: UUID, val name: String, val description: String?, val tasks: List<TaskResponse>)
