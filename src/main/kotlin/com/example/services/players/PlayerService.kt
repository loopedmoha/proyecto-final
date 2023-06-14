package com.example.services.players

import com.example.models.player.Player
import com.example.repositories.ItemRepository
import com.example.repositories.PlayerRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*
@Single
class PlayerService(  private val playerRepository: PlayerRepository) : IPlayerService {
    override suspend fun findAll(): List<Player> {
        return playerRepository.findAll()
    }

    override suspend fun findById(id: UUID): Player? {
        return playerRepository.findById(id)
    }

    suspend fun findByCreator(creator : String) : List<Player>{
        return playerRepository.findByCreator(creator);
    }

    override suspend fun save(t: Player): Player {
        return playerRepository.save(t)

    }

    override suspend fun update(t: Player): Player {
        return playerRepository.update(t)
    }

    override suspend fun deleteById(id: UUID): Boolean {
        return playerRepository.deleteById(id)
    }
}