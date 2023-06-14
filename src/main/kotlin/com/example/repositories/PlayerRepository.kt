package com.example.repositories

import com.example.models.player.Player
import com.example.db.MongoDbManager
import org.litote.kmongo.eq
import java.util.UUID
import org.koin.core.annotation.Single

@Single

class PlayerRepository : IRepository<Player, UUID> {
    override suspend fun findAll(): List<Player> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Player>("players")
        return collection.find().toList()
    }

    override suspend fun save(t: Player): Player {
        val collection = MongoDbManager.mongoDatabase.getCollection<Player>("players")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: Player): Player {
        val collection = MongoDbManager.mongoDatabase.getCollection<Player>("players")
        collection.updateOne(Player::uuid eq t.uuid, t)
        return t
    }

    override suspend fun deleteById(id: UUID): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<Player>("players")
        val res = collection.deleteOne(Player::uuid eq id.toString())
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: UUID): Player? {
        val collection = MongoDbManager.mongoDatabase.getCollection<Player>("players")
        return collection.findOne(Player::uuid eq id.toString())
    }

    suspend fun findByCreator(creator: String): List<Player> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Player>("players")
        return collection.find(Player::creator eq creator).toList()
    }
}