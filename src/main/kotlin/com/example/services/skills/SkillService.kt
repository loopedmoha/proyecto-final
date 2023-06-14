package com.example.services.skills

import com.example.repositories.SkillRepository
import com.example.models.Skill
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class SkillService(private val skillRepository: SkillRepository) : ISkillService {
    override suspend fun findAll(): List<Skill> {
        return skillRepository.findAll()
    }

    override suspend fun findById(id: String): Skill? {
        return skillRepository.findById(id)
    }

    override suspend fun save(item: Skill): Skill {
        return skillRepository.save(item)
    }

    override suspend fun update(item: Skill): Skill {
        return skillRepository.update(item)
    }

    override suspend fun deleteById(id: String): Boolean {
        return skillRepository.deleteById(id)
    }
}