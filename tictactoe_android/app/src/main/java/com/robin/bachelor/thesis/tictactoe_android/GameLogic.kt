package com.robin.bachelor.thesis.tictactoe_android

class GameLogic {
  var board: Array<Array<String>> = Array(3) { Array(3) { "" } }
  var currentPlayer: String = "X"

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
    for (i in 0..2) {
      if (isSame(board[i][0], board[i][1], board[i][2])) return true
      if (isSame(board[0][i], board[1][i], board[2][i])) return true
    }
    if (isSame(board[0][0], board[1][1], board[2][2])) return true
    if (isSame(board[0][2], board[1][1], board[2][0])) return true
    return false
  }

  fun isDraw(): Boolean {
    return board.all { row -> row.all { it.isNotEmpty() } } && !checkWin()
  }

  fun resetGame() {
    board = Array(3) { Array(3) { "" } }
    currentPlayer = "X"
  }

  private fun isSame(a: String, b: String, c: String): Boolean {
    return a.isNotEmpty() && a == b && b == c
  }
}
