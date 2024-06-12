package com.isi.todo.repository

import com.isi.todo.db.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TaskRepository : JpaRepository<Task, UUID> {
}