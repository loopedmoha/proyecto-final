package com.example.dragon_squire_app

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dragon_squire_app.databinding.ActivityLoginBinding
import com.example.dragon_squire_app.models.LoggedUser
import com.example.dragon_squire_app.models.Token
import com.example.dragon_squire_app.utils.DSProperties
import com.example.dragon_squire_app.utils.IApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val client = IApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DSProperties.getAdress(this@LoginActivity)
        Log.d("LOGIN", "COSA: ${DSProperties.getAdress(this@LoginActivity)}")
        Log.d("LOGIN", "COSA: ${DSProperties.getProperty("dev", this@LoginActivity)}")

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isClickable = false
            val username = binding.textLoginUsername.text.toString()
            val password = binding.textLoginPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                /* HACER AQUÍ LLAMADA A LA API */
                lifecycleScope.launch(Dispatchers.IO) {
                    val res = withContext(lifecycleScope.coroutineContext) {
                        var res = client.login(username, password)
                        if (res == "BAD_REQUEST") {
                            Log.d("LOGIN", "BAD_REQUEST")
                            showToast("Usuario o contraseña incorrecto")
                        } else if (res == "NOT_FOUND") {
                            Log.d("LOGIN", "NOT_FOUND")
                            showToast("Usuario no encontrado")
                            if (registerUser()) {
                                res = client.login(username, password)
                                goToMain(username, res)
                            }
                        } else if (res == "ERROR") {
                            Log.d("LOGIN", "ERROR DE LOGEO")
                            showToast("Error de conexión")
                        } else {
                            goToMain(username, res)
                        }
                        res
                    }
                    Log.d("LOGIN2", res.toString())
                }
            } else {
                binding.btnLogin.isClickable = true
                showToast("Rellena todos los campos")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(): Boolean {
        var registered = false
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Register")
        alertDialog.setMessage("El usuario no existe, ¿registrarlo?")
        alertDialog.setPositiveButton("Si") { dialog, which ->
            lifecycleScope.launch(Dispatchers.IO) {
                val res = withContext(lifecycleScope.coroutineContext) {
                    client.register(
                        binding.textLoginUsername.text.toString(),
                        binding.textLoginPassword.text.toString()
                    )
                }
                if (res == "ERROR") {
                    Log.d("LOGIN", "ERROR DE REGISTRO")
                    showToast("Error de conexión")
                } else {
                    Log.d("LOGIN", "REGISTRO CORRECTO")
                    goToMain(binding.textLoginUsername.text.toString(), res)
                    registered = true
                }
            }
        }
        alertDialog.setNegativeButton("No") { _, _ ->
            showToast("No se ha registrado")
        }
        alertDialog.show()
        return registered
    }

    private fun goToMain(username: String, token: String) {
        val intent = Intent(this@LoginActivity, CharacterSelectionActivity::class.java).apply {
            val tokenObj = LoggedUser(username, Json.decodeFromString<Token>(token).token)
            putExtra("loggedUser", Json.encodeToString(tokenObj))
        }
        startActivity(intent)
    }
}
