package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class TestingPageActivity : AppCompatActivity() {

    private lateinit var deckSpinner: Spinner
    private lateinit var startTestButton: Button
    private lateinit var homeButton: Button
    private lateinit var testingButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var profileButton: Button
    private val deckMap = mutableMapOf<String, String>() // Сопоставление идентификатор -> название

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_page)

        // Найдем элементы интерфейса
        deckSpinner = findViewById(R.id.deckSpinner)
        startTestButton = findViewById(R.id.startTestButton)
        homeButton = findViewById(R.id.homeButton)
        testingButton = findViewById(R.id.testingButton)
        statisticsButton = findViewById(R.id.statisticsButton)
        profileButton = findViewById(R.id.profileButton)

        // Загрузка колод
        loadDecks()
        setupSpinner()

        // Обработчик кнопки старта теста
        startTestButton.setOnClickListener {
            val selectedDeckName = deckSpinner.selectedItem?.toString()
            val selectedDeckId = deckMap.entries.find { it.value == selectedDeckName }?.key
            if (selectedDeckId != null && selectedDeckId.isNotEmpty()) {
                val intent = Intent(this, FlashCardsActivity::class.java)
                intent.putExtra("deck_id", selectedDeckId) // Передача идентификатора колоды
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a deck to continue", Toast.LENGTH_SHORT).show()
            }
        }

        // Навигационная панель
        homeButton.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java) // Переход на домашнюю страницу
            startActivity(intent)
        }

        testingButton.setOnClickListener {
            val intent = Intent(this, TestingPageActivity::class.java) // Переход на страницу тестов
            startActivity(intent)
        }

        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java) // Переход на страницу статистики
            startActivity(intent)
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java) // Переход на страницу профиля
            startActivity(intent)
        }
    }

    private fun loadDecks() {
        val decksFile = File(filesDir, "decks.txt")
        if (decksFile.exists()) {
            decksFile.forEachLine { line ->
                if (line.isNotBlank()) {
                    val parts = line.split(":")
                    if (parts.size == 2) {
                        val deckId = parts[0].trim()
                        val deckName = parts[1].trim()
                        deckMap[deckId] = deckName
                    } else {
                        // Лог ошибки
                        Log.e("TestingPageActivity", "Invalid deck format: $line")
                    }
                }
            }
        } else {
            Log.e("TestingPageActivity", "Decks file not found at ${decksFile.absolutePath}")
            Toast.makeText(this, "No decks available", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupSpinner() {
        if (deckMap.isEmpty()) {
            Toast.makeText(this, "No decks to display", Toast.LENGTH_SHORT).show()
            return
        }

        val deckNames = deckMap.values.toList() // Получаем список названий колод
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            deckNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deckSpinner.adapter = adapter

        // Лог для проверки
        Log.d("TestingPageActivity", "Loaded decks: $deckNames")
    }
}
