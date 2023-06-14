package com.example.services.misc

import com.example.models.Npc
import com.example.repositories.NpcRepository
import com.example.services.IServices
import org.koin.core.annotation.Single

@Single
class NpcService(private val npcRepository: NpcRepository) : INpcService {
    override suspend fun findAll(): List<Npc> {
        return npcRepository.findAll()
    }

    override suspend fun findById(id: String): Npc? {
        return npcRepository.findById(id)
    }

    override suspend fun save(t: Npc): Npc {
        return npcRepository.save(t)
    }

    override suspend fun update(t: Npc): Npc {
        return npcRepository.update(t)
    }

    override suspend fun deleteById(id: String): Boolean {
        return npcRepository.deleteById(id)
    }

    suspend fun findByCreator(creator : String) : List<Npc>{
        return npcRepository.findByCreator(creator)
    }
}