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

    fun getBoard(id: UUID): Board = boardRepository.findById(id).orElseThrow { NoSuchElementException("Board not found") }

    fun getTask(id: UUID): Task = taskRepository.findById(id).orElseThrow { NoSuchElementException("Task not found") }

    @Transactional
    fun createTask(boardId: UUID, name: String, description: String?, user: UUID): TaskDto {
        val board = getBoard(boardId)  // ???
        val task = Task(name = name, description = description, userId = user, board = board)
        board.tasks.add(task)
        boardRepository.save(board) // ?
        /// this save operation will also persist the new task to the database due to the cascading settings (if any) defined on the tasks relationship in the Board entity.
        return TaskDto(id = task.id, name = task.name, description = task.description, userId = task.userId.toString(), status = task.status)
    }


    fun updateTask(taskId: UUID, task: TaskDto): TaskDto {
        val existingTask = taskRepository.findById(taskId).orElseThrow { NoSuchElementException("Task not found") }
        val updatedTask = existingTask.copy(name = task.name!!, description = task.description, userId = UUID.fromString(task.userId), status = task.status!!)
        val res = taskRepository.save(updatedTask)
        return TaskDto(id = res.id, name = res.name, description = res.description, userId = res.userId.toString(), status = res.status)
    }

    fun patchTask(taskId: UUID, task: TaskDto): TaskDto? {
        val existingTask = taskRepository.findById(taskId).orElseThrow { NoSuchElementException("Task not found") }
        val updatedTask = existingTask.copy(
            name = task.name ?: existingTask.name,
            description = task.description ?: existingTask.description,
            userId = task.userId?.let { UUID.fromString(it) } ?: existingTask.userId,
            status = task.status ?: existingTask.status)
        taskRepository.save(updatedTask)
        return TaskDto(id = updatedTask.id, name = updatedTask.name, description = updatedTask.description, userId = updatedTask.userId.toString(), status = updatedTask.status)
    }



    fun deleteTask(taskId: UUID) {
        if (!taskRepository.existsById(taskId)) {
            throw ResourceNotFoundException("Task not found with id $taskId")
        }
        taskRepository.deleteById(taskId)
    }

    fun deleteTasksByUserId(userId: UUID) {
        val tasks = taskRepository.findAllByUserId(userId)
        if(tasks.isEmpty()) {
            throw ResourceNotFoundException("Task not found for the user id $userId")
        }
        taskRepository.deleteAll(tasks)
    }

}