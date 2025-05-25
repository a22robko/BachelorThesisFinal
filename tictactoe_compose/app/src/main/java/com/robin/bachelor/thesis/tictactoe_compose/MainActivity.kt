package com.robin.bachelor.thesis.tictactoe_compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Scaffold(
        topBar = {
          TopAppBar(
            title = { Text("Tic Tac Toe") },
            colors = TopAppBarDefaults.topAppBarColors(
              containerColor = MaterialTheme.colorScheme.primary,
              titleContentColor = Color.White
            )
          )
        }
      ) {
        TicTacToeScreen()
      }
    }
  }
}

@Preview
@Composable
fun TicTacToeScreen() {
  val game by remember { mutableStateOf(GameLogic()) }
  val ai by remember { mutableStateOf(AI()) }
  var gameOver by remember { mutableStateOf(false) }
  var isAiMode by remember { mutableStateOf(false) }

  if (gameOver) {
    AlertDialog(
      onDismissRequest = { gameOver = false },
      title = { Text("Game Over") },
      text = { Text(if (game.checkWin()) "${game.currentPlayer} Wins!" else "Draw") },
      confirmButton = {
        Button(onClick = {
          game.resetGame()
          gameOver = false
        }) {
          Text("Restart")
        }
      }
    )
  }

  Column(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = "Current Player: ${game.currentPlayer}", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

    for (row in 0 until 3) {
      Row {
        for (col in 0 until 3) {
          TicTacToeCell(game.board[row][col], gameOver) {
            if (!gameOver && game.makeMove(row, col)) {
              gameOver = game.checkWin() || game.isDraw()
            }
          }
        }
      }
    }

    LaunchedEffect(game.currentPlayer) {
      if (isAiMode && game.currentPlayer == "O") {
        delay(500)
        val aiMove = ai.getBestMove(game.board)
        aiMove?.let {
          game.makeMove(it.first, it.second)
          gameOver = game.checkWin() || game.isDraw()
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Button(onClick = {
      isAiMode = !isAiMode
      game.resetGame()
      gameOver = false
    }) {
      Text(if (isAiMode) "Switch to 2 Players" else "Switch to AI Mode")
    }
  }
}

@Composable
fun TicTacToeCell(value: String, gameOver: Boolean, onClick: () -> Unit) {
  val imageRes = when (value) {
    "X" -> R.drawable.x
    "O" -> R.drawable.o
    else -> null
  }

  Box(
    modifier = Modifier
      .size(100.dp)
      .padding(5.dp)
      .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
      .clickable(enabled = !gameOver && value.isEmpty()) { onClick() },
    contentAlignment = Alignment.Center
  ) {
    imageRes?.let {
      Image(
        painter = painterResource(id = it),
        contentDescription = value,
        modifier = Modifier.size(80.dp)
      )
    }
  }
}
