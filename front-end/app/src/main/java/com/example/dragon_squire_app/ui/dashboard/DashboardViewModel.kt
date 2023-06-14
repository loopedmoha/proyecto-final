package com.example.dragon_squire_app.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dragon_squire_app.models.player.Player
import com.example.dragon_squire_app.models.stats.Attributes

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }

    val attributes =
        MutableLiveData<MutableList<Attributes>>().apply {
            value = mutableListOf()
        }
    val text: LiveData<String> = _text
}