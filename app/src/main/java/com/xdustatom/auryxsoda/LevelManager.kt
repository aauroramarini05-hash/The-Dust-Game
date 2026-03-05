package com.xdustatom.auryxsoda

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class Level(
    val levelNumber: Int,
    val moves: Int,
    val targetScore: Int,
    val difficulty: String
)

class LevelManager(private val context: Context) {
    
    private var levels: List<Level> = emptyList()
    private var currentLevelIndex: Int = 0
    private val prefs = context.getSharedPreferences("auryx_soda_prefs", Context.MODE_PRIVATE)
    
    init {
        loadLevels()
        currentLevelIndex = prefs.getInt("current_level", 0)
    }
    
    private fun loadLevels() {
        try {
            val inputStream = context.assets.open("levels.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Level>>() {}.type
            levels = Gson().fromJson(reader, type)
            reader.close()
        } catch (e: Exception) {
            // Generate default levels if file not found
            levels = generateDefaultLevels()
        }
    }
    
    private fun generateDefaultLevels(): List<Level> {
        val defaultLevels = mutableListOf<Level>()
        
        for (i in 1..50) {
            val difficulty = when {
                i <= 10 -> "easy"
                i <= 25 -> "medium"
                i <= 40 -> "hard"
                else -> "expert"
            }
            
            val moves = when (difficulty) {
                "easy" -> 30
                "medium" -> 25
                "hard" -> 20
                else -> 15
            }
            
            val targetScore = when (difficulty) {
                "easy" -> 1000 + (i * 100)
                "medium" -> 2000 + (i * 150)
                "hard" -> 3000 + (i * 200)
                else -> 5000 + (i * 300)
            }
            
            defaultLevels.add(Level(i, moves, targetScore, difficulty))
        }
        
        return defaultLevels
    }
    
    fun getCurrentLevel(): Level? {
        return if (currentLevelIndex < levels.size) {
            levels[currentLevelIndex]
        } else null
    }
    
    fun getLevel(index: Int): Level? {
        return if (index >= 0 && index < levels.size) {
            levels[index]
        } else null
    }
    
    fun getTotalLevels(): Int = levels.size
    
    fun setCurrentLevel(index: Int) {
        currentLevelIndex = index.coerceIn(0, levels.size - 1)
    }
    
    fun completeLevel(stars: Int) {
        val currentStars = prefs.getInt("level_${currentLevelIndex}_stars", 0)
        if (stars > currentStars) {
            prefs.edit().putInt("level_${currentLevelIndex}_stars", stars).apply()
        }
        
        val maxUnlocked = prefs.getInt("max_unlocked_level", 0)
        if (currentLevelIndex >= maxUnlocked) {
            prefs.edit().putInt("max_unlocked_level", currentLevelIndex + 1).apply()
        }
    }
    
    fun getLevelStars(levelIndex: Int): Int {
        return prefs.getInt("level_${levelIndex}_stars", 0)
    }
    
    fun isLevelUnlocked(levelIndex: Int): Boolean {
        val maxUnlocked = prefs.getInt("max_unlocked_level", 0)
        return levelIndex <= maxUnlocked
    }
    
    fun getMaxUnlockedLevel(): Int {
        return prefs.getInt("max_unlocked_level", 0)
    }
}
