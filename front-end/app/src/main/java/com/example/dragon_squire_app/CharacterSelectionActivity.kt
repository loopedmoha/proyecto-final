package com.example.dragon_squire_app

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragon_squire_app.character.CharacterListAdapter
import com.example.dragon_squire_app.character.CharacterListListener
import com.example.dragon_squire_app.character.CharacterListViewModel
import com.example.dragon_squire_app.databinding.ActivityCharacterSelectionBinding
import com.example.dragon_squire_app.databinding.AddCharacterLayoutBinding
import com.example.dragon_squire_app.databinding.FragmentJoinSessionBinding
import com.example.dragon_squire_app.models.LoggedUser
import com.example.dragon_squire_app.models.dto.CharacterCreationDto
import com.example.dragon_squire_app.models.player.Player
import com.example.dragon_squire_app.utils.IApiService
import com.example.dragon_squire_app.utils.SessionData
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CharacterSelectionActivity : AppCompatActivity(), CharacterListListener {
    private lateinit var binding: ActivityCharacterSelectionBinding

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    private val apiService = IApiService.create()
    lateinit var loggedUser: LoggedUser
    private val client = IApiService.create()
    val charactersViewModel = CharacterListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        val cosa = intent.getSerializableExtra("loggedUser").toString()
        Log.d("CharacterSelection", cosa)
        loggedUser = json.decodeFromString(cosa)
        //loggedUser = json.decodeFromString(intent.getStringExtra("loggedUser"))
        getCharacters()
        binding = ActivityCharacterSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("CharacterSelection", "Character selection activity created")

        binding.buttonFloatAddCharacter.setOnClickListener {
            Log.d("CharacterSelection", "Add character button pressed")
            showCharacterCreationDialog()
        }
        val chList = getCharacters()
        val characterAdapter =
            CharacterListAdapter(charactersViewModel.characters.value ?: mutableListOf(), this)
        binding.selectCharacterActivityRecycler.layoutManager = LinearLayoutManager(this)
        binding.selectCharacterActivityRecycler.adapter = characterAdapter

        charactersViewModel.characters.observe(this) {
            Log.d("CharacterSelection", "Characters: $it")
            characterAdapter.clear()
            it.forEach { character ->
                Log.d("CharacterSelection", "Character: $character")
                characterAdapter.add(character)
            }
        }
    }

    private fun getCharacters() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = apiService.getCharacter(loggedUser.username)
            if (response != "ERROR") {
                val characters = json.decodeFromString<MutableList<Player>>(response)
                Log.d("CharacterSelection", "Characters: $response")
                withContext(Dispatchers.Main) {
                    charactersViewModel.characters.value = characters
                }
            } else {
                Log.d("CharacterSelection", "Error getting characters")
            }
        }
    }

    private fun showCharacterCreationDialog() {
        val dialog = Dialog(this)
        val addBinding = AddCharacterLayoutBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        var spinnerAdapter = setSpinnerRace()
        addBinding.spinnerAddRace.adapter = spinnerAdapter

        spinnerAdapter = setSpinnerClass()
        addBinding.spinnerAddClass.adapter = spinnerAdapter
        dialog.setContentView(addBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.white)
        addBinding.buttonAddCancel.setOnClickListener {
            dialog.dismiss()
        }
        addBinding.buttonAddConfirm.setOnClickListener {
            val name = addBinding.editTextAddName.text.toString()
            val strength = addBinding.editTextAddFuerza.text.toString()
            val dexterity = addBinding.editTextAddDestreza.text.toString()
            val constitution = addBinding.editTextAddConstitucion.text.toString()
            val intelligence = addBinding.editTextAddInteligencia.text.toString()
            val wisdom = addBinding.editTextAddSabiduria.text.toString()
            val charisma = addBinding.editTextAddCarisma.text.toString()
            val race = addBinding.spinnerAddRace.selectedItem.toString()
            val clase = addBinding.spinnerAddClass.selectedItem.toString()

            if (name.isNotEmpty()
                && strength.isNotEmpty()
                && dexterity.isNotEmpty()
                && constitution.isNotEmpty()
                && intelligence.isNotEmpty()
                && wisdom.isNotEmpty()
                && charisma.isNotEmpty()
                && race.isNotEmpty()
                && clase.isNotEmpty()
            ) {
                val characterCreationDto = CharacterCreationDto(
                    loggedUser.username,
                    name,
                    race,
                    clase,
                    strength.toInt(),
                    dexterity.toInt(),
                    constitution.toInt(),
                    intelligence.toInt(),
                    wisdom.toInt(),
                    charisma.toInt()
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    val res = withContext(Dispatchers.IO) {
                        val res = apiService.createCharacter(characterCreationDto)
                        //Log.d("CharacterSelection", "Character creation response: $res")
                        val a = json.decodeFromString<Player>(res)
                        val characters = charactersViewModel.characters.value
                        charactersViewModel.characters.postValue(characters?.apply { add(a) })
                        Log.d("CharacterSelection", "Character creation response: $a")
                       // val b = json.decodeFromString<CharacterCreationDto>(res)
                        //charactersViewModel.characters.postValue(json.decodeFromString(res))

                    }
                }
                Log.d("CharacterSelection", "Character creation dto: $characterCreationDto")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun setSpinnerRace(): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            this, R.array.race_array, android.R.layout.simple_spinner_item
        )
    }

    private fun setSpinnerClass(): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            this, R.array.class_array, android.R.layout.simple_spinner_item
        )
    }

    override fun onCharacterClicked(player: Player) {
        Log.d("CharacterSelection", "Character clicked: $player")
        val dialogBinding = FragmentJoinSessionBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(this@CharacterSelectionActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_join_session, null)
        dialogBuilder.setView(dialogView)

        val editTextSessionName = dialogView.findViewById<EditText>(R.id.edit_text_session_name)
        val textJoin = dialogView.findViewById<TextView>(R.id.text_join_session_confirmation)

        textJoin.text = "¿A qué sesión quieres unirte con '${player.name}'?"
        dialogBuilder.setPositiveButton("Unirse") { _, _ ->
            val sessionName = editTextSessionName.text.toString()
            var response: Boolean = false
            if (sessionName.isNotEmpty()) {
                val res = lifecycleScope.async(Dispatchers.IO) {
                    response = apiService.checkSession(sessionName)
                    //Log.d("CharacterSelection", "Session join response: $response")

                    if (response) {
                        SessionData.username = player.name
                        //Toast.makeText(this@CharacterSelectionActivity, "Unido a la sesión", Toast.LENGTH_SHORT).show()
                    }
                }
                runBlocking {
                    res.await()
                    Log.d("CharacterSelection", "Session join response: $response")
                }
                if (!response) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        client.createNamedSession(sessionName)
                    }
                    Toast.makeText(
                        this@CharacterSelectionActivity,
                        "Sesión creada con éxito",
                        Toast.LENGTH_SHORT
                    ).show()

                    SessionData.sessionName.value = sessionName
                    val intent = Intent(this@CharacterSelectionActivity, MainActivity::class.java)
                    intent.putExtra("player", json.encodeToString(player))
                    //it.putExtra("loggedUser", json.encodeToString(token))
                    Log.d("CharacterSelection", "Session name: ${SessionData.sessionName.value}")
                    startActivity(intent)
                } else {
                    SessionData.sessionName.value = sessionName
                    val intent = Intent(this@CharacterSelectionActivity, MainActivity::class.java)
                    intent.putExtra("player", json.encodeToString(player))
                    //it.putExtra("loggedUser", json.encodeToString(token))
                    Log.d("CharacterSelection", "Session name: ${SessionData.sessionName.value}")
                    //SessionData.player = player
                    startActivity(intent)
                }
            } else {
                Toast.makeText(
                    this@CharacterSelectionActivity,
                    "Introduce un nombre de sesión",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        dialogBuilder.setNegativeButton("Cancelar") { _, _ ->

        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

}


