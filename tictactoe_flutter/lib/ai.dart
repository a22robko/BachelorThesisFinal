import 'dart:math';

class AI {
  static final _random = Random();

  static List<int>? getBestMove(List<List<String>> board) {
    String aiPlayer = "O"; // AI plays as "O"
    String humanPlayer = "X"; // Human player is "X"

    // 1. Check if AI can win directly
    List<int>? winningMove = _findWinningMove(board, aiPlayer);
    if (winningMove != null) return winningMove;

    // 2. Check if the opponent can win in the next move, and block them
    List<int>? blockingMove = _findWinningMove(board, humanPlayer);
    if (blockingMove != null) return blockingMove;

    // 3. Otherwise, choose the best available move (center > corners > random)
    return _findBestMove(board);
  }

  /// Check if there is a winning move for a given player
  static List<int>? _findWinningMove(List<List<String>> board, String player) {
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        if (board[row][col] == "") { // If the cell is empty
          board[row][col] = player; // Simulate the move
          bool isWinning = _checkWin(board, player);
          board[row][col] = ""; // Restore the board
          if (isWinning) return [row, col]; // Return the winning move
        }
      }
    }
    return null;
  }

  /// Find the best strategic move
  static List<int>? _findBestMove(List<List<String>> board) {
    // Prioritize the center position
    if (board[1][1] == "") return [1, 1];

    // Then prioritize corner positions
    List<List<int>> corners = [[0, 0], [0, 2], [2, 0], [2, 2]];
    List<List<int>> availableCorners = corners.where((pos) => board[pos[0]][pos[1]] == "").toList();
    if (availableCorners.isNotEmpty) {
      return availableCorners[_random.nextInt(availableCorners.length)];
    }

    // Finally, choose any remaining available move randomly
    List<List<int>> availableMoves = [];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] == "") availableMoves.add([i, j]);
      }
    }
    return availableMoves.isNotEmpty ? availableMoves[_random.nextInt(availableMoves.length)] : null;
  }

  /// Check if a player has won the game
  static bool _checkWin(List<List<String>> board, String player) {
    for (int i = 0; i < 3; i++) {
      if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true; // Horizontal
      if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true; // Vertical
    }
    if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true; // Main diagonal
    if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true; // Anti-diagonal
    return false;
  }
}
