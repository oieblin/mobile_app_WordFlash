package com.example.myapplication.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var loginField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginField = findViewById(R.id.Login)
        passwordField = findViewById(R.id.Password)
        loginButton = findViewById(R.id.button)
        registerButton = findViewById(R.id.button2)

        loginButton.setOnClickListener { handleLogin() }
        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {
        val enteredLogin = loginField.text.toString().trim()
        val enteredPassword = passwordField.text.toString().trim()

        if (isUserRegistered(enteredLogin, enteredPassword)) {
            saveLoginToFile(enteredLogin, enteredPassword)  // Сохраняем логин и пароль в файл
            saveLoginToPreferences(enteredLogin)  // Сохраняем логин в SharedPreferences
            navigateToHome()
        } else {
            Toast.makeText(this, "Invalid login or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isUserRegistered(login: String, password: String): Boolean {
        val file = File(filesDir, "users.txt")

        if (!file.exists() || file.length() == 0L) {
            return false
        }
        file.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(":")
                if (parts.size == 2) {
                    val storedLogin = parts[0].trim()
                    val storedPassword = parts[1].trim()
                    if (storedLogin == login && storedPassword == password) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun saveLoginToPreferences(login: String) {
        val preferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("current_user", login)  // Сохраняем логин
        editor.apply()
    }

    private fun saveLoginToFile(login: String, password: String) {
        val file = File(filesDir, "current_user.txt")
        file.writeText("$login:$password")
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }
}
