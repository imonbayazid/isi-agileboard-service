package com.isi.todo.dto

import com.isi.todo.db.model.TaskStatus
import com.isi.todo.db.model.User
import java.util.*

data class TaskDto(val id: UUID?, val name: String?, val description: String?, val userId: String?, val status: TaskStatus?)
data class TaskResponse(val id: UUID, val name: String, val description: String?, val status: TaskStatus, val user: User?)
