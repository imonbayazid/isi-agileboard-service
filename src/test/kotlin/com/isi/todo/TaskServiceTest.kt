package com.isi.todo

import com.isi.todo.db.entity.Board
import com.isi.todo.db.entity.Task
import com.isi.todo.db.model.TaskStatus
import com.isi.todo.dto.TaskDto
import com.isi.todo.repository.BoardRepository
import com.isi.todo.repository.TaskRepository
import com.isi.todo.service.TaskService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class TaskServiceTest : StringSpec({

    val boardRepository = mockk<BoardRepository>()
    val taskRepository = mockk<TaskRepository>()
    val taskService = TaskService(boardRepository, taskRepository)

    /*"should create a new task" {
        val boardId = UUID.randomUUID()
        val taskDto = TaskDto(id = UUID.randomUUID(), name = "New Task", description = "Description", userId = UUID.randomUUID().toString(), status = TaskStatus.STARTED)
        val board = Board(id = boardId, name = "Board 1", tasks = mutableListOf())
        val task = Task(id = UUID.randomUUID(), name = taskDto.name!!, description = taskDto.description, userId = UUID.fromString(taskDto.userId), board = board)

        every { boardRepository.findById(boardId) } returns Optional.of(board)
        every { taskRepository.save(any()) } returns task

        val result = taskService.createTask(boardId, taskDto)

        result.name shouldBe "New Task"
        result.description shouldBe "Description"
        //verify { taskRepository.save(any()) }
    } */


    "should retrieve tasks by board ID" {
        val boardId = UUID.randomUUID()
        val board = Board(id = boardId, name = "Board 1", tasks = mutableListOf(
            Task(id = UUID.randomUUID(), name = "Task 1", description = "Description 1", userId = UUID.randomUUID(), board = Board(name = "Sample Board")),
            Task(id = UUID.randomUUID(), name = "Task 2", description = "Description 2", userId = UUID.randomUUID(), board = Board(name = "Sample Board"))
        ))
        every { boardRepository.findById(boardId) } returns Optional.of(board)

        val result = taskService.getTasksByBoardId(boardId)

        result.size shouldBe 2
        result[0].name shouldBe "Task 1"
        result[1].name shouldBe "Task 2"
    }

    "should update a task" {
        val taskId = UUID.randomUUID()
        val existingTask = Task(id = taskId, name = "Old Task", userId = UUID.randomUUID(), board = Board(name = "Sample Board"))

        val taskDto = TaskDto(id = taskId, name = "Updated Task", description = "Updated Description", userId = existingTask.userId.toString(), status = TaskStatus.STARTED)
        every { taskRepository.findById(taskId) } returns Optional.of(existingTask)
        every { taskRepository.save(any()) } answers { firstArg() }

        val result = taskService.updateTask(taskId, taskDto)

        result.name shouldBe "Updated Task"
    }

    "should delete a task by ID" {
        val taskId = UUID.randomUUID()
        every { taskRepository.existsById(taskId) } returns true
        every { taskRepository.deleteById(taskId) } returns Unit

        taskService.deleteTask(taskId)

        verify { taskRepository.deleteById(taskId) }
    }

    "should delete tasks by user ID" {
        val userId = UUID.randomUUID()
        val tasks = listOf(
            Task(id = UUID.randomUUID(), name = "Task 1", userId = userId, board = Board(name = "Sample Board")),
            Task(id = UUID.randomUUID(), name = "Task 2", userId = userId, board = Board(name = "Sample Board"))
        )
        every { taskRepository.findAllByUserId(userId) } returns tasks
        every { taskRepository.deleteAll(tasks) } returns Unit

        taskService.deleteTasksByUserId(userId)

        verify { taskRepository.deleteAll(tasks) }
    }
})


