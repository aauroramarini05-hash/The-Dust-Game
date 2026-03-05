package com.xdustatom.auryxsoda

import android.content.Context
import android.graphics.*
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class GameView(context: Context) : View(context) {

    private val boardManager = BoardManager(8, 8)
    private val scoreSystem = ScoreSystem()
    private val animationSystem = AnimationSystem()
    private val screenManager = ScreenManager()
    private val levelManager = LevelManager(context)

    private var tileSize: Float = 0f
    private var boardOffsetX: Float = 0f
    private var boardOffsetY: Float = 0f

    private var selectedTile: Point? = null
    private var currentMoves: Int = 0
    private var currentLevel: Level? = null

    private var isProcessing: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    // Paints
    private val candyPaints = mutableMapOf<CandyType, Paint>()
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 48f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val bgPaint = Paint().apply {
        color = Color.parseColor("#FFF5E6")
    }
    private val tileBgPaint = Paint().apply {
        color = Color.parseColor("#FFE4CC")
        style = Paint.Style.FILL
    }
    private val tileStrokePaint = Paint().apply {
        color = Color.parseColor("#FFD9B3")
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val selectedPaint = Paint().apply {
        color = Color.parseColor("#FF6B35")
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    // Sound pool
    private val soundPool: SoundPool
    private var matchSound: Int = 0
    private var swapSound: Int = 0
    private var comboSound: Int = 0
    private var winSound: Int = 0
    private var loseSound: Int = 0

    init {
        // Initialize candy paints
        candyPaints[CandyType.RED] = createCandyPaint(Color.parseColor("#FF3B47"))
        candyPaints[CandyType.BLUE] = createCandyPaint(Color.parseColor("#4A90E2"))
        candyPaints[CandyType.GREEN] = createCandyPaint(Color.parseColor("#7ED321"))
        candyPaints[CandyType.YELLOW] = createCandyPaint(Color.parseColor("#F8E71C"))
        candyPaints[CandyType.PURPLE] = createCandyPaint(Color.parseColor("#BD10E0"))
        candyPaints[CandyType.ORANGE] = createCandyPaint(Color.parseColor("#FF8C00"))

        // Initialize sound pool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sounds (using dummy resource IDs, will be loaded from raw folder)
        try {
            matchSound = soundPool.load(context, R.raw.match, 1)
            swapSound = soundPool.load(context, R.raw.swap, 1)
            comboSound = soundPool.load(context, R.raw.combo, 1)
            winSound = soundPool.load(context, R.raw.win, 1)
            loseSound = soundPool.load(context, R.raw.lose, 1)
        } catch (e: Exception) {
            // Sounds not loaded, continue without audio
        }

        screenManager.addListener { screen ->
            when (screen) {
                GameScreen.GAME -> startGame()
                else -> {}
            }
        }

        screenManager.setScreen(GameScreen.MAIN_MENU)
    }

    private fun createCandyPaint(color: Int): Paint {
        return Paint().apply {
            this.color = color
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    private fun calculateDimensions() {
        val availableWidth = width.toFloat()
        val availableHeight = height.toFloat() * 0.6f // Leave space for UI

        tileSize = min(availableWidth / 8f, availableHeight / 8f) * 0.9f
        boardOffsetX = (availableWidth - tileSize * 8) / 2f
        boardOffsetY = availableHeight * 0.3f
    }

    fun startGame() {
        currentLevel = levelManager.getCurrentLevel()
        currentLevel?.let {
            currentMoves = it.moves
            scoreSystem.reset()
            boardManager.initialize(tileSize)
            selectedTile = null
            isProcessing = false
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        when (screenManager.getCurrentScreen()) {
            GameScreen.MAIN_MENU -> drawMainMenu(canvas)
            GameScreen.LEVEL_SELECT -> drawLevelSelect(canvas)
            GameScreen.GAME -> drawGame(canvas)
            GameScreen.WIN -> drawWinScreen(canvas)
            GameScreen.LOSE -> drawLoseScreen(canvas)
        }
    }

    private fun drawMainMenu(canvas: Canvas) {
        textPaint.textSize = 100f
        textPaint.color = Color.parseColor("#FF6B35")
        canvas.drawText("Auryx Soda", width / 2f, height / 3f, textPaint)

        textPaint.textSize = 60f
        textPaint.color = Color.BLACK
        canvas.drawText("Tap to Play", width / 2f, height / 2f, textPaint)

        textPaint.textSize = 40f
        canvas.drawText("Match-3 Puzzle Game", width / 2f, height / 2f + 100f, textPaint)
    }

    private fun drawLevelSelect(canvas: Canvas) {
        textPaint.textSize = 80f
        textPaint.color = Color.parseColor("#FF6B35")
        canvas.drawText("Select Level", width / 2f, 150f, textPaint)

        val levels = levelManager.getTotalLevels()
        val cols = 5
        val buttonSize = width / (cols + 1f)
        val startY = 250f

        textPaint.textSize = 40f

        for (i in 0 until min(20, levels)) {
            val row = i / cols
            val col = i % cols
            val x = (col + 1) * buttonSize
            val y = startY + row * buttonSize * 1.2f

            val isUnlocked = levelManager.isLevelUnlocked(i)
            val stars = levelManager.getLevelStars(i)

            val buttonPaint = Paint().apply {
                color = if (isUnlocked) Color.parseColor("#4A90E2") else Color.GRAY
                style = Paint.Style.FILL
            }

            canvas.drawCircle(x, y, buttonSize / 3f, buttonPaint)

            textPaint.color = Color.WHITE
            canvas.drawText("${i + 1}", x, y + 15f, textPaint)

            if (stars > 0) {
                textPaint.textSize = 30f
                canvas.drawText("★".repeat(stars), x, y + 50f, textPaint)
                textPaint.textSize = 40f
            }
        }

        textPaint.textSize = 50f
        textPaint.color = Color.BLACK
        canvas.drawText("Tap level to start", width / 2f, height - 100f, textPaint)
    }

    private fun drawGame(canvas: Canvas) {
        // Draw UI header
        drawGameUI(canvas)

        // Draw board
        drawBoard(canvas)

        // Auto-process matches
        if (!isProcessing && !boardManager.updateAnimations(20f)) {
            handler.postDelayed({
                processMatches()
            }, 100)
        }

        invalidate()
    }

    private fun drawGameUI(canvas: Canvas) {
        textPaint.textSize = 50f
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("Score: ${scoreSystem.getScore()}", 50f, 100f, textPaint)

        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("Moves: $currentMoves", width - 50f, 100f, textPaint)

        currentLevel?.let {
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.textSize = 40f
            canvas.drawText("Level ${it.levelNumber}", width / 2f, 100f, textPaint)
            canvas.drawText("Target: ${it.targetScore}", width / 2f, 150f, textPaint)
        }

        val combo = scoreSystem.getComboMultiplier()
        if (combo > 1) {
            textPaint.textSize = 60f
            textPaint.color = Color.parseColor("#FF6B35")
            canvas.drawText("COMBO x$combo", width / 2f, 200f, textPaint)
        }
    }

    private fun drawBoard(canvas: Canvas) {
        val board = boardManager.getBoard()

        for (row in board.indices) {
            for (col in board[row].indices) {
                val x = boardOffsetX + col * tileSize
                val y = boardOffsetY + row * tileSize

                // Draw tile background
                canvas.drawRoundRect(
                    x, y, x + tileSize, y + tileSize,
                    10f, 10f, tileBgPaint
                )
                canvas.drawRoundRect(
                    x, y, x + tileSize, y + tileSize,
                    10f, 10f, tileStrokePaint
                )

                // Draw candy
                val tile = board[row][col]
                tile.candy?.let { candy ->
                    if (!candy.isEmpty()) {
                        drawCandy(canvas, candy, tileSize)
                    }
                }

                // Draw selection
                if (selectedTile?.x == col && selectedTile?.y == row) {
                    canvas.drawRoundRect(
                        x, y, x + tileSize, y + tileSize,
                        10f, 10f, selectedPaint
                    )
                }
            }
        }
    }

    private fun drawCandy(canvas: Canvas, candy: Candy, size: Float) {
        val paint = candyPaints[candy.type] ?: return

        val anim = animationSystem.getDestroyAnimation(Point(candy.position.x.toInt(), candy.position.y.toInt()))
        val alpha = anim?.alpha ?: candy.alpha
        val scale = anim?.scale ?: candy.scale
        val rotation = anim?.rotation ?: candy.rotation

        paint.alpha = (alpha * 255).toInt()

        val centerX = boardOffsetX + candy.position.x * tileSize + tileSize / 2f
        val centerY = boardOffsetY + candy.position.y * tileSize + tileSize / 2f
        val radius = size / 2.5f * scale

        canvas.save()
        canvas.rotate(rotation, centerX, centerY)
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw special indicator
        if (candy.special != CandySpecial.NONE) {
            val specialPaint = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = 6f
                this.alpha = (alpha * 255).toInt()
            }

            when (candy.special) {
                CandySpecial.LINE_HORIZONTAL, CandySpecial.LINE_VERTICAL -> {
                    canvas.drawLine(
                        centerX - radius / 2, centerY,
                        centerX + radius / 2, centerY,
                        specialPaint
                    )
                }
                CandySpecial.BOMB -> {
                    canvas.drawCircle(centerX, centerY, radius / 2, specialPaint)
                }
                else -> {}
            }
        }

        canvas.restore()
        paint.alpha = 255
    }

    private fun drawWinScreen(canvas: Canvas) {
        textPaint.textSize = 100f
        textPaint.color = Color.parseColor("#7ED321")
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("YOU WIN!", width / 2f, height / 3f, textPaint)

        textPaint.textSize = 60f
        textPaint.color = Color.BLACK
        canvas.drawText("Score: ${scoreSystem.getScore()}", width / 2f, height / 2f, textPaint)

        currentLevel?.let {
            val stars = scoreSystem.getStars(it.targetScore)
            textPaint.textSize = 80f
            textPaint.color = Color.parseColor("#F8E71C")
            canvas.drawText("★".repeat(stars), width / 2f, height / 2f + 100f, textPaint)
        }

        textPaint.textSize = 50f
        textPaint.color = Color.BLACK
        canvas.drawText("Tap to continue", width / 2f, height - 150f, textPaint)
    }

    private fun drawLoseScreen(canvas: Canvas) {
        textPaint.textSize = 100f
        textPaint.color = Color.parseColor("#FF3B47")
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("GAME OVER", width / 2f, height / 3f, textPaint)

        textPaint.textSize = 60f
        textPaint.color = Color.BLACK
        canvas.drawText("Score: ${scoreSystem.getScore()}", width / 2f, height / 2f, textPaint)

        textPaint.textSize = 50f
        canvas.drawText("Tap to retry", width / 2f, height - 150f, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            handleTouch(event.x, event.y)
        }
        return true
    }

    private fun handleTouch(x: Float, y: Float) {
        when (screenManager.getCurrentScreen()) {
            GameScreen.MAIN_MENU -> {
                screenManager.setScreen(GameScreen.LEVEL_SELECT)
                invalidate()
            }
            GameScreen.LEVEL_SELECT -> {
                handleLevelSelect(x, y)
            }
            GameScreen.GAME -> {
                if (!isProcessing) {
                    handleGameTouch(x, y)
                }
            }
            GameScreen.WIN -> {
                currentLevel?.let {
                    val stars = scoreSystem.getStars(it.targetScore)
                    levelManager.completeLevel(stars)
                    levelManager.setCurrentLevel(it.levelNumber)
                }
                screenManager.setScreen(GameScreen.LEVEL_SELECT)
                invalidate()
            }
            GameScreen.LOSE -> {
                screenManager.setScreen(GameScreen.GAME)
                startGame()
            }
        }
    }

    private fun handleLevelSelect(x: Float, y: Float) {
        val levels = levelManager.getTotalLevels()
        val cols = 5
        val buttonSize = width / (cols + 1f)
        val startY = 250f

        for (i in 0 until min(20, levels)) {
            val row = i / cols
            val col = i % cols
            val btnX = (col + 1) * buttonSize
            val btnY = startY + row * buttonSize * 1.2f

            val distance = Math.sqrt(((x - btnX) * (x - btnX) + (y - btnY) * (y - btnY)).toDouble())
            if (distance < buttonSize / 3f && levelManager.isLevelUnlocked(i)) {
                levelManager.setCurrentLevel(i)
                screenManager.setScreen(GameScreen.GAME)
                return
            }
        }
    }

    private fun handleGameTouch(x: Float, y: Float) {
        val col = ((x - boardOffsetX) / tileSize).toInt()
        val row = ((y - boardOffsetY) / tileSize).toInt()

        if (col < 0 || col >= 8 || row < 0 || row >= 8) return

        val tappedTile = Point(col, row)

        if (selectedTile == null) {
            selectedTile = tappedTile
            playSound(swapSound)
            vibrate(50)
        } else {
            if (selectedTile == tappedTile) {
                selectedTile = null
            } else {
                attemptSwap(selectedTile!!, tappedTile)
                selectedTile = null
            }
        }

        invalidate()
    }

    private fun attemptSwap(pos1: Point, pos2: Point) {
        if (boardManager.swapCandies(pos1, pos2)) {
            currentMoves--
            isProcessing = true
            playSound(swapSound)
            vibrate(50)

            handler.postDelayed({
                processMatches()
            }, 300)
        } else {
            playSound(swapSound)
            vibrate(100)
        }
    }

    private fun processMatches() {
        val matches = boardManager.findMatches()

        if (matches.isNotEmpty()) {
            scoreSystem.incrementCombo()

            for (match in matches) {
                scoreSystem.addMatchScore(match.size, match.isSpecialMatch())
            }

            val removed = boardManager.removeMatches(matches)
            animationSystem.startDestroyAnimation(removed)

            playSound(if (scoreSystem.getComboMultiplier() > 1) comboSound else matchSound)
            vibrate(100)

            handler.postDelayed({
                boardManager.applyGravity(tileSize)
                handler.postDelayed({
                    processMatches()
                }, 500)
            }, 300)
        } else {
            scoreSystem.resetCombo()
            isProcessing = false

            checkGameEnd()
        }

        invalidate()
    }

    private fun checkGameEnd() {
        currentLevel?.let {
            if (scoreSystem.getScore() >= it.targetScore) {
                playSound(winSound)
                vibrate(200)
                handler.postDelayed({
                    screenManager.setScreen(GameScreen.WIN)
                    invalidate()
                }, 500)
            } else if (currentMoves <= 0 && !boardManager.hasValidMoves()) {
                playSound(loseSound)
                vibrate(300)
                handler.postDelayed({
                    screenManager.setScreen(GameScreen.LOSE)
                    invalidate()
                }, 500)
            }
        }
    }

    private fun playSound(soundId: Int) {
        try {
            soundPool.play(soundId, 0.5f, 0.5f, 0, 0, 1f)
        } catch (e: Exception) {
            // Sound playback failed
        }
    }

    private fun vibrate(duration: Long) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        } catch (e: Exception) {
            // Vibration failed
        }
    }

    fun onPause() {
        soundPool.autoPause()
    }

    fun onResume() {
        soundPool.autoResume()
    }

    fun onDestroy() {
        soundPool.release()
    }
}
