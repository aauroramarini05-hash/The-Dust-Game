package com.xdustatom.auryxsoda

enum class GameScreen {
    MAIN_MENU,
    LEVEL_SELECT,
    GAME,
    WIN,
    LOSE
}

class ScreenManager {
    private var currentScreen: GameScreen = GameScreen.MAIN_MENU
    private var listeners = mutableListOf<(GameScreen) -> Unit>()
    
    fun setScreen(screen: GameScreen) {
        currentScreen = screen
        notifyListeners()
    }
    
    fun getCurrentScreen(): GameScreen = currentScreen
    
    fun addListener(listener: (GameScreen) -> Unit) {
        listeners.add(listener)
    }
    
    fun removeListener(listener: (GameScreen) -> Unit) {
        listeners.remove(listener)
    }
    
    private fun notifyListeners() {
        listeners.forEach { it(currentScreen) }
    }
}
