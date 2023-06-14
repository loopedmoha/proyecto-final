package com.example.services.players

import com.example.models.player.Player
import com.example.services.IServices
import java.util.UUID

interface IPlayerService : IServices<Player, UUID> {
}