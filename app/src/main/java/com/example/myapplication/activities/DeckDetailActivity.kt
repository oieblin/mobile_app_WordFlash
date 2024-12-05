package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.CardAdapter
import com.example.myapplication.models.Card
import com.example.myapplication.models.Deck
import java.io.File

class DeckDetailActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var deckNameTextView: TextView
    private lateinit var editDeckButton: Button
    private lateinit var deleteDeckButton: Button
    private lateinit var addCardButton: Button
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var currentDeck: Deck

    private val addCardLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                updateCards()
            }
        }
    private val editDeckLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedDeckName = result.data?.getStringExtra("updatedDeckName")
                if (!updatedDeckName.isNullOrEmpty()) {
                    currentDeck.setName(updatedDeckName)
                    deckNameTextView.text = updatedDeckName // Обновляем имя колоды на экране
                    Toast.makeText(this, "Deck updated!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val editCardLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val cardUpdated = data?.getBooleanExtra("cardUpdated", false) ?: false
            val cardDeleted = data?.getBooleanExtra("cardDeleted", false) ?: false

            if (cardUpdated) {
                Toast.makeText(this, "Card updated", Toast.LENGTH_SHORT).show()
                updateCards()
            } else if (cardDeleted) {
                Toast.makeText(this, "Card deleted", Toast.LENGTH_SHORT).show()
                updateCards()
            }
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_detail)

        // Инициализация UI
        backButton = findViewById(R.id.cancelButton)
        deckNameTextView = findViewById(R.id.deckNameTextView)
        editDeckButton = findViewById(R.id.editDeckButton)
        deleteDeckButton = findViewById(R.id.deleteDeckButton)
        addCardButton = findViewById(R.id.addCardButton)
        cardRecyclerView = findViewById(R.id.cardRecyclerView)

        // Получение ID колоды
        val deckId = intent.getStringExtra("deckId")
        if (deckId.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid deck ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Загрузка текущей колоды
        currentDeck = loadDeck(deckId)
        deckNameTextView.text = currentDeck.getName()

        // Настройка RecyclerView
        cardRecyclerView.layoutManager = LinearLayoutManager(this)
        cardAdapter = CardAdapter(currentDeck.getCards().toMutableList()) { card ->
            navigateToEditCard(card)
        }
        cardRecyclerView.adapter = cardAdapter

        // Установить обработчики
        backButton.setOnClickListener { finish() }
        editDeckButton.setOnClickListener { navigateToEditDeck() }
        deleteDeckButton.setOnClickListener { deleteDeck() }
        addCardButton.setOnClickListener { addCard() }
    }

    override fun onResume() {
        super.onResume()
        updateCards()
    }

    private fun updateCards() {
        val updatedCards = loadCards(currentDeck.getDeckId()) // Загружаем карты для текущей колоды
        cardAdapter.updateCards(updatedCards)                // Обновляем адаптер
    }


    private fun loadDeck(deckId: String): Deck {
        val file = File(filesDir, "decks.txt")
        if (!file.exists()) {
            Toast.makeText(this, "Deck file not found", Toast.LENGTH_SHORT).show()
            finish()
            return Deck()
        }

        var loadedDeck: Deck? = null
        file.forEachLine { line ->
            val parts = line.split(":")
            if (parts.size >= 2 && parts[0] == deckId) {
                loadedDeck = Deck().apply {
                    setDeckId(parts[0])
                    setName(parts[1])
                    setCards(loadCards(deckId))
                }
                return@forEachLine
            }
        }

        if (loadedDeck == null) {
            Toast.makeText(this, "Deck not found", Toast.LENGTH_SHORT).show()
            finish()
            return Deck()
        }

        return loadedDeck!!
    }

    private fun loadCards(deckId: String): MutableList<Card> {
        val file = File(filesDir, "cards.txt")
        val cards = mutableListOf<Card>()

        if (file.exists()) {
            file.forEachLine { line ->
                val parts = line.split(":")
                if (parts.size == 3 && parts[0] == deckId) {
                    val card = Card().apply {
                        setTerm(parts[1])        // Термин карточки
                        setDefinition(parts[2])  // Определение карточки
                    }
                    cards.add(card)
                }
            }
        } else {
            Toast.makeText(this, "No cards found for this deck.", Toast.LENGTH_SHORT).show()
        }

        return cards
    }


    private fun navigateToEditDeck() {
        val intent = Intent(this, EditDeckActivity::class.java)
        intent.putExtra("deckId", currentDeck.getDeckId())
        intent.putExtra("deckName", currentDeck.getName())
        editDeckLauncher.launch(intent)
    }


    // При переходе на EditCardActivity
    private fun navigateToEditCard(card: Card) {
        val intent = Intent(this, EditCardActivity::class.java).apply {
            putExtra("deckId", currentDeck.getDeckId())
            putExtra("term", card.getTerm())
            putExtra("definition", card.getDefinition())
        }
        editCardLauncher.launch(intent)
    }

    private fun deleteDeck() {
        val file = File(filesDir, "decks.txt")
        file.writeText(file.readLines().filterNot { it.startsWith(currentDeck.getDeckId()) }.joinToString("\n"))

        val cardsFile = File(filesDir, "cards.txt")
        cardsFile.writeText(cardsFile.readLines().filterNot { line ->
            val parts = line.split(":")
            parts.size >= 4 && parts[0] == currentDeck.getDeckId()
        }.joinToString("\n"))

        Toast.makeText(this, "Deck deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun addCard() {
        val intent = Intent(this, AddCardActivity::class.java)
        intent.putExtra("deckId", currentDeck.getDeckId())
        addCardLauncher.launch(intent)
    }
}
