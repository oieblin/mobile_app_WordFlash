package com.example.myapplication.models

class DeckStatistics {
    private var deckId: String? = null
    private var totalCardsLearned: Int = 0
    private var correctAnswers: Int = 0
    private var incorrectAnswers: Int = 0

    // Getters and Setters
    fun getDeckId(): String {
        return deckId ?: ""
    }

    fun setDeckId(deckId: String?) {
        this.deckId = deckId
    }

    fun getTotalCardsLearned(): Int {
        return totalCardsLearned
    }

    fun setTotalCardsLearned(totalCardsLearned: Int) {
        this.totalCardsLearned = totalCardsLearned
    }

    fun getCorrectAnswers(): Int {
        return correctAnswers
    }

    fun setCorrectAnswers(correctAnswers: Int) {
        this.correctAnswers = correctAnswers
    }

    fun getIncorrectAnswers(): Int {
        return incorrectAnswers
    }

    fun setIncorrectAnswers(incorrectAnswers: Int) {
        this.incorrectAnswers = incorrectAnswers
    }
}
