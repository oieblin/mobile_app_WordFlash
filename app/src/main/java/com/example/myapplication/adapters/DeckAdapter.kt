package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Deck

class DeckAdapter(
    private var decks: List<Deck>,
    private val onDeckClick: (Deck) -> Unit
) : RecyclerView.Adapter<DeckAdapter.DeckViewHolder>() {

    class DeckViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val deckNameTextView: TextView = view.findViewById(R.id.deckNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = decks[position]
        holder.deckNameTextView.text = deck.getName()
        holder.itemView.setOnClickListener { onDeckClick(deck) }
    }

    override fun getItemCount(): Int = decks.size

    // Метод для обновления списка колод
    fun updateDecks(newDecks: List<Deck>) {
        decks = newDecks
        notifyDataSetChanged()
    }
}

