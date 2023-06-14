package com.example.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordEncrypter {
    fun encrypt(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }

    fun checkPassword(password: String, secretPassword: String): Boolean {
       return BCrypt.checkpw(password, secretPassword)
    }
}