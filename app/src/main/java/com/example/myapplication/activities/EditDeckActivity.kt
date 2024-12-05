package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class EditDeckActivity : AppCompatActivity() {

    private lateinit var deckNameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private var deckId: String? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_deck)

        deckNameEditText = findViewById(R.id.deckNameEditText)
        saveButton = findViewById(R.id.saveDeckButton)
        backButton = findViewById(R.id.cancelButton)

        deckId = intent.getStringExtra("deckId")
        val deckName = intent.getStringExtra("deckName")



        deckNameEditText.setText(deckName)

        saveButton.setOnClickListener { saveDeck() }
        backButton.setOnClickListener { finish() }
    }

    private fun saveDeck() {
        val newDeckName = deckNameEditText.text.toString().trim()
        if (newDeckName.isEmpty()) {
            Toast.makeText(this, "Deck name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(filesDir, "decks.txt")
        file.writeText(file.readLines().joinToString("\n") {
            val parts = it.split(":")
            if (parts[0] == deckId) "${parts[0]}:$newDeckName" else it
        })

        val resultIntent = Intent()
        resultIntent.putExtra("updatedDeckName", newDeckName) // Передаем обновленное имя
        setResult(RESULT_OK, resultIntent)
        Toast.makeText(this, "Deck updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

}
