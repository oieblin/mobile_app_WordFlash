package com.example.myapplication.models

class Statistics {
    private var userId: String? = null
    private var totalCardsLearned: Int = 0
    private var correctAnswers: Int = 0
    private var incorrectAnswers: Int = 0
    private var deckProgress: MutableMap<String, DeckStatistics> = mutableMapOf()

    // Getters and Setters
    fun getUserId(): String {
        return userId ?: ""
    }

    fun setUserId(userId: String?) {
        this.userId = userId
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

    fun getDeckProgress(): Map<String, DeckStatistics> {
        return deckProgress
    }

    fun setDeckProgress(deckProgress: Map<String, DeckStatistics>) {
        this.deckProgress.clear()
        this.deckProgress.putAll(deckProgress)
    }

    // Method to update progress
    fun updateDeckStatistics(deckId: String, stats: DeckStatistics) {
        deckProgress[deckId] = stats
    }
}
