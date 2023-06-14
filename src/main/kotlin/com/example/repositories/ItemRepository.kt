package com.example.repositories

import com.example.db.MongoDbManager
import com.example.models.Item
import org.koin.core.annotation.Single
import org.litote.kmongo.eq

@Single
class ItemRepository : IRepository<Item, String>   {

    override suspend fun findAll(): List<Item> {
        val collection = MongoDbManager.mongoDatabase.getCollection<Item>("items")
        return collection.find().toList()
    }

    override suspend fun save(t: Item): Item {
        val collection = MongoDbManager.mongoDatabase.getCollection<Item>("items")
        collection.insertOne(t)
        return t
    }

    override suspend fun update(t: Item): Item {
        val collection = MongoDbManager.mongoDatabase.getCollection<Item>("items")
        collection.updateOne(Item::uuid eq t.uuid, t)
        return t
    }

    override suspend fun deleteById(id: String): Boolean {
        val collection = MongoDbManager.mongoDatabase.getCollection<Item>("items")
        val res = collection.deleteOne(Item::uuid eq id)
        return res.wasAcknowledged()
    }

    override suspend fun findById(id: String): Item? {
        val collection = MongoDbManager.mongoDatabase.getCollection<Item>("items")
        return collection.findOne(Item::uuid eq id)
    }

}