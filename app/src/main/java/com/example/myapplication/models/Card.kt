package com.example.myapplication.models

class Card {
    private var cardId: String = ""
    private var term: String = ""
    private var definition: String = ""
    private var audioUri: String = ""
    private var level: String = ""
    private var correctAnswerCount = 0



    // Getters and Setters

    fun setCardId(id: String) { cardId = id }
    fun getTerm(): String = term
    fun setTerm(t: String) { term = t }
    fun getDefinition(): String = definition
    fun setDefinition(def: String) { definition = def }
    fun getCardId(): String = cardId


    fun getLevel(): String = level

    fun setLevel(level: String) {
        this.level = level
    }

    fun getCorrectAnswerCount(): Int = correctAnswerCount

    fun setCorrectAnswerCount(correctAnswerCount: Int) {
        this.correctAnswerCount = correctAnswerCount
    }
}
