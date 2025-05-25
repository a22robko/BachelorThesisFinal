import 'dart:async';
import 'dart:math';
import 'package:flutter/services.dart';

import 'tetromino.dart';




class GameLogic {
  static const int gridWidth = 8;
  static const int gridHeight = 12;


  List<List<int>> grid = List.generate(
      gridHeight, (_) => List.filled(gridWidth, 0));
  Tetromino? currentTetromino;
  Set<Point<int>> settledBlocks = {};
  bool gameOver = false;


  final Function onGridUpdated;
  final Function onGameOver;


  GameLogic({required this.onGridUpdated, required this.onGameOver}) {
    createTetromino();


    HardwareKeyboard.instance.addHandler((event) {
      if (!gameOver && event is KeyDownEvent) {
        if (event.logicalKey == LogicalKeyboardKey.arrowLeft) {
          if (!isTetrominoOverlapping(-1, 0, 0)) currentTetromino?.moveLeft();
          repaint();
        } else if (event.logicalKey == LogicalKeyboardKey.arrowRight) {
          if (!isTetrominoOverlapping(1, 0, 0)) currentTetromino?.moveRight();
          repaint();
        } else if (event.logicalKey == LogicalKeyboardKey.arrowDown) {
          if (!isTetrominoOverlapping(0, 1, 0)) currentTetromino?.moveDown();
          repaint();
        } else if (event.logicalKey == LogicalKeyboardKey.arrowUp) {
          if (!isTetrominoOverlapping(0, 0, 1)) currentTetromino?.rotateClockwise();
          repaint();
        }
      }
      return false;
    });


    Timer.periodic(const Duration(seconds: 1), (timer) {
      if (gameOver || currentTetromino == null) return;


      if (!isTetrominoOverlapping(0, 1, 0)) {
        currentTetromino?.moveDown();
      } else {
        settledBlocks.addAll(currentTetromino!.getBlocks(0, 0, 0));
        settleRows();
        createTetromino();
      }


      repaint();
    });
  }


  void restartGame() {
    settledBlocks.clear();


    for (int row = 0; row < gridHeight; row++) {
      for (int col = 0; col < gridWidth; col++) {
        grid[row][col] = 0;
      }
    }


    gameOver = false;


    createTetromino();
  }


  void createTetromino() {
    final random = Random();
    int shape = random.nextInt(3); // 0 = square, 1 = L-shape, 2 = T-shape
    currentTetromino =
        Tetromino(position: Point(gridWidth ~/ 2, 0), shape: shape, rotate: 0);


    if (isTetrominoOverlapping(0, 0, 0)) {
      gameOver = true;
      onGameOver();
    } else {
      repaint();
    }
  }


  bool isTetrominoOverlapping(int dx, int dy, int dr) {
    if (currentTetromino == null) return true;


    for (var point in currentTetromino!.getBlocks(dx, dy, dr)) {
      if (point.x < 0 || point.x >= gridWidth || point.y < 0 || point.y >= gridHeight) {
        return true;
      }
      if (settledBlocks.contains(Point(point.x, point.y))) {
        return true;
      }
    }
    return false;
  }


  void settleRows() {
    for (int row = 0; row < gridHeight; row++) {
      bool fullRow = true;
      for (int col = 0; col < gridWidth; col++) {
        if (!settledBlocks.contains(Point(col, row))) {
          fullRow = false;
          break;
        }
      }
      if (fullRow) {
        settledBlocks.removeWhere((point) => point.y == row);
        Set<Point<int>> newSettledBlocks = {};
        for (var point in settledBlocks) {
          if (point.y < row) {
            newSettledBlocks.add(Point(point.x, point.y + 1));
          } else {
            newSettledBlocks.add(point);
          }
        }
        settledBlocks = newSettledBlocks;
      }
    }
  }


  void repaint() {
    for (int row = 0; row < gridHeight; row++) {
      for (int col = 0; col < gridWidth; col++) {
        grid[row][col] = 0;
      }
    }


    for (var point in settledBlocks) {
      if (point.y >= 0 && point.y < gridHeight && point.x >= 0 &&
          point.x < gridWidth) {
        grid[point.y][point.x] = 2;
      }
    }


    if (currentTetromino == null) return;


    for (var point in currentTetromino!.getBlocks(0, 0, 0)) {
      if (point.y >= 0 && point.y < gridHeight && point.x >= 0 &&
          point.x < gridWidth) {
        grid[point.y][point.x] = 1;
      }
    }


    onGridUpdated();
  }
}

