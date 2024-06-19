package com.isi.todo

import com.isi.todo.client.userService.UserDetailsService
import com.isi.todo.db.entity.Board
import com.isi.todo.dto.BoardDto
import com.isi.todo.repository.BoardRepository
import com.isi.todo.service.BoardService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


@SpringBootTest
class BoardServiceTest : StringSpec({

    val boardRepository = mockk<BoardRepository>()
    val userDetailsService = mockk<UserDetailsService>()
    val boardService = BoardService(boardRepository, userDetailsService)

    "should retrieve all boards" {
        val boards = listOf(
            Board(id = UUID.randomUUID(), name = "Board 1"),
            Board(id = UUID.randomUUID(), name = "Board 2")
        )
        every { boardRepository.findAll() } returns boards

        val result = boardService.getAllBoards()

        result.size shouldBe 2
        result[0].name shouldBe "Board 1"
        result[1].name shouldBe "Board 2"
    }


    "should create a new board" {
        val boardDto = BoardDto(id = null, name = "New Board", description = "Description")
        val board = Board(id = UUID.randomUUID(), name = boardDto.name, description = boardDto.description)
        every { boardRepository.save(any()) } returns board

        val result = boardService.createBoard(boardDto)

        result.name shouldBe "New Board"
        result.description shouldBe "Description"
    }

    "should retrieve a board by ID" {
        val boardId = UUID.randomUUID()
        val board = Board(id = boardId, name = "Board 1", description = "Description")
        every { boardRepository.findById(boardId) } returns Optional.of(board)

        val result = boardService.getBoardById(boardId)

        result.id shouldBe boardId
        result.name shouldBe "Board 1"
        result.description shouldBe "Description"
    }

})

