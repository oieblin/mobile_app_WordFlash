package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class AddCardActivity : AppCompatActivity() {

    private lateinit var termEditText: EditText
    private lateinit var definitionEditText: EditText
    private lateinit var createCardButton: Button
    private lateinit var deckSpinner: Spinner
    private lateinit var cancelButton: Button

    private var deckMap: MutableMap<String, String> = mutableMapOf() // Map for deckId to deckName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        // Инициализация UI компонентов
        termEditText = findViewById(R.id.termEditText)
        definitionEditText = findViewById(R.id.definitionEditText)
        createCardButton = findViewById(R.id.createCardButton)
        deckSpinner = findViewById(R.id.deckSpinner)
        cancelButton = findViewById(R.id.cancelButton)

        // Загрузка колод
        loadDecks()

        // Слушатель на создание карточки
        createCardButton.setOnClickListener {
            val term = termEditText.text.toString().trim()
            val definition = definitionEditText.text.toString().trim()
            val selectedDeckId = getSelectedDeckId()

            if (selectedDeckId == null) {
                Toast.makeText(this, "Please select a deck!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (term.isEmpty() || definition.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (term.contains(":") || definition.contains(":")) {
                Toast.makeText(this, "Term and definition cannot contain ':'", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveCard(selectedDeckId, term, definition)
        }

        // Слушатель на кнопку отмены
        cancelButton.setOnClickListener { finish() }
    }

    // Метод для загрузки колод в Spinner
    private fun loadDecks() {
        val file = File(filesDir, "decks.txt")
        if (!file.exists()) {
            Toast.makeText(this, "No decks found. Please create a deck first.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val deckNames = mutableListOf<String>()
        file.forEachLine { line ->
            val parts = line.split(":")
            if (parts.size == 2) {
                val deckId = parts[0]
                val deckName = parts[1]
                deckMap[deckId] = deckName
                deckNames.add(deckName)
            }
        }

        // Устанавливаем адаптер для Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deckNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deckSpinner.adapter = adapter
    }

    // Метод для получения выбранного deckId
    private fun getSelectedDeckId(): String? {
        val selectedDeckName = deckSpinner.selectedItem as? String
        return deckMap.entries.find { it.value == selectedDeckName }?.key
    }

    // Метод для сохранения карточки
    // В методе saveCard, после добавления карточки:
    // В методе saveCard, после добавления карточки:
    private fun saveCard(deckId: String, term: String, definition: String) {
        try {
            val file = File(filesDir, "cards.txt")
            val cardEntry = "$deckId:$term:$definition\n"

            if (!file.exists()) {
                file.createNewFile()
            }

            file.appendText(cardEntry)

            Toast.makeText(this, "Card added successfully!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()

        } catch (e: Exception) {
            Toast.makeText(this, "Error saving card: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}
