package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File
import java.io.IOException

class RegistrationActivity : AppCompatActivity() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEditText = findViewById(R.id.loginField)
        passwordEditText = findViewById(R.id.passwordField)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val login = loginEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    if (isUserExists(login)) {
                        Toast.makeText(this, "User with this login already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        registerUser(login, password)
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this, "Error accessing the user data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun isUserExists(login: String): Boolean {
        val file = File(filesDir, "users.txt")
        return if (file.exists()) {
            file.useLines { lines ->
                lines.any { line ->
                    val parts = line.split(":")
                    parts.size == 2 && parts[0] == login
                }
            }
        } else {
            false
        }
    }

    @Throws(IOException::class)
    private fun registerUser(login: String, password: String) {
        val file = File(filesDir, "users.txt")
        if (!file.exists()) {
            file.createNewFile() // Создаем файл, если он не существует
        }
        // Добавляем нового пользователя с новой строки
        file.appendText("$login:$password\n")
    }
}
