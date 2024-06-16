package com.isi.todo.client.userService

import com.isi.todo.dto.UserResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UserDetailsService(
    private val restTemplate: RestTemplate,
    @Value("\${userservice.api.url}") private val userServiceApiUrl: String
) {

    fun getUserDetails(): UserResponse? {
        return restTemplate.getForObject(userServiceApiUrl, UserResponse::class.java)
    }
}