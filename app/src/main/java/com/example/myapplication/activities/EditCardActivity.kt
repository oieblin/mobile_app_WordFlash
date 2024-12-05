package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class EditCardActivity : AppCompatActivity() {

    private lateinit var termEditText: EditText
    private lateinit var definitionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: ImageButton
    private lateinit var deleteButton: Button

    private var deckId: String? = null
    private var term: String? = null
    private var definition: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_card)

        termEditText = findViewById(R.id.termEditText)
        definitionEditText = findViewById(R.id.definitionEditText)
        saveButton = findViewById(R.id.saveCardButton)
        cancelButton = findViewById(R.id.cancelButton)
        deleteButton = findViewById(R.id.deleteCardButton)

        deckId = intent.getStringExtra("deckId")
        term = intent.getStringExtra("term")
        definition = intent.getStringExtra("definition")

        termEditText.setText(term ?: "")
        definitionEditText.setText(definition ?: "")

        saveButton.setOnClickListener { saveCard() }
        cancelButton.setOnClickListener { onCancel() }
        deleteButton.setOnClickListener { deleteCard() }
    }

    private fun saveCard() {
        val newTerm = termEditText.text.toString().trim()
        val newDefinition = definitionEditText.text.toString().trim()

        if (newTerm.isEmpty() || newDefinition.isEmpty()) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(filesDir, "cards.txt")
        try {
            val updated = file.readLines().joinToString("\n") { line ->
                val parts = line.split(":")
                if (parts.size >= 3 && parts[0] == deckId && parts[1] == term) {
                    "$deckId:$newTerm:$newDefinition"
                } else {
                    line
                }
            }
            file.writeText(updated)

            Toast.makeText(this, "Card updated successfully", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK, Intent().putExtra("cardUpdated", true))
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error updating card: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteCard() {
        val file = File(filesDir, "cards.txt")
        try {
            val updated = file.readLines().filterNot { line ->
                val parts = line.split(":")
                parts.size >= 3 && parts[0] == deckId && parts[1] == term
            }.joinToString("\n")
            file.writeText(updated)

            Toast.makeText(this, "Card deleted successfully", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK, Intent().putExtra("cardDeleted", true))
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error deleting card: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onCancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
