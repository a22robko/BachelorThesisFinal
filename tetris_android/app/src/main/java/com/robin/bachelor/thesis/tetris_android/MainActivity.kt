package com.robin.bachelor.thesis.tetris_android

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  private lateinit var gameLogic: GameLogic
  private lateinit var gridLayout: GridLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    gridLayout = findViewById(R.id.gridLayout)
    val restartButton: Button = findViewById(R.id.restartButton)

    val screenWidth = resources.displayMetrics.widthPixels
    val blockSize = screenWidth / 10.0f

    initializeGrid(blockSize.toInt())

    gameLogic = GameLogic(
      onGridUpdated = { updateGrid() },
      onGameOver = { showGameOverDialog() }
    )

    restartButton.setOnClickListener {
      gameLogic.restartGame()
      updateGrid()
    }

    updateGrid()
  }

  private fun initializeGrid(blockSize: Int) {
    gridLayout.removeAllViews()
    gridLayout.rowCount = GameLogic.GRID_HEIGHT
    gridLayout.columnCount = GameLogic.GRID_WIDTH

    for (i in 0 until GameLogic.GRID_HEIGHT * GameLogic.GRID_WIDTH) {
      val cell = TextView(this).apply {
        layoutParams = GridLayout.LayoutParams().apply {
          width = blockSize
          height = blockSize
          setMargins(1, 1, 1, 1)
        }
        setBackgroundResource(R.color.black)
      }
      gridLayout.addView(cell)
    }
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    if (!gameLogic.gameOver) {
      when (keyCode) {
        KeyEvent.KEYCODE_DPAD_LEFT -> gameLogic.moveLeft()
        KeyEvent.KEYCODE_DPAD_RIGHT -> gameLogic.moveRight()
        KeyEvent.KEYCODE_DPAD_DOWN -> gameLogic.moveDown()
        KeyEvent.KEYCODE_DPAD_UP -> gameLogic.rotate()
      }
      updateGrid()
    }
    return super.onKeyDown(keyCode, event)
  }

  private fun showGameOverDialog() {
    AlertDialog.Builder(this)
      .setTitle("Game Over")
      .setMessage("Restart game?")
      .setPositiveButton("Restart") { _, _ ->
        gameLogic.restartGame()
        updateGrid()
      }
      .show()
  }

  private fun updateGrid() {
    for (i in 0 until gridLayout.childCount) {
      val cell = gridLayout.getChildAt(i)
      val row = i / GameLogic.GRID_WIDTH
      val col = i % GameLogic.GRID_WIDTH

      when (gameLogic.grid[row][col]) {
        1 -> cell.setBackgroundResource(R.color.green)
        2 -> cell.setBackgroundResource(R.color.red)
        else -> cell.setBackgroundResource(R.color.black)
      }
    }
  }
}
