package com.example.dragon_squire_app

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dragon_squire_app.chat.WebSocketListenerChat
import com.example.dragon_squire_app.chat.WebSocketListenerDice
import com.example.dragon_squire_app.databinding.ActivityMainBinding
import com.example.dragon_squire_app.databinding.FragmentDicepoolBinding
import com.example.dragon_squire_app.models.DicePool
import com.example.dragon_squire_app.models.dto.DiceDTO
import com.example.dragon_squire_app.models.player.Player
import com.example.dragon_squire_app.models.toDto
import com.example.dragon_squire_app.utils.DSProperties
import com.example.dragon_squire_app.utils.IApiService
import com.example.dragon_squire_app.utils.SessionData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import androidx.navigation.ui.setupWithNavController


class MainActivity : AppCompatActivity() {

    val url = DSProperties.activeUrl
    private lateinit var binding: ActivityMainBinding
    val json = Json { ignoreUnknownKeys = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val client = IApiService.create()
        val playerString = intent.getStringExtra("player")
        val player = json.decodeFromString<Player>(playerString!!)
        SessionData.player = player
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d("MainActivity", "Player: $player")
        Log.d("MainActivity", "Player: $player")
        Log.d("MainActivity", "SessionData: ${SessionData.sessionName.value}")

        SessionData.username = player.name



        binding.floatingActionButton.setOnClickListener {
            throwDicePool()
        }



        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        connectToSession()

        Log.d("MainActivity", "Main activity created")
    }

    private fun connectToSession() {
        val sessionName = SessionData.sessionName.value
        val client = OkHttpClient()
        val user = SessionData.username

        val requestChat = Request.Builder()
            .url("ws://$url/chat/room/$sessionName/$user").build()
        Log.d("MainActivity", "Connecting to session: $sessionName")


        val requestDice = Request.Builder()
            .url("ws://$url/dice/$sessionName/$user").build()

        val listener = WebSocketListenerChat()
        val listenerD = WebSocketListenerDice(this)
        val wsChat: WebSocket = client.newWebSocket(requestChat, listener)
        val wsDice: WebSocket = client.newWebSocket(requestDice, listenerD)

        SessionData.wsChat = wsChat
        SessionData.wsDice = wsDice
    }

    private fun throwDicePool() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.fragment_dicepool)
        dialog.show()
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val dicePool = DicePool(0, 0, 0, 0, 0, 0, 0)

        val d4Img = dialog.findViewById(R.id.d4_image) as ImageView
        var d4value = 0
        val d4text = dialog.findViewById(R.id.text_dicepool_d4) as TextView
        d4text.text = d4value.toString()
        d4Img.setOnClickListener {
            dicePool.addD4()
            d4value++
            d4text.text = d4value.toString()
            Log.d("MainActivity", "D4 clicked")
        }
        val d6Img = dialog.findViewById(R.id.d6_image) as ImageView
        var d6value = 0
        val d6text = dialog.findViewById(R.id.text_dicepool_d6) as TextView
        d6text.text = d6value.toString()
        d6Img.setOnClickListener {
            dicePool.addD6()
            d6value++
            d6text.text = d6value.toString()
            Log.d("MainActivity", "D6 clicked")
        }

        val d8Img = dialog.findViewById(R.id.d8_image) as ImageView
        var d8value = 0
        val d8text = dialog.findViewById(R.id.text_dicepool_d8) as TextView
        d8text.text = d8value.toString()
        d8Img.setOnClickListener {
            dicePool.addD8()

            d8value++
            d8text.text = d8value.toString()
            Log.d("MainActivity", "D8 clicked")
        }

        val d10Img = dialog.findViewById(R.id.d10_image) as ImageView
        var d10value = 0
        val d10text = dialog.findViewById(R.id.text_dicepool_d10) as TextView
        d10text.text = d10value.toString()
        d10Img.setOnClickListener {
            dicePool.addD10()
            d10value++
            d10text.text = d10value.toString()
            Log.d("MainActivity", "D10 clicked")
        }

        val d12Img = dialog.findViewById(R.id.d12_image) as ImageView
        var d12value = 0
        val d12text = dialog.findViewById(R.id.text_dicepool_d12) as TextView
        d12text.text = d12value.toString()
        d12Img.setOnClickListener {
            dicePool.addD12()
            d12value++
            d12text.text = d12value.toString()
            Log.d("MainActivity", "D12 clicked")
        }

        val d20Img = dialog.findViewById(R.id.d20_image) as ImageView
        var d20value = 0
        val d20text = dialog.findViewById(R.id.text_dicepool_d20) as TextView
        d20text.text = d20value.toString()
        d20Img.setOnClickListener {
            dicePool.addD20()
            d20value++
            d20text.text = d20value.toString()
            Log.d("MainActivity", "D20 clicked")
        }

        val d100Img = dialog.findViewById(R.id.d100_image) as ImageView
        var d100value = 0
        val d100text = dialog.findViewById(R.id.text_dicepool_d100) as TextView
        d100text.text = d100value.toString()
        d100Img.setOnClickListener {
            dicePool.addD100()
            d100value++
            d100text.text = d100value.toString()
            Log.d("MainActivity", "D100 clicked")
        }

        Log.d("MainActivity", "Dicepool: $dicePool")
        val throwButton = dialog.findViewById(R.id.button_throw_dice) as Button
        throwButton.setOnClickListener {
            Log.d("MainActivity", "Throw button clicked: ${dicePool.throwDicePool()}")

            val dto = dicePool.toDto(SessionData.username)
            SessionData.wsDice.send("${dicePool.d4};${dicePool.d6};${dicePool.d8};${dicePool.d10};${dicePool.d12};${dicePool.d20};${dicePool.d100};1")
            dialog.dismiss()
        }
    }
}