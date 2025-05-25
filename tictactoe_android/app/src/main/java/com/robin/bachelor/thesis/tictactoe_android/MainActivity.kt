package com.robin.bachelor.thesis.tictactoe_android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  private val game = GameLogic()
  private var isAiMode = false
  private var gameOver = false
  private lateinit var imageViews: Array<Array<ImageView>>
  private lateinit var modeButton: Button
  private lateinit var currentPlayerText: TextView
  private val boardCache = Array(3) { Array(3) { "" } }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    currentPlayerText = findViewById(R.id.currentPlayerText)
    updateCurrentPlayerText()

    imageViews = Array(3) { row ->
      Array(3) { col ->
        findViewById(resources.getIdentifier("img_${row}${col}", "id", packageName))
      }
    }

    imageViews.forEachIndexed { row, array ->
      array.forEachIndexed { col, imageView ->
        imageView.setOnClickListener { handleMove(row, col) }
      }
    }

    modeButton = findViewById(R.id.modeButton)
    modeButton.setOnClickListener { toggleMode() }
  }

  private fun handleMove(row: Int, col: Int) {
    if (gameOver || game.board[row][col].isNotEmpty()) return

    gameOver = game.makeMove(row, col)
    updateBoard()
    updateCurrentPlayerText()

    if (gameOver) {
      showGameOverDialog()
      return
    }

    if (isAiMode && game.currentPlayer == "O") {
      Handler(Looper.getMainLooper()).postDelayed({
        val aiMove = AI().getBestMove(game.board)
        if (aiMove != null) {
          gameOver = game.makeMove(aiMove.first, aiMove.second)
          updateBoard()
          updateCurrentPlayerText()

          if (gameOver) {
            showGameOverDialog()
          }
        }
      }, 500)
    }
  }

  private fun updateCurrentPlayerText() {
    currentPlayerText.text = getString(R.string.txt_current_player, game.currentPlayer)
  }

  private fun updateBoard() {
    imageViews.forEachIndexed { row, array ->
      array.forEachIndexed { col, imageView ->
        val value = game.board[row][col]
        if (value != boardCache[row][col]) {
          when (value) {
            "X" -> imageView.setImageResource(R.drawable.x)
            "O" -> imageView.setImageResource(R.drawable.o)
            else -> imageView.setImageResource(android.R.color.transparent)
          }
          boardCache[row][col] = value
        }
      }
    }
  }

  private fun showGameOverDialog() {
    AlertDialog.Builder(this)
      .setTitle("Game Over")
      .setMessage(if (game.checkWin()) "${game.currentPlayer} Wins!" else "Draw")
      .setPositiveButton("Restart") { _, _ -> resetGame() }
      .show()
  }

  private fun resetGame() {
    game.resetGame()
    gameOver = false
    for (i in 0..2) {
      for (j in 0..2) {
        boardCache[i][j] = ""
      }
    }
    updateBoard()
    updateCurrentPlayerText()
  }

  private fun toggleMode() {
    isAiMode = !isAiMode
    modeButton.text = if (isAiMode) getString(R.string.btn_switch_to_players) else getString(R.string.btn_switch_to_ai)
    resetGame()
  } }
