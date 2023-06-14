package com.example.services.misc

import com.example.models.User
import com.example.services.IServices
import java.util.UUID

interface IUserService : IServices<User, UUID> {
    suspend fun findByUserName(username: String): User?
}