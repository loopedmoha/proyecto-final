package com.example.models

object CurrentSessions {
    val sessions = mutableListOf<String>()
    val usersSessionDice = mutableMapOf<String, Int>()
    val usersSessionChat = mutableMapOf<String, Int>()
}