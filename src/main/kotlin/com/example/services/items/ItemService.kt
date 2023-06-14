package com.example.services.items

import com.example.repositories.ItemRepository
import com.example.models.Item
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class ItemService(private val itemRepository: ItemRepository) : IItemService {
    override suspend fun findAll(): List<Item> {
        return itemRepository.findAll()
    }

    override suspend fun findById(id: String): Item? {
        return itemRepository.findById(id)
    }

    override suspend fun save(t: Item): Item {
        return itemRepository.save(t)
    }

    override suspend fun update(t: Item): Item {
        return itemRepository.update(t)
    }

    override suspend fun deleteById(id: String): Boolean {
        return itemRepository.deleteById(id)
    }

}