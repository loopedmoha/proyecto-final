package com.example.services.misc

import com.example.models.Encounter
import com.example.services.IServices

interface IEncounterService : IServices<Encounter, String> {
    suspend fun findByCreator(creator : String) : List<Encounter>
}