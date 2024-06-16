package com.isi.todo.db.model

import java.util.*

interface BoardProjection {
    val id: UUID
    val name: String
}