package com.isi.todo.service

import com.isi.todo.core.ResourceNotFoundException
import com.isi.todo.db.entity.Board
import com.isi.todo.db.entity.Task
import com.isi.todo.db.model.TaskStatus
import com.isi.todo.dto.TaskDto
import com.isi.todo.repository.BoardRepository
import com.isi.todo.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class TaskService(private val boardRepository: BoardRepository, private val taskRepository: TaskRepository) {

    // <editor-fold desc="public section">
    fun getBoardById(id: UUID): Board =
        boardRepository.findById(id).orElseThrow { NoSuchElementException("Board not found") }

    fun getTaskById(id: UUID): Task =
        taskRepository.findById(id).orElseThrow { NoSuchElementException("Task not found") }

    fun getTasksByBoardId(boardId: UUID): List<TaskDto> {
        val board = getBoardById(boardId)
        return board.tasks.map { it.toDto() }
    }

    @Transactional
    fun createTask(boardId: UUID, taskDto: TaskDto): TaskDto {
        val board = getBoardById(boardId)
        val task = Task(
            name = taskDto.name!!,
            description = taskDto.description,
            userId = UUID.fromString(taskDto.userId),
            board = board
        )
        board.tasks.add(task)
        boardRepository.save(board)
        return task.toDto()
    }

    fun updateTask(taskId: UUID, taskDto: TaskDto): TaskDto {
        val existingTask = getTaskById(taskId)
        val updatedTask = existingTask.copy(
            name = taskDto.name!!,
            description = taskDto.description,
            userId = UUID.fromString(taskDto.userId),
            status = taskDto.status!!
        )
        return taskRepository.save(updatedTask).toDto()
    }

    fun patchTask(taskId: UUID, taskDto: TaskDto): TaskDto {
        val existingTask = getTaskById(taskId)
        val updatedTask = existingTask.copy(
            name = taskDto.name ?: existingTask.name,
            description = taskDto.description ?: existingTask.description,
            userId = taskDto.userId?.let { UUID.fromString(it) } ?: existingTask.userId,
            status = taskDto.status ?: existingTask.status
        )
        return taskRepository.save(updatedTask).toDto()
    }

    fun deleteTask(taskId: UUID) {
        if (!taskRepository.existsById(taskId)) {
            throw ResourceNotFoundException("Task not found with id $taskId")
        }
        taskRepository.deleteById(taskId)
    }

    fun deleteTasksByUserId(userId: UUID) {
        val tasks = taskRepository.findAllByUserId(userId)
        if (tasks.isEmpty()) {
            throw ResourceNotFoundException("Task not found for the user id $userId")
        }
        taskRepository.deleteAll(tasks)
    }
    // </editor-fold>

    // <editor-fold desc="private section">
    private fun Task.toDto() = TaskDto(id, name, description, userId.toString(), status)
    // </editor-fold>
}