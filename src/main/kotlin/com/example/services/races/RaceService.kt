package com.example.services.races

import com.example.models.player.Race
import com.example.repositories.ItemRepository
import com.example.repositories.RaceRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class RaceService(  private val raceRepository: RaceRepository) : IRaceService {
    override suspend fun findAll(): List<Race> {
        return raceRepository.findAll()

    }

    override suspend fun findById(id: String): Race? {
        return raceRepository.findById(id)
    }

    suspend fun findByName(name : String) : Race?{
        return raceRepository.findByName(name)
    }
    override suspend fun save(t: Race): Race {
        return raceRepository.save(t)
    }

    override suspend fun update(t: Race): Race {
        return raceRepository.update(t)
    }

    override suspend fun deleteById(id: String): Boolean {
        return raceRepository.deleteById(id)
    }
}