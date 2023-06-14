package com.example.services.misc

import com.example.models.Encounter
import com.example.repositories.EncounterRepository
import org.koin.core.annotation.Single

@Single
class EncounterService(private val encounterRepository: EncounterRepository) : IEncounterService {
    override suspend fun findByCreator(creator: String): List<Encounter> {
        return encounterRepository.findByCreator(creator)
    }

    override suspend fun findAll(): List<Encounter> {
        return encounterRepository.findAll()
    }

    override suspend fun findById(id: String): Encounter? {
        return encounterRepository.findById(id)
    }

    override suspend fun save(t: Encounter): Encounter {
        return encounterRepository.save(t)

    }

    override suspend fun update(t: Encounter): Encounter {
        return encounterRepository.update(t)
    }

    override suspend fun deleteById(id: String): Boolean {
        return encounterRepository.deleteById(id)
    }
}