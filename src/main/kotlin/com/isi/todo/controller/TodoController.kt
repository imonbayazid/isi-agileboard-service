package com.isi.todo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoController {

    @GetMapping("/test")
    fun test(): String{
        return "testing.."
    }
}