package com.example.myapplication.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class ProfilePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val userLoginTextView: TextView = findViewById(R.id.userLoginTextView)
        val changePasswordButton: Button = findViewById(R.id.changePasswordButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val homeButton: Button = findViewById(R.id.homeButton)
        val testingButton: Button = findViewById(R.id.testingButton)
        val statisticsButton: Button = findViewById(R.id.statisticsButton)

        // Считываем логин из файла current_user.txt
        val userLogin = readLoginFromFile()
        if (userLogin != null) {
            userLoginTextView.text = "Username: $userLogin"
        } else {
            userLoginTextView.text = "Username: Not logged in"
        }

        // Переход к EditPasswordActivity
        changePasswordButton.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        // Выход из приложения
        logoutButton.setOnClickListener {
            clearCurrentUserFile()
            val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.remove("current_user")
            editor.apply()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            navigateToLoginPage()
        }

        // Переходы на другие страницы
        homeButton.setOnClickListener {
            startActivity(Intent(this, HomepageActivity::class.java))
        }

        testingButton.setOnClickListener {
            startActivity(Intent(this, TestingPageActivity::class.java))
        }

        statisticsButton.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
    }

    private fun readLoginFromFile(): String? {
        val file = File(filesDir, "current_user.txt")
        return if (file.exists()) {
            file.readText().split(":")[0].trim()
        } else {
            null
        }
    }

    private fun clearCurrentUserFile() {
        val file = File(filesDir, "current_user.txt")
        if (file.exists()) {
            file.writeText("")  // Очищаем файл
        }
    }

    private fun navigateToLoginPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
