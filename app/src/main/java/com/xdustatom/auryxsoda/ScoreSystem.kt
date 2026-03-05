package com.xdustatom.auryxsoda

class ScoreSystem {
    
    private var currentScore: Int = 0
    private var comboMultiplier: Int = 1
    private var consecutiveMatches: Int = 0
    
    fun reset() {
        currentScore = 0
        comboMultiplier = 1
        consecutiveMatches = 0
    }
    
    fun addMatchScore(matchSize: Int, isSpecial: Boolean): Int {
        var points = when (matchSize) {
            3 -> 50
            4 -> 100
            5 -> 200
            else -> matchSize * 20
        }
        
        if (isSpecial) {
            points *= 2
        }
        
        points *= comboMultiplier
        currentScore += points
        
        return points
    }
    
    fun incrementCombo() {
        consecutiveMatches++
        comboMultiplier = 1 + (consecutiveMatches / 2)
    }
    
    fun resetCombo() {
        consecutiveMatches = 0
        comboMultiplier = 1
    }
    
    fun getScore(): Int = currentScore
    
    fun getComboMultiplier(): Int = comboMultiplier
    
    fun getStars(targetScore: Int): Int {
        val percentage = (currentScore.toFloat() / targetScore.toFloat()) * 100
        return when {
            percentage >= 150 -> 3
            percentage >= 100 -> 2
            percentage >= 70 -> 1
            else -> 0
        }
    }
}
