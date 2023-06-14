package com.example.repositories

import com.example.db.MongoDbManager
import com.example.models.Npc
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.UUID

@Single
class NpcRepository : IRepository<Npc, String> {
    override suspend fun findAll(): List<Npc> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Npc>("npc")
        return collection.find().toList()
    }

    override suspend fun save(t: Npc): Npc {
        val collection = MongoDbManager.mongoDatabase.getCollection<Npc>("npc")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: Npc): Npc {
        val collection = MongoDbManager.mongoDatabase.getCollection<Npc>("npc")
        collection.updateOne(Npc::name eq t.name, t)
        return t
    }

    override suspend fun deleteById(id: String): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<Npc>("npc")
        val res = collection.deleteOne(Npc::name eq id)
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: String): Npc? {
        val collection = MongoDbManager.mongoDatabase.getCollection<Npc>("npc")
        return collection.findOne(Npc::name eq id)
    }

    suspend fun findByCreator(creator: String): List<Npc> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Npc>("npc")
        return collection.find(Npc::creador eq creator).toList()
    }
}