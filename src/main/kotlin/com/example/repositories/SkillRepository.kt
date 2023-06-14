package com.example.repositories

import com.example.db.MongoDbManager
import com.example.models.Skill
import org.litote.kmongo.eq
import org.koin.core.annotation.Single

@Single

class SkillRepository : IRepository<Skill, String> {
    override suspend fun findAll(): List<Skill> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Skill>("skills")
        return collection.find().toList()
    }

    override suspend fun save(t: Skill): Skill {
        val collection = MongoDbManager.mongoDatabase.getCollection<Skill>("skills")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: Skill): Skill {
        val collection = MongoDbManager.mongoDatabase.getCollection<Skill>("skills")
        collection.updateOne(Skill::name eq t.name, t)
        return t
    }

    override suspend fun deleteById(id: String): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<Skill>("skills")
        val res = collection.deleteOne(Skill::name eq id)
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: String): Skill? {
        val collection = MongoDbManager.mongoDatabase.getCollection<Skill>("skills")
        return collection.findOne(Skill::name eq id)
    }
}