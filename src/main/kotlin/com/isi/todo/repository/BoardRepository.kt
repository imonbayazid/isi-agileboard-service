package com.isi.todo.repository

import com.isi.todo.db.entity.Board
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BoardRepository : JpaRepository<Board, UUID>{
   /* //SELECT new com.isi.todo.dto.BoardDto(b.id, b.name) FROM Board b
    @Query("SELECT b.id as id, b.name as name FROM Board b")
    fun findAllBoardIdsAndNames(): List<BoardProjection> */

    /*@EntityGraph(attributePaths = ["tasks"])
    fun findAllBoardsWithTasks(): List<Board>

    @EntityGraph(attributePaths = ["tasks"])
    fun findBoardByIdWithTasks(id: UUID): Board */

    /*@EntityGraph(attributePaths = ["tasks"])
    fun findBoardById(id: UUID): Optional<Board> */

}