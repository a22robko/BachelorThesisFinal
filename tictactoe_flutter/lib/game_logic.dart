class GameLogic {
  List<List<String>> board = List.generate(3, (_) => List.filled(3, "", growable: false));
  String currentPlayer = "X";

  bool makeMove(int row, int col) {
    if (board[row][col].isEmpty) {
      board[row][col] = currentPlayer;
      if (checkWin() || isDraw()) {
        return true;
      }
      _switchPlayer();
    }
    return false;
  }

  void _switchPlayer() {
    currentPlayer = (currentPlayer == "X") ? "O" : "X";
  }

  bool checkWin() {
    for (int i = 0; i < 3; i++) {
      if (_isSame(board[i][0], board[i][1], board[i][2])) return true;
      if (_isSame(board[0][i], board[1][i], board[2][i])) return true;
    }
    if (_isSame(board[0][0], board[1][1], board[2][2])) return true;
    if (_isSame(board[0][2], board[1][1], board[2][0])) return true;
    return false;
  }

  bool isDraw() {
    for (var row in board) {
      if (row.contains("")) return false;
    }
    return !checkWin();
  }

  void resetGame() {
    board = List.generate(3, (_) => List.filled(3, "", growable: false));
    currentPlayer = "X";
  }

  bool _isSame(String a, String b, String c) {
    return a.isNotEmpty && a == b && b == c;
  }
}
