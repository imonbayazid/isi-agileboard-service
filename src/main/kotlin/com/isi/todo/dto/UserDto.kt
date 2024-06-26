package com.isi.todo.dto

import com.isi.todo.db.model.User
import java.util.*

data class UserName(val first: String, val last: String)
data class UserResponse(val results: List<UserResult>)
data class UserResult(val name: UserName, val email: String) {
    fun toUser() = User(UUID.randomUUID(), "${name.first} ${name.last}", email)
}

data class UserWebhookPayload(val time: String, val data: UserData)
data class UserData(val user: String)