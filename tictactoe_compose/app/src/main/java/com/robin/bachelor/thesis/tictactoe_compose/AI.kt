package com.robin.bachelor.thesis.tictactoe_compose

class AI {
  fun getBestMove(board: List<MutableList<String>>): Pair<Int, Int>? {
    val aiPlayer = "O"
    val humanPlayer = "X"

    findWinningMove(board, aiPlayer)?.let { return it }
    findWinningMove(board, humanPlayer)?.let { return it }

    return findBestMove(board)
  }

  private fun findWinningMove(board: List<MutableList<String>>, player: String): Pair<Int, Int>? {
    for (i in 0..2) {
      for (j in 0..2) {
        if (board[i][j].isEmpty()) {
          board[i][j] = player
          val isWinning = checkWin(board, player)
          board[i][j] = ""
          if (isWinning) return Pair(i, j)
        }
      }
    }
    return null
  }

  private fun findBestMove(board: List<MutableList<String>>): Pair<Int, Int>? {
    val center = Pair(1, 1)
    if (board[1][1].isEmpty()) return center

    val corners = listOf(Pair(0, 0), Pair(0, 2), Pair(2, 0), Pair(2, 2))
    val availableCorners = corners.filter { board[it.first][it.second].isEmpty() }
    if (availableCorners.isNotEmpty()) {
      return availableCorners.random()
    }

    val availableMoves = mutableListOf<Pair<Int, Int>>()
    for (i in 0..2) {
      for (j in 0..2) {
        if (board[i][j].isEmpty()) availableMoves.add(Pair(i, j))
      }
    }
    return if (availableMoves.isNotEmpty()) availableMoves.random() else null
  }

  private fun checkWin(board: List<MutableList<String>>, player: String): Boolean {
    for (i in 0..2) {
      if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true
      if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true
    }
    if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true
    if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true
    return false
  }
}
