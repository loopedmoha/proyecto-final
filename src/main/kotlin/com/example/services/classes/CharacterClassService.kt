package com.example.services.classes

import com.example.models.player.CharacterClass
import com.example.repositories.ClassRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class CharacterClassService(private val classRepository: ClassRepository) : IClassService {
    override suspend fun findAll(): List<CharacterClass> {
        return classRepository.findAll()
    }

    override suspend fun findById(id: String): CharacterClass? {
        return classRepository.findById(id)
    }

    override suspend fun save(t: CharacterClass): CharacterClass {
        return classRepository.save(t)
    }

    override suspend fun update(t: CharacterClass): CharacterClass {
        return classRepository.update(t)
    }

    override suspend fun deleteById(id: String): Boolean {
        return classRepository.deleteById(id)
    }
}