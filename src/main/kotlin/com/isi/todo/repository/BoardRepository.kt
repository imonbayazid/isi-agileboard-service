package com.isi.todo.repository

import com.isi.todo.db.model.Board
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BoardRepository : JpaRepository<Board, UUID>