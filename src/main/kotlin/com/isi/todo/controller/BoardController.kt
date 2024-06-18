package com.isi.todo.controller

import com.isi.todo.core.ApiResponse
import com.isi.todo.core.ResponseHandler
import com.isi.todo.dto.BoardDto
import com.isi.todo.service.BoardService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1")
class BoardController(
    private val boardService: BoardService
) {

    @GetMapping("/boards")
    fun getAllBoards(): ResponseEntity<ApiResponse> {
        val boards = boardService.getAllBoards()
        return ResponseHandler.generateResponse(HttpStatus.OK, boards, true)
    }

    @PostMapping("/boards")
    fun createBoard(@RequestBody boardDto: BoardDto): ResponseEntity<ApiResponse> {
        val createdBoard = boardService.createBoard(boardDto)
        return ResponseHandler.generateResponse(HttpStatus.CREATED, createdBoard, true)
    }


    @GetMapping("/boards/{id}")
    fun getBoard(@PathVariable id: String): ResponseEntity<ApiResponse> {
        val boardId = UUID.fromString(id)
        val res = boardService.getBoardById(boardId)
        return ResponseHandler.generateResponse(HttpStatus.OK, res, true)
    }

    @DeleteMapping("/boards/{id}")
    fun deleteBoard(@PathVariable id: String): ResponseEntity<ApiResponse> {
        val boardId = UUID.fromString(id)
        boardService.deleteBoard(boardId)
        return ResponseHandler.generateResponse(HttpStatus.OK, null, true)
    }
}