package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.models.Deck
import java.io.File

class AddDeckActivity : AppCompatActivity() {

    private lateinit var deckNameEditText: EditText
    private lateinit var createDeckButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_deck)

        // Инициализация компонентов UI
        deckNameEditText = findViewById(R.id.deckNameEditText)
        createDeckButton = findViewById(R.id.createDeckButton)
        cancelButton = findViewById(R.id.cancelButton)

        // Запрашиваем фокус на поле ввода
        deckNameEditText.requestFocus()

        // Логика кнопки создания
        createDeckButton.setOnClickListener {
            val deckName = deckNameEditText.text.toString().trim()

            // Проверка на пустое имя колоды
            if (deckName.isEmpty()) {
                Toast.makeText(this, "Имя колоды не может быть пустым!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Проверка на существование колоды с таким же названием
            if (isDeckExists(deckName)) {
                Toast.makeText(this, "Колода с таким именем уже существует!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Создание и сохранение колоды
            createAndSaveDeck(deckName)
        }

        // Логика кнопки отмены
        cancelButton.setOnClickListener {
            finish() // Закрытие текущего окна
        }
    }

    // Проверка на существование колоды с данным именем
    private fun isDeckExists(deckName: String): Boolean {
        val file = File(filesDir, "decks.txt")
        return file.exists() && file.readLines().any { line ->
            val parts = line.split(":")
            parts.size == 2 && parts[1] == deckName
        }
    }

    // Создание новой колоды и возврат в HomepageActivity
    private fun createAndSaveDeck(deckName: String) {
        val deck = Deck().apply {
            setDeckId(System.currentTimeMillis().toString()) // Уникальный ID для колоды
            setName(deckName)
        }

        saveDeckToFile(deck)

        // Возвращение в HomepageActivity с успешным результатом
        val resultIntent = Intent()
        resultIntent.putExtra("newDeckAdded", true)
        setResult(RESULT_OK, resultIntent)
        finish() // Закрытие текущего окна
    }

    // Сохранение колоды в файл
    private fun saveDeckToFile(deck: Deck) {
        val file = File(filesDir, "decks.txt")
        if (!file.exists()) {
            file.createNewFile() // Создаем файл, если он не существует
        }
        // Добавляем новую колоду в файл
        file.appendText("${deck.getDeckId()}:${deck.getName()}\n")
        Toast.makeText(this, "Колода успешно создана!", Toast.LENGTH_SHORT).show()
    }
}
