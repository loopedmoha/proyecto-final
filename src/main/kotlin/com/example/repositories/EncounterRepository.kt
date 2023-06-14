package com.example.repositories

import com.example.db.MongoDbManager
import com.example.models.Encounter
import org.koin.core.annotation.Single
import org.litote.kmongo.eq


@Single
class EncounterRepository : IRepository<Encounter, String> {
    override suspend fun findAll(): List<Encounter> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Encounter>("encounters")
        return collection.find().toList()
    }

    override suspend fun save(t: Encounter): Encounter {
        val collection = MongoDbManager.mongoDatabase.getCollection<Encounter>("encounters")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: Encounter): Encounter {
        val collection = MongoDbManager.mongoDatabase.getCollection<Encounter>("encounters")
        collection.updateOne(Encounter::uuid eq t.uuid, t)
        return t
    }

    override suspend fun deleteById(id: String): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<Encounter>("encounters")
        val res = collection.deleteOne(Encounter::uuid eq id)
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: String): Encounter? {
        val collection = MongoDbManager.mongoDatabase.getCollection<Encounter>("encounters")
        return collection.findOne(Encounter::uuid eq id)
    }

    suspend fun findByCreator(creator: String): List<Encounter> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Encounter>("encounters")
        return collection.find(Encounter::creator eq creator).toList()
    }

}