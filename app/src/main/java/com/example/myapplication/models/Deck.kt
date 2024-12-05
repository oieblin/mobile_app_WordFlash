package com.example.myapplication.models

class Deck {
    private var deckId: String? = null
    private var name: String? = null
    private var cards: MutableList<Card> = mutableListOf()
    private var owner: User? = null

    // Getters and Setters
    fun getDeckId(): String {
        return deckId ?: ""
    }

    fun setDeckId(deckId: String?) {
        this.deckId = deckId
    }

    fun getName(): String {
        return name ?: ""
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getCards(): List<Card> {
        return cards
    }

    fun setCards(cards: Any) {
        this.cards.clear()
        this.cards.addAll(cards as Collection<Card>)
    }

    fun getOwner(): User? {
        return owner
    }

    fun setOwner(owner: User?) {
        this.owner = owner
    }

    // Method to add a card
    fun addCard(card: Card) {
        cards.add(card)
    }

    // Method to remove a card
    fun removeCard(card: Card) {
        cards.remove(card)
    }
}
