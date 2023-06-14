package com.example.repositories

import com.example.db.MongoDbManager
import com.example.models.player.Race
import org.litote.kmongo.eq
import org.koin.core.annotation.Single

@Single

class RaceRepository : IRepository<Race, String> {
    override suspend fun findAll(): List<Race> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Race>("races")
        return collection.find().toList()
    }

    suspend fun findByName(name : String) : Race?{
        val collection = MongoDbManager.mongoDatabase.getCollection<Race>("races")
        return collection.findOne(Race::name eq name)
    }
    override suspend fun save(t: Race): Race {
        val collection = MongoDbManager.mongoDatabase.getCollection<Race>("races")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: Race): Race {
        val collection = MongoDbManager.mongoDatabase.getCollection<Race>("races")
        collection.updateOne(Race::name eq t.name, t)
        return t
    }

    override suspend fun deleteById(id: String): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<Race>("races")
        val res = collection.deleteOne(Race::name eq id)
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: String): Race? {
        val collection = MongoDbManager.mongoDatabase.getCollection<Race>("races")
        return collection.findOne(Race::name eq id)
    }


}