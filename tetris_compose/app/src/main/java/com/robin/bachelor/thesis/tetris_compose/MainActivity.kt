package com.robin.bachelor.thesis.tetris_compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.focusable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalConfiguration

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
        TetrisScreen()
      }
    }
  }
}

@Preview
@Composable
fun TetrisScreen() {
  val gameLogic by remember { mutableStateOf(GameLogic()) }
  val focusRequester = remember { FocusRequester() }

  val screenWidth = LocalConfiguration.current.screenWidthDp
  val blockSize = screenWidth / 10.0f

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  if (gameLogic.gameOver) {
    AlertDialog(
      onDismissRequest = { gameLogic.restartGame() },
      title = { Text("Game Over") },
      text = { Text("Restart?") },
      confirmButton = {
        Button(onClick = {
          gameLogic.restartGame()
        }) {
          Text("Restart")
        }
      }
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .focusRequester(focusRequester)
      .focusable()
      .onKeyEvent { event ->
        if (event.type == KeyEventType.KeyDown) {
          when (event.key) {
            Key.DirectionLeft -> gameLogic.moveLeft()
            Key.DirectionRight -> gameLogic.moveRight()
            Key.DirectionUp -> gameLogic.rotate()
            Key.DirectionDown -> gameLogic.moveDown()
          }
        }
        true
      },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Column(
      modifier = Modifier
        .border(1.dp, Color.White)
    ) {
      gameLogic.grid.forEach { row ->
        Row {
          row.forEach { cellValue ->
            val color = when (cellValue) {
              1 -> Color.Green
              2 -> Color.Red
              else -> Color.Black
            }
            Box(
              modifier = Modifier
                .size(blockSize.dp)
                .background(color)
                .border(1.dp, Color.White)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = { gameLogic.restartGame() }) {
      Text("Restart Game")
    }
  }
}
