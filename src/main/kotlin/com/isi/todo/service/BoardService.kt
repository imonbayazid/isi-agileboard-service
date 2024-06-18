package com.isi.todo.service

import com.isi.todo.client.userService.UserDetailsService
import com.isi.todo.core.ResourceNotFoundException
import com.isi.todo.db.entity.Board
import com.isi.todo.db.entity.Task
import com.isi.todo.dto.BoardDto
import com.isi.todo.dto.BoardResponse
import com.isi.todo.dto.TaskResponse
import com.isi.todo.repository.BoardRepository
import com.isi.todo.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val userDetailsService: UserDetailsService
) {

    fun getAllBoards(): List<BoardDto> {
        return boardRepository.findAll().map { board ->
            BoardDto(id = board.id, name = board.name, description = board.description)
        }
    }
    fun createBoard(name: String, description: String?): BoardDto {
        val board = Board(name = name, description = description)
        val savedBoard = boardRepository.save(board)
        return BoardDto(id = savedBoard.id, name = savedBoard.name, description = savedBoard.description)
    }

    fun getBoardById(id: UUID): BoardResponse {
           val board = boardRepository.findById(id)
               .orElseThrow { NoSuchElementException("Board not found") }
           val tasksWithUserDetails = board.tasks.map { task ->
               val userResponse = userDetailsService.getUserDetails() // get random user details
               TaskResponse(task.id, task.name, task.description, task.status, userResponse?.results?.firstOrNull()?.toUser())
           }
           return BoardResponse(board.id, board.name, board.description, tasksWithUserDetails)
    }

    fun deleteBoard(id: UUID) {
        if (!boardRepository.existsById(id)) {
            throw ResourceNotFoundException("Board not found with id $id")
        }
        boardRepository.deleteById(id)
    }

}