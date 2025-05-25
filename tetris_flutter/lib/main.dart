import 'package:flutter/material.dart';

import 'game_logic.dart';

void main() {
  runApp(const TetrisApp());
}

class TetrisApp extends StatelessWidget {
  const TetrisApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Tetris',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: const TetrisGame(),
    );
  }
}

class TetrisGame extends StatefulWidget {
  const TetrisGame({super.key});

  @override
  _TetrisGameState createState() => _TetrisGameState();
}

class _TetrisGameState extends State<TetrisGame> {
  late GameLogic gameLogic;

  void _showGameOverDialog() {
    showDialog(
        context: context,
        builder: (BuildContext context) =>
            AlertDialog(
              title: Text("Game Over"),
              content: Text("Restart game?"),
              actions: [
                TextButton(
                    onPressed: () {
                      setState(() {
                        gameLogic.restartGame();
                      });
                      Navigator.of(context).pop();
                    },
                    child: Text("Restart"))
              ],
            ));
  }

  @override
  void initState() {
    super.initState();
    gameLogic = GameLogic(onGridUpdated: () {
      setState(() {});
    }, onGameOver: _showGameOverDialog);
  }

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery
        .of(context)
        .size
        .width;
    final double blockSize = (screenWidth / 10).clamp(20.0, 50.0);

    return Scaffold(
      appBar: AppBar(title: const Text("Tetris")),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: List.generate(GameLogic.gridHeight, (row) {
              return Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(GameLogic.gridWidth, (col) {
                  return Container(
                    width: blockSize,
                    height: blockSize,
                    decoration: BoxDecoration(
                      color: () {
                        switch (gameLogic.grid[row][col]) {
                          case 1:
                            return Colors.green;
                          case 2:
                            return Colors.red;
                          default:
                            return Colors.black;
                        }
                      }(),
                      border: Border.all(color: Colors.white, width: 1),
                    ),
                  );
                }),
              );
            }),
          ),
          const SizedBox(height: 20),
          ElevatedButton(
            onPressed: gameLogic.restartGame,
            child: const Text("Restart"),
          ),
        ],
      ),
    );
  }
}
