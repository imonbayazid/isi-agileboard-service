package com.isi.todo.db.model

import jakarta.persistence.*
import java.util.*

@Entity
data class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),

    val name: String,

    val description: String? = null,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val tasks: MutableList<Task> = mutableListOf()
)
