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

class EditProfileActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        val newPasswordEditText: EditText = findViewById(R.id.newPasswordEditText)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val savePasswordButton: Button = findViewById(R.id.savePasswordButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)

        // Сохранение нового пароля
        savePasswordButton.setOnClickListener {
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Получаем логин текущего пользователя из SharedPreferences
            val currentUserLogin = preferences.getString("current_user", null)

            if (currentUserLogin != null) {
                // Обновление пароля в файле
                val isPasswordUpdated = updatePasswordInFile(currentUserLogin, newPassword)
                if (isPasswordUpdated) {
                    // Также обновляем пароль в файле current_user.txt
                    updatePasswordInCurrentUserFile(currentUserLogin, newPassword)
                    Toast.makeText(this, "Password successfully changed", Toast.LENGTH_SHORT).show()
                    navigateToProfilePage()
                } else {
                    Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработка кнопки "Back" для возврата на ProfilePageActivity
        cancelButton.setOnClickListener {
            navigateToProfilePage()
        }
    }

    private fun updatePasswordInFile(login: String, newPassword: String): Boolean {
        val file = File(filesDir, "users.txt")
        if (!file.exists()) {
            Toast.makeText(this, "User file not found", Toast.LENGTH_SHORT).show()
            return false
        }

        val lines = file.readLines()
        val updatedLines = mutableListOf<String>()
        var isUpdated = false

        for (line in lines) {
            val parts = line.split(":")
            if (parts.size == 2 && parts[0] == login) {
                updatedLines.add("${parts[0]}:$newPassword")
                isUpdated = true
            } else {
                updatedLines.add(line)
            }
        }

        if (isUpdated) {
            file.writeText(updatedLines.joinToString("\n"))
        }

        return isUpdated
    }

    private fun updatePasswordInCurrentUserFile(login: String, newPassword: String) {
        val file = File(filesDir, "current_user.txt")
        if (file.exists()) {
            file.writeText("$login:$newPassword")  // Перезаписываем новый пароль
        }
    }

    private fun navigateToProfilePage() {
        val intent = Intent(this, ProfilePageActivity::class.java)
        startActivity(intent)
        finish() // Завершаем текущую Activity
    }
}
