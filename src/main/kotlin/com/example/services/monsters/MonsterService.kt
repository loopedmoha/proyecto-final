package com.example.services.monsters

import com.example.models.Monster
import com.example.repositories.ItemRepository
import com.example.repositories.MonsterRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class MonsterService(  private val monsterRepository: MonsterRepository) : IMonsterService {
    override suspend fun findAll(): List<Monster> {
        return monsterRepository.findAll()
    }

    override suspend fun findById(id: String): Monster? {
        return monsterRepository.findById(id)
    }

    override suspend fun save(t: Monster): Monster {
        return monsterRepository.save(t)
    }

    override suspend fun update(t: Monster): Monster {
        return monsterRepository.update(t)
    }

    override suspend fun deleteById(id: String): Boolean {
        return monsterRepository.deleteById(id)
    }
}