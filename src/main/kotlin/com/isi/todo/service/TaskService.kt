package com.isi.todo.service

import com.isi.todo.core.ResourceNotFoundException
import com.isi.todo.db.entity.Board
import com.isi.todo.db.entity.Task
import com.isi.todo.db.model.BoardProjection
import com.isi.todo.dto.TaskDto
import com.isi.todo.repository.BoardRepository
import com.isi.todo.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class TaskService(private val boardRepository: BoardRepository, private val taskRepository: TaskRepository) {


    fun getBoard(id: UUID): Board = boardRepository.findById(id).orElseThrow { NoSuchElementException("Board not found") }


    // ?? find all tasks by board id
    fun getTask(id: UUID): Task = taskRepository.findById(id).orElseThrow { NoSuchElementException("Task not found") }

    @Transactional
    fun createTask(boardId: UUID, name: String, description: String?, user: UUID): TaskDto {
        val board = getBoard(boardId)  // ???
        val task = Task(name = name, description = description, userId = user, board = board) //?
        board.tasks.add(task)
        boardRepository.save(board) // ?
        /// this save operation will also persist the new task to the database due to the cascading settings (if any) defined on the tasks relationship in the Board entity.
        return TaskDto(id = task.id, name = task.name, description = task.description, userId = task.userId.toString(), status = task.status)
    }

    /*
    *    @Transactional
    fun createTask(boardId: UUID, name: String, description: String?, user: UUID): Task {
        /*val board = getBoard(boardId)  // ???
        val task = Task(name = name, description = description, userId = user, board = board) //?
        board.tasks.add(task)
        boardRepository.save(board) // ?
        /// this save operation will also persist the new task to the database due to the cascading settings (if any) defined on the tasks relationship in the Board entity.
        return task */
        val task = Task(name = name, description = description, userId = user, board = board)

    }
    *
    * */

    fun updateTask(taskId: UUID, task: Task): Task {
        val existingTask = taskRepository.findById(taskId).orElseThrow { NoSuchElementException("Task not found") }
        val updatedTask = existingTask.copy(name = task.name, description = task.description, userId = task.userId, status = task.status)
        return taskRepository.save(updatedTask)
    }


    fun deleteTask(taskId: UUID) {
        if (!taskRepository.existsById(taskId)) {
            throw ResourceNotFoundException("Task not found with id $taskId")
        }
        taskRepository.deleteById(taskId)
    }

    fun deleteTasksByUserId(userId: UUID) {
        val tasks = taskRepository.findAllByUserId(userId)
        taskRepository.deleteAll(tasks)
    }

    /* @Transactional
     fun handleUserDeleted(userId: UUID) {
         // delete all tasks of that user
         val tasks = taskRepository.findAllByUser(userId)
         taskRepository.deleteAll(tasks)
     } */
}