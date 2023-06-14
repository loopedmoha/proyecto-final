package com.example.repositories

import com.example.db.MongoDbManager
import com.example.models.player.CharacterClass
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
@Single
class ClassRepository : IRepository<CharacterClass, String>{
    override suspend fun findAll(): List<CharacterClass> {
        val collection = MongoDbManager.mongoDatabase.getCollection<CharacterClass>("classes")
        return collection.find().toList()
    }

    override suspend fun save(t: CharacterClass): CharacterClass {
        val collection = MongoDbManager.mongoDatabase.getCollection<CharacterClass>("classes")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: CharacterClass): CharacterClass {
        val collection = MongoDbManager.mongoDatabase.getCollection<CharacterClass>("classes")
        collection.updateOne(CharacterClass::name eq t.name, t)
        return t
    }

    override suspend fun deleteById(id: String): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<CharacterClass>("classes")
        val res = collection.deleteOne(CharacterClass::name eq id)
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: String): CharacterClass? {
        val collection = MongoDbManager.mongoDatabase.getCollection<CharacterClass>("classes")
        return collection.findOne(CharacterClass::name eq id)
    }

}