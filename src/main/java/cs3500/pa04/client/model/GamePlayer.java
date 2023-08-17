package cs3500.pa04.client.model;

import cs3500.pa04.client.view.BattleSalvoView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Abstract class to hold common functionality between both
 * types of players.
 */
public abstract class GamePlayer {
  private static Random rand = new Random();
  private static BattleSalvoView view;
  private static int numOppoCoordLeft;

  /**
   * Given the specifications for a BattleSalvo board, set up the board and return in.
   *
   * @param board          - the raw board to set up
   * @param height         - the height of the board, range: [6, 15] inclusive
   * @param width          - the width of the board, range: [6, 15] inclusive
   * @param specifications - a map of ship type to the number of occurrences each ship
   *                       should appear on the board
   * @return - the placements of each ship on the board
   */
  public Board setUpBoard(char[][] board, int height, int width,
                          Map<ShipType, Integer> specifications) {
    // Should automatically place ships on its own board and the player's board.
    board = new char[height][width];
    for (char[] row : board) {
      Arrays.fill(row, '0');
    }
    List<Ship> ships = new ArrayList<>();
    Direction shipDirection;
    List<Coord> shipPlacement = null;

    for (Map.Entry<ShipType, Integer> entry : specifications.entrySet()) {
      for (int i = 0; i < entry.getValue(); i++) {
        Ship ship = new Ship(entry.getKey());
        while (shipPlacement == null) {
          shipDirection = generateRandomInt(2) == 0 ? Direction.HORIZONTAL : Direction.VERTICAL;
          if (shipDirection == Direction.HORIZONTAL) {
            int row = generateRandomInt(board.length);
            int startCol = generateRandomInt(board[0].length - ship.getSize());
            shipPlacement = placeShipHorizontally(board, ship, row, startCol);
          } else {
            int col = generateRandomInt(board[0].length);
            int startRow = generateRandomInt(board.length - ship.getSize());
            shipPlacement = placeShipVertically(board, ship, col, startRow);
          }
        }
        ship.setPlacement(shipPlacement);
        ships.add(ship);
        shipPlacement = null; // set to null again to repeat
      }
    }
    return new Board(board, ships);
  }

  /**
   * Place a ship on the board and set its placement coordinates.
   *
   * @param board    - the board to place ship on
   * @param ship     - the ship to place
   * @param row      - the row index to place this ship
   * @param startCol - the starting column to place the ship
   * @return - the list of coordinates where this ship is placed.
   */
  private List<Coord> placeShipHorizontally(char[][] board, Ship ship, int row, int startCol) {
    List<Coord> placement = new ArrayList<>();
    // Check if the placement will cause an overlap.
    for (int i = startCol; i < startCol + ship.getSize(); i++) {
      if (board[row][i] != '0') {
        return null;
      }
    }
    for (int i = startCol; i < startCol + ship.getSize(); i++) {
      board[row][i] = ship.getType().getTag();
      placement.add(new Coord(i, row));
    }
    return placement;
  }

  /**
   * Place a ship on the board and set its placement coordinates.
   *
   * @param board    - the board to place ship on
   * @param ship     - the ship to place
   * @param col      - the col index to place this ship
   * @param startRow - the starting row to place the ship
   * @return - the list of coordinates where this ship is placed.
   */
  private List<Coord> placeShipVertically(char[][] board, Ship ship, int col, int startRow) {
    List<Coord> placement = new ArrayList<>();
    // Check if the placement will cause an overlap.
    for (int i = startRow; i < startRow + ship.getSize(); i++) {
      if (board[i][col] != '0') {
        return null;
      }
    }
    for (int i = startRow; i < startRow + ship.getSize(); i++) {
      board[i][col] = ship.getType().getTag();
      placement.add(new Coord(col, i));
    }
    return placement;
  }

  /**
   * Generate a random integer, from 0 up until the specified max.
   *
   * @param max - the maximum the integer can be
   * @return - the integer generated
   */
  public int generateRandomInt(int max) {
    if (max == 0) {
      return 0;
    }
    return rand.nextInt(max);
  }

  /**
   * Initialize a random number generator with the specified seed.
   *
   * @param seed - the seed to initialize this random instance with
   */
  public static void setRandSeed(long seed) {
    rand = new Random(seed);
  }

  /**
   * Initialize an instance of the BattleSalvoView to use
   * within ConsolePlayer.
   *
   * @param view - the instance to initialize with
   */
  public static void setView(BattleSalvoView view) {
    GamePlayer.view = view;
  }

  /**
   * Return an instance of the view. Primarily for
   * the ConsolePlayer class.
   *
   * @return - instance of the view
   */
  public static BattleSalvoView getView() {
    return view;
  }

  /**
   * Sets value numCoords as how many coordinates are available on the opponent's board.
   *
   * @param numCoords - number of coordinates left on the opponent's board
   */
  public static void setNumOppoCoordLeft(int numCoords) {
    numOppoCoordLeft = numCoords;
  }

  /**
   * Gets how many coordinates are available on the opponent's board.
   *
   * @return - number of coordinates left on the opponent's board
   */
  public static int getNumOppoCoordLeft() {
    return numOppoCoordLeft;
  }
}


