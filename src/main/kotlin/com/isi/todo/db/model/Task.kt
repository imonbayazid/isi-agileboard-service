package com.isi.todo.db.model

import jakarta.persistence.*
import java.util.*


@Entity
data class Task(
    @Id @GeneratedValue val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    val user: UUID,
    @Enumerated(EnumType.STRING)
    val status: TaskStatus = TaskStatus.CREATED
)