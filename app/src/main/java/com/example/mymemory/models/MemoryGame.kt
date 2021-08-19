package com.example.mymemory.models

import com.example.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?){


    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlip = 0
    private var indexOfSingleSelectedCard: Int? = null
    private val TAG = "MemoryGame"

    init {
        if(customImages == null){
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
        }else {
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode(),it) }
        }
    }

    fun flipCard(position: Int): Boolean {


        val card = cards[position]
        // Three cases:
        // 0 card previously flipped over => restore cards + flip over the selected card
        // 1 card previously flipped over => flip over the selected card + check if they match
        // 2 cards previously flipped over => restore cards + flip over the selected card
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {

            restoreCards()
            indexOfSingleSelectedCard = position
           //0 cards flipped over or 2
        }else{
            //exactly one card flipped over
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        numCardFlip ++
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }
    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }else{

            cards[position1].isMatched = true
            cards[position2].isMatched = true
            numPairsFound++
            return true

        }
    }
    private fun restoreCards(){
        for (card in cards){
            if(!card.isMatched)
                card.isFaceUp = false

        }


    }

    fun haveWonGame(): Boolean {
        return  numPairsFound == boardSize.getNumPairs()
    }

    fun isAlreadyFaceUp(position: Int): Boolean {
         return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
     return  numCardFlip / 2
    }
}