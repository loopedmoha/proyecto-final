package com.example.dragon_squire_app.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dragon_squire_app.models.player.Player

class CharacterListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    val characters = MutableLiveData<MutableList<Player>>().apply { value = mutableListOf() }
    val selectedCharacter = MutableLiveData<Player>()
    val newCharacter = MutableLiveData<Player>()
}