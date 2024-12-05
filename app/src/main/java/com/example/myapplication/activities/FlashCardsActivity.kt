package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.gestures.OnSwipeTouchListener
import com.example.myapplication.models.Card
import java.io.File

class FlashCardsActivity : AppCompatActivity() {

    private lateinit var cardView: TextView
    private var currentCardIndex = 0
    private var cards: List<Card> = emptyList()  // Теперь список объектов Card
    private var correctAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_cards)

        cardView = findViewById(R.id.cardView)

        // Получаем deck_id, если оно не передано, завершаем активность
        val deckId = intent.getStringExtra("deck_id") ?: run {
            Toast.makeText(this, "No deck selected!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Загружаем карты для колоды
        loadCards(deckId)
        if (cards.isEmpty()) {
            Toast.makeText(this, "No cards available in this deck!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Отображаем первую карту
        showCard()

        // Переключение карты при нажатии
        cardView.setOnClickListener {
            toggleCard()
        }

        // Слушатель свайпов для следующих/предыдущих карт
        cardView.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                // Увеличиваем счетчик правильных ответов
                val card = cards[currentCardIndex]
                card.setCorrectAnswerCount(card.getCorrectAnswerCount() + 1)
                correctAnswers++  // Общий счетчик правильных ответов
                nextCard()
            }

            override fun onSwipeLeft() {
                nextCard()  // Если не выучено, просто показываем следующую карту
            }
        })
    }

    private fun loadCards(deckId: String) {
        val cardsFile = File(filesDir, "cards.txt")
        Log.d("FlashCardsActivity", "Cards file path: ${cardsFile.absolutePath}")

        if (cardsFile.exists()) {
            cards = cardsFile.readLines()
                .filter { it.contains(":") } // Убираем строки без разделителя ":"
                .mapNotNull { line ->
                    val parts = line.split(":")
                    if (parts.size == 3 && parts[0] == deckId) { // Фильтруем по deckId
                        val card = Card()
                        card.setTerm(parts[1].trim()) // Устанавливаем термин
                        card.setDefinition(parts[2].trim()) // Устанавливаем определение
                        card // Возвращаем объект Card
                    } else {
                        null
                    }
                }

            Log.d("FlashCardsActivity", "Loaded ${cards.size} cards for deck $deckId")
        } else {
            Log.d("FlashCardsActivity", "Cards file does not exist at path ${cardsFile.absolutePath}")
        }
    }

    private fun showCard() {
        if (cards.isNotEmpty() && currentCardIndex < cards.size) {
            val card = cards[currentCardIndex]
            cardView.text = card.getTerm()  // Показываем термин на карточке
        }
    }

    private fun toggleCard() {
        val card = cards[currentCardIndex]
        cardView.text = if (cardView.text == card.getTerm()) card.getDefinition() else card.getTerm()  // Переключаемся между термином и определением
    }

    private fun nextCard() {
        currentCardIndex++
        if (currentCardIndex < cards.size) {
            showCard() // Показываем следующую карту
        } else {
            finishTest() // Завершаем тест, если все карты просмотрены
        }
    }

    private fun finishTest() {
        val percentage = (correctAnswers.toDouble() / cards.size) * 100
        saveTestResults(percentage)

        // Переход на экран с результатами теста
        val intent = Intent(this, TestResultsActivity::class.java)
        intent.putExtra("percentage", percentage)
        startActivity(intent)
        finish()
    }

    private fun saveTestResults(percentage: Double) {
        val currentUserFile = File(filesDir, "current_user.txt")
        if (currentUserFile.exists()) {
            val lines = currentUserFile.readLines()
            if (lines.isNotEmpty()) {
                val updatedLines = lines.toMutableList()
                updatedLines[0] = "${updatedLines[0]}, Test result: ${"%.2f".format(percentage)}%"
                currentUserFile.writeText(updatedLines.joinToString("\n"))
            }
        }
    }
}
