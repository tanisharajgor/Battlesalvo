package cs3500.pa04.client.model;

import java.util.List;

/**
 * Represents a board in the BattleSalvo game.
 */
public class Board {
  private char[][] board;
  private final List<Ship> ships;

  /**
   * Creates a new Board instance.
   *
   * @param board - the raw board
   * @param ships - list of ships that belong to this board
   */
  public Board(char[][] board, List<Ship> ships) {
    this.board = board;
    this.ships = ships;
  }

  /**
   * Creates a new Board instance.
   *
   * @param board - the raw board
   */
  public Board(char[][] board) {
    this.board = board;
    this.ships = null;
  }

  /**
   * Updates the raw board.
   *
   * @param board - the updated version of the raw board
   */
  public void updateRawBoard(char[][] board) {
    this.board = board;
  }

  /**
   * Get the current raw board.
   *
   * @return - the raw board
   */
  public char[][] getRawBoard() {
    return board;
  }

  /**
   * Get the list of ships associated with this board.
   *
   * @return - the list of ships
   */
  public List<Ship> getShips() {
    return ships;
  }

  /**
   * Determine the number of ships remaining (not sunk)
   * on this board.
   *
   * @return - the number of ships not sunk
   */
  public int numShipsRemaining() {
    int numShips = 0;
    for (Ship ship : ships) {
      if (!isShipSunk(ship)) {
        numShips++;
      }
    }
    return numShips;
  }

  /**
   * Determine if the given ship has sunk.
   *
   * @param ship - the ship to check for
   * @return - whether the ship is sunk
   */
  public boolean isShipSunk(Ship ship) {
    for (Coord coord : ship.getPlacement()) {
      if (board[coord.getY()][coord.getX()] != 'H') {
        return false;
      }
    }
    return true;
  }

  /**
   * Determine the number of non-guessed coordinates
   * for this board.
   *
   * @return - the number of non-guessed coordinates
   */
  public int numNonGuessedCoord() {
    int num = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (!List.of('H', 'M').contains(board[i][j])) {
          num++;
        }
      }
    }
    return num;
  }
}
