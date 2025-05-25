import 'package:flutter/material.dart';
import 'game_logic.dart';
import 'ai.dart';

void main() {
  runApp(TicTacToeApp());
}

class TicTacToeApp extends StatelessWidget {
  const TicTacToeApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Tic Tac Toe',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: const TicTacToeGame(),
    );
  }
}

class TicTacToeGame extends StatefulWidget {
  const TicTacToeGame({super.key});

  @override
  _TicTacToeGameState createState() => _TicTacToeGameState();
}

class _TicTacToeGameState extends State<TicTacToeGame> {
  GameLogic game = GameLogic();
  bool isAiMode = false;
  bool gameOver = false;

  void handleMove(int row, int col) {
    if (gameOver || game.board[row][col].isNotEmpty) return;

    setState(() {
      gameOver = game.makeMove(row, col);
    });

    if (gameOver) {
      _showGameOverDialog();
      return;
    }

    if (isAiMode && game.currentPlayer == "O") {
      Future.delayed(Duration(milliseconds: 500), () {
        var aiMove = AI.getBestMove(game.board);
        if (aiMove != null) {
          setState(() {
            gameOver = game.makeMove(aiMove[0], aiMove[1]);
          });
          if (gameOver) _showGameOverDialog();
        }
      });
    }
  }

  void _showGameOverDialog() {
    String message = game.checkWin() ? "${game.currentPlayer} Wins!" : "It's a Draw!";

    showDialog(
        context: context,
        builder: (BuildContext context) => AlertDialog(
          title: Text("Game Over"),
          content: Text(message),
          actions: [
            TextButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: Text("Restart"))
          ],
        ));
  }

  void _toggleMode() {
    setState(() {
      isAiMode = !isAiMode;
      game.resetGame();
      gameOver = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Tic Tac Toe")),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "Current Player: ${game.currentPlayer}",
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 20),
            _buildBoard(),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _toggleMode,
              child: Text(isAiMode ? "Switch to 2 Players" : "Switch to AI Mode"),
            ),
          ],
        ),
      );
  }

  Widget _buildBoard() {
    return Column(
      children: List.generate(3, (row) => Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: List.generate(3, (col) => _buildCell(row, col)),
      )),
    );
  }

  Widget _buildCell(int row, int col) {
    return GestureDetector(
      onTap: () => handleMove(row, col),
      child: Container(
        width: 100,
        height: 100,
        margin: EdgeInsets.all(5),
        decoration: BoxDecoration(
          color: Colors.blue[100],
          border: Border.all(color: Colors.black),
        ),
        child: Center(
          child: game.board[row][col] == ""
              ? null
              : Image.asset(
            game.board[row][col] == "X" ? "assets/images/x.png" : "assets/images/o.png",
            width: 80,
            height: 80,
          ),
        ),
      ),
    );
  }
}
