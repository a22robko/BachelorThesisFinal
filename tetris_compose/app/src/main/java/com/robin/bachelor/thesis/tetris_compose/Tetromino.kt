package com.robin.bachelor.thesis.tetris_compose

import kotlin.random.Random

class Tetromino {
  private var position = Pair(GameLogic.GRID_WIDTH / 2, 0)
  private var shape = Random.nextInt(3) // 0 = square, 1 = L-shape, 2 = T-shape
  private var rotate = 0

  fun getBlocks(dx: Int, dy: Int, dr: Int): List<Pair<Int, Int>> {
    val x = position.first + dx
    val y = position.second + dy
    val r = (rotate + dr) % 4

    return when (shape) {
      0 -> listOf(Pair(x, y), Pair(x + 1, y), Pair(x, y + 1), Pair(x + 1, y + 1)) // Square
      1 -> when (r) {
        0 -> listOf(Pair(x, y), Pair(x + 1, y), Pair(x - 1, y), Pair(x - 1, y + 1))
        1 -> listOf(Pair(x, y), Pair(x, y + 1), Pair(x, y - 1), Pair(x - 1, y - 1))
        2 -> listOf(Pair(x, y), Pair(x - 1, y), Pair(x + 1, y), Pair(x + 1, y - 1))
        else -> listOf(Pair(x, y), Pair(x, y - 1), Pair(x, y + 1), Pair(x + 1, y + 1))
      }
      else -> when (r) { // T-Shape
        0 -> listOf(Pair(x, y), Pair(x - 1, y), Pair(x + 1, y), Pair(x, y + 1))
        1 -> listOf(Pair(x, y), Pair(x, y - 1), Pair(x, y + 1), Pair(x - 1, y))
        2 -> listOf(Pair(x, y), Pair(x - 1, y), Pair(x + 1, y), Pair(x, y - 1))
        else -> listOf(Pair(x, y), Pair(x, y - 1), Pair(x, y + 1), Pair(x + 1, y))
      }
    }
  }

  fun moveLeft() { position = Pair(position.first - 1, position.second) }

  fun moveRight() { position = Pair(position.first + 1, position.second) }

  fun moveDown() { position = Pair(position.first, position.second + 1) }

  fun rotate() { rotate = (rotate + 1) % 4 }
}