package com.robin.bachelor.thesis.tetris_compose

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.collections.HashSet

class GameLogic {
  companion object {
    const val GRID_WIDTH = 8
    const val GRID_HEIGHT = 12
  }

  var gameOver by mutableStateOf(false)
  var grid by mutableStateOf(Array(GRID_HEIGHT) { IntArray(GRID_WIDTH) })
  private var settledBlocks = HashSet<Pair<Int, Int>>()
  private var currentTetromino = Tetromino()

  private val handler = Handler(Looper.getMainLooper())
  private val fallRunnable = object : Runnable {
    override fun run() {
      if (!gameOver) {
        moveDown()
        handler.postDelayed(this, 1000)
      }
    }
  }

  init {
    handler.postDelayed(fallRunnable, 1000)
    handler.post { updateGridState() }
  }

  fun restartGame() {
    handler.removeCallbacks(fallRunnable)
    grid = Array(GRID_HEIGHT) { IntArray(GRID_WIDTH) }
    settledBlocks.clear()
    gameOver = false
    handler.postDelayed(fallRunnable, 1000)
    currentTetromino = Tetromino()
    updateGridState()
  }

  fun moveLeft() {
    if (!gameOver && !wouldOverlap(-1, 0, 0)) {
      currentTetromino.moveLeft()
      updateGridState()
    }
  }

  fun moveRight() {
    if (!gameOver && !wouldOverlap(1, 0, 0)) {
      currentTetromino.moveRight()
      updateGridState()
    }
  }

  fun moveDown() {
    if (!gameOver && !wouldOverlap(0, 1, 0)) {
      currentTetromino.moveDown()
    } else if (!gameOver) {
      settledBlocks.addAll(currentTetromino.getBlocks(0, 0, 0))
      clearFullRows()
      currentTetromino = Tetromino()
      if (wouldOverlap(0, 0, 0)) {
        gameOver = true
      }
    }
    updateGridState()
  }

  fun rotate() {
    if (!gameOver && !wouldOverlap(0, 0, 1)) {
      currentTetromino.rotate()
      updateGridState()
    }
  }

  private fun updateGridState() {
    val newGrid = Array(GRID_HEIGHT) { IntArray(GRID_WIDTH) }

    for (row in 0 until GRID_HEIGHT) {
      for (col in 0 until GRID_WIDTH) {
        newGrid[row][col] = 0
      }
    }

    for ((x, y) in currentTetromino.getBlocks(0, 0, 0)) {
      if (y in 0 until GRID_HEIGHT && x in 0 until GRID_WIDTH) {
        newGrid[y][x] = 1
      }
    }

    for ((x, y) in settledBlocks) {
      if (y in 0 until GRID_HEIGHT && x in 0 until GRID_WIDTH) {
        newGrid[y][x] = 2
      }
    }

    grid = newGrid
  }

  private fun clearFullRows() {
    val rowsToClear = mutableSetOf<Int>()

    for (row in 0 until GRID_HEIGHT) {
      if ((0 until GRID_WIDTH).all { col -> Pair(col, row) in settledBlocks }) {
        rowsToClear.add(row)
      }
    }

    if (rowsToClear.isNotEmpty()) {
      val newBlocks = mutableSetOf<Pair<Int, Int>>()

      for ((x, y) in settledBlocks) {
        val dropAmount = rowsToClear.count { it > y }
        if ((x to y) !in rowsToClear.map { row -> x to row }) {
          newBlocks.add(x to (y + dropAmount))
        }
      }

      settledBlocks.clear()
      settledBlocks.addAll(newBlocks)
    }
  }

  private fun wouldOverlap(dx: Int, dy: Int, dr: Int): Boolean {
    return currentTetromino.getBlocks(dx, dy, dr)
      .any { it.first < 0 || it.first >= GRID_WIDTH || it.second < 0  ||  it.second >= GRID_HEIGHT || it in settledBlocks }
  }
}
