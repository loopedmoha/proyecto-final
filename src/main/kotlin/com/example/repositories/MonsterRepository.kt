package com.example.repositories

import com.example.db.MongoDbManager
import com.example.models.Monster
import org.litote.kmongo.eq

import org.koin.core.annotation.Single

@Single

class MonsterRepository : IRepository<Monster, String> {

    override suspend fun findAll(): List<Monster> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Monster>("monsters")
        return collection.find().toList()
    }

    override suspend fun save(t: Monster): Monster {
        val collection = MongoDbManager.mongoDatabase.getCollection<Monster>("monsters")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: Monster): Monster {
        val collection = MongoDbManager.mongoDatabase.getCollection<Monster>("monsters")
        collection.updateOne(Monster::name eq t.name, t)
        return t
    }

    override suspend fun deleteById(id: String): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<Monster>("monsters")
        val res = collection.deleteOne(Monster::name eq id)
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: String): Monster? {
        val collection = MongoDbManager.mongoDatabase.getCollection<Monster>("monsters")
        return collection.findOne(Monster::name eq id)
    }
}