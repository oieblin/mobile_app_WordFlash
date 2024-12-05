package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Card

class CardAdapter(
    private var cardList: MutableList<Card>,
    private val onClick: (Card) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val termTextView: TextView = itemView.findViewById(R.id.termTextView)
        private val definitionTextView: TextView = itemView.findViewById(R.id.definitionTextView)

        fun bind(card: Card) {
            termTextView.text = card.getTerm()
            definitionTextView.text = card.getDefinition()
            itemView.setOnClickListener { onClick(card) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int = cardList.size

    fun updateCards(newCardList: List<Card>) {
        cardList.clear()
        cardList.addAll(newCardList)
        notifyDataSetChanged()
    }
}

