package com.example.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.example.db.MongoDbManager
import com.example.models.Item
import com.example.models.Monster
import com.example.models.Skill
import com.example.models.player.CharacterClass
import com.example.models.player.Race

/**
 * Data loader se encargara de cargar los datos de los iniciales de la aplicacion en caso de que no existan.
 *
 * @constructor Create empty Data loader
 */
object DataLoader {
    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend fun loadItems() {
        val itemsJson = this::class.java.classLoader.getResourceAsStream("initialItems.json")
        itemsJson?.let {
            val readed = it.readBytes().toString(Charsets.UTF_8)
            val items = json.decodeFromString<List<Item>>(readed)
            items.forEach { item ->
                println("Loading item: $item")
            }
            val db = MongoDbManager.mongoDatabase
            db.getCollection<Item>("items").insertMany(items)
        }
    }

    suspend fun loadSkills() {
        val itemsJson = this::class.java.classLoader.getResourceAsStream("initialSkills.json")
        itemsJson?.let {
            val readed = it.readBytes().toString(Charsets.UTF_8)
            val items = json.decodeFromString<List<Skill>>(readed)
            items.forEach { item ->
                println("Loading skill: $item")
            }
            val db = MongoDbManager.mongoDatabase
            db.getCollection<Skill>("skills").insertMany(items)
        }
    }

    suspend fun loadRaces() {
        val itemsJson = this::class.java.classLoader.getResourceAsStream("initialRaces.json")
        itemsJson?.let {
            val readed = it.readBytes().toString(Charsets.UTF_8)
            val items = json.decodeFromString<List<Race>>(readed)
            items.forEach { item ->
                println("Loading skill: $item")
            }
            val db = MongoDbManager.mongoDatabase
            db.getCollection<Race>("races").insertMany(items)
        }
    }

    suspend fun loadClasses() {
        val itemsJson = this::class.java.classLoader.getResourceAsStream("initialClasses.json")
        itemsJson?.let {
            val readed = it.readBytes().toString(Charsets.UTF_8)
            val items = json.decodeFromString<List<CharacterClass>>(readed)
            items.forEach { item ->
                println("Loading skill: $item")
            }
            val db = MongoDbManager.mongoDatabase
            db.getCollection<CharacterClass>("classes").insertMany(items)
        }
    }

    suspend fun loadMonsters() {
        val itemsJson = this::class.java.classLoader.getResourceAsStream("initialMonsters.json")
        itemsJson?.let {
            val readed = it.readBytes().toString(Charsets.UTF_8)
            val items = json.decodeFromString<List<Monster>>(readed)
            items.forEach { item ->
                println("Loading skill: $item")
            }
            val db = MongoDbManager.mongoDatabase
            db.getCollection<Monster>("monsters").insertMany(items)
        }
    }
}