package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.DeckAdapter
import com.example.myapplication.models.Deck
import java.io.File

class HomepageActivity : AppCompatActivity() {

    private lateinit var deckRecyclerView: RecyclerView
    private lateinit var addDeckButton: Button
    private lateinit var homeButton: Button
    private lateinit var testingButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var profileButton: Button
    private lateinit var adapter: DeckAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // TextView for the page title
        val myCollectionsTextView: TextView = findViewById(R.id.myCollectionsTextView)

        // Initialize RecyclerView
        deckRecyclerView = findViewById(R.id.deckRecyclerView)
        deckRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load decks and set up adapter
        val decks = loadDecks()
        adapter = DeckAdapter(decks) { selectedDeck ->
            openDeckActivity(selectedDeck)
        }
        deckRecyclerView.adapter = adapter

        if (decks.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_decks_message), Toast.LENGTH_SHORT).show()
        }

        // Add Deck Button
        addDeckButton = findViewById(R.id.addDeckButton)
        addDeckButton.setOnClickListener {
            createNewDeck()
        }

        // Initialize navigation buttons
        homeButton = findViewById(R.id.homeButton)
        testingButton = findViewById(R.id.testingButton)
        statisticsButton = findViewById(R.id.statisticsButton)
        profileButton = findViewById(R.id.profileButton)

        // Set click listeners for navigation buttons
        homeButton.setOnClickListener {
            startActivity(Intent(this, HomepageActivity::class.java))
        }

        testingButton.setOnClickListener {
            startActivity(Intent(this, TestingPageActivity::class.java))
        }

        statisticsButton.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfilePageActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload decks and update adapter
        val updatedDecks = loadDecks()
        adapter.updateDecks(updatedDecks)
    }

    // Function to load decks from a file
    private fun loadDecks(): List<Deck> {
        val file = File(filesDir, "decks.txt")
        val decks = mutableListOf<Deck>()

        if (file.exists()) {
            file.forEachLine { line ->
                val parts = line.split(":")
                if (parts.size == 2) {
                    val deck = Deck().apply {
                        setDeckId(parts[0])
                        setName(parts[1])
                    }
                    decks.add(deck)
                }
            }
        }
        return decks
    }


    // Function to open the selected deck
    private fun openDeckActivity(deck: Deck) {
        val intent = Intent(this, DeckDetailActivity::class.java)
        intent.putExtra("deckId", deck.getDeckId())
        intent.putExtra("deckName", deck.getName())
        startActivity(intent)
    }

    // Function to create a new deck
    private fun createNewDeck() {
        val intent = Intent(this, AddDeckActivity::class.java)
        startActivity(intent)
    }

    // Function to save a deck to the file
    private fun saveDeck(deck: Deck) {
        val file = File(filesDir, "decks.txt")
        file.appendText("${deck.getDeckId()}:${deck.getName()}\n")
    }
}
