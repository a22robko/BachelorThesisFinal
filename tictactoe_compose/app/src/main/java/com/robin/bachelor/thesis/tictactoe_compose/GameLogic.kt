package com.robin.bachelor.thesis.tictactoe_compose

import androidx.compose.runtime.mutableStateListOf

class GameLogic {
  var board = mutableStateListOf(
    mutableStateListOf("", "", ""),
    mutableStateListOf("", "", ""),
    mutableStateListOf("", "", "")
  )
  var currentPlayer = "X"

  fun makeMove(row: Int, col: Int): Boolean {
    if (board[row][col].isEmpty()) {
      board[row][col] = currentPlayer
      if (checkWin() || isDraw()) return true
      switchPlayer()
    }
    return false
  }

  private fun switchPlayer() {
    currentPlayer = if (currentPlayer == "X") "O" else "X"
  }

  fun checkWin(): Boolean {
    for (i in 0 until 3) {
      if ((board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty()) ||
        (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty())
      ) return true
    }
    if ((board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty()) ||
      (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty())
    ) return true
    return false
  }

  fun isDraw(): Boolean {
    return board.all { row -> row.all { it.isNotEmpty() } } && !checkWin()
  }

  fun resetGame() {
    board.forEach { row -> row.replaceAll { "" } }
    currentPlayer = "X"
  }
}
