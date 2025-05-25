import 'dart:math';

class Tetromino {
  Point<int> position;
  int shape; // 0 = square, 1 = L-shape, 2 = T-shape
  int rotate; // 0, 1, 2, 3

  Tetromino({required this.position, required this.shape, required this.rotate});

  List<Point<int>> getBlocks(int dx, int dy, int dr) {
    int positionXNew = position.x + dx;
    int positionYNew = position.y + dy;
    int rotateNew = (rotate + dr) % 4;

    switch (shape) {
      case 0: // Square
        return [
          Point(positionXNew, positionYNew),
          Point(positionXNew + 1, positionYNew),
          Point(positionXNew, positionYNew + 1),
          Point(positionXNew + 1, positionYNew + 1)
        ];
      case 1: { // L-Shape
        switch(rotateNew) {
          case 0:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew + 1, positionYNew),
              Point(positionXNew - 1, positionYNew),
              Point(positionXNew - 1, positionYNew + 1)
            ];
          case 1:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew, positionYNew + 1),
              Point(positionXNew, positionYNew - 1),
              Point(positionXNew - 1, positionYNew -1)
            ];
          case 2:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew - 1, positionYNew),
              Point(positionXNew + 1, positionYNew),
              Point(positionXNew + 1, positionYNew - 1)
            ];
          case 3:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew, positionYNew - 1),
              Point(positionXNew, positionYNew + 1),
              Point(positionXNew + 1, positionYNew + 1)
            ];
          default:
            return [];
        }
      }
      case 2: { // T-Shape
        switch(rotateNew) {
          case 0:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew - 1, positionYNew),
              Point(positionXNew + 1, positionYNew),
              Point(positionXNew, positionYNew + 1)
            ];
          case 1:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew, positionYNew - 1),
              Point(positionXNew, positionYNew + 1),
              Point(positionXNew - 1, positionYNew)
            ];
          case 2:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew - 1, positionYNew),
              Point(positionXNew + 1, positionYNew),
              Point(positionXNew, positionYNew - 1)
            ];
          case 3:
            return [
              Point(positionXNew, positionYNew),
              Point(positionXNew, positionYNew - 1),
              Point(positionXNew, positionYNew + 1),
              Point(positionXNew + 1, positionYNew)
            ];
          default:
            return [];
        }
      }
      default:
        return [];
    }
  }

  void moveDown() {
    position = Point(position.x, position.y + 1);
  }

  void moveLeft() {
    position = Point(position.x - 1, position.y);
  }

  void moveRight() {
    position = Point(position.x + 1, position.y);
  }

  void rotateClockwise() {
    rotate = (rotate + 1) % 4;
  }
}