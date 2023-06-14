package com.example.services.misc

import com.example.models.User
import com.example.repositories.UserRespository
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class UserService(private val userRepository: UserRespository) : IUserService {
    override suspend fun findAll(): List<User> {
        return userRepository.findAll()
    }

    override suspend fun deleteById(id: UUID): Boolean {
        return userRepository.deleteById(id)
    }

    override suspend fun update(t: User): User {
        return userRepository.update(t)
    }

    override suspend fun save(t: User): User {
        return userRepository.save(t)
    }

    override suspend fun findById(id: UUID): User? {
        return userRepository.findById(id)
    }

    override suspend fun findByUserName(username: String): User? {
        return userRepository.findByUsername(username)
    }
}