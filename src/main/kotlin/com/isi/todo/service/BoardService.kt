package com.isi.todo.service

import com.isi.todo.db.model.Board
import com.isi.todo.db.model.Task
import com.isi.todo.repository.BoardRepository
import com.isi.todo.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BoardService(private val boardRepository: BoardRepository, private val taskRepository: TaskRepository) {

    fun getAllBoards(): List<Board> = boardRepository.findAll()

    fun createBoard(name: String, description: String?): Board {
        val board = Board(name = name, description = description)
        return boardRepository.save(board)
    }

    fun getBoard(id: UUID): Board = boardRepository.findById(id).orElseThrow { NoSuchElementException("Board not found") }

    fun deleteBoard(id: UUID) = boardRepository.deleteById(id)

    fun getTask(id: UUID): Task = taskRepository.findById(id).orElseThrow { NoSuchElementException("Task not found") }

    @Transactional
    fun createTask(boardId: UUID, name: String, description: String?, user: UUID): Task {
        val board = getBoard(boardId)
        val task = Task(name = name, description = description, user = user)
        board.tasks.add(task)
        boardRepository.save(board)
        return task
    }

    fun updateTask(taskId: UUID, task: Task): Task {
        val existingTask = taskRepository.findById(taskId).orElseThrow { NoSuchElementException("Task not found") }
        val updatedTask = existingTask.copy(name = task.name, description = task.description, user = task.user, status = task.status)
        return taskRepository.save(updatedTask)
    }

    fun deleteTask(taskId: UUID) = taskRepository.deleteById(taskId)

    @Transactional
    fun handleUserDeleted(userId: UUID) {
        // delete all tasks of that user
    }
}