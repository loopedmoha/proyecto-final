package com.example.services

interface IServices<T, ID> {
    suspend fun findAll(): List<T>
    suspend fun findById(id: ID): T?
    suspend fun save(t: T): T
    suspend fun update(t: T): T
    suspend fun deleteById(id: ID): Boolean
}