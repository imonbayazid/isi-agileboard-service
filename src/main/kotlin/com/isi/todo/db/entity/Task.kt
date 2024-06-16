package com.isi.todo.db.entity

import com.isi.todo.db.model.TaskStatus
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "tasks")
data class Task(
    @Id @GeneratedValue val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    val userId: UUID,
    @Enumerated(EnumType.STRING)
    val status: TaskStatus = TaskStatus.CREATED,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    val board: Board
)