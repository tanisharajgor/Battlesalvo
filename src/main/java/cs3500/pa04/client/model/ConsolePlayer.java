package cs3500.pa04.client.model;

import cs3500.pa04.client.view.BattleSalvoView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents a human player/user in a game of BattleSalvo.
 */
public class ConsolePlayer extends GamePlayer implements Player {
  private char[][] board;
  private char[][] opponentRecord;

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "Console Player";
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    Board placedBoard = setUpBoard(board, height, width, specifications);
    saveBoard(placedBoard);
    setOppoRecord(height, width);
    return placedBoard.getShips();
  }

  /**
   * Saves the current state of a given board.
   *
   * @param board - the board to save
   */
  private void saveBoard(Board board) {
    BoardManager.setBoard1(board);
  }

  /**
   * Sets the record of the opponent's board.
   *
   * @param height - the height of the board
   * @param width - the width of the board
   */
  private void setOppoRecord(int height, int width) {
    opponentRecord = new char[height][width];
    for (char[] row : opponentRecord) {
      Arrays.fill(row, '0');
    }
    BoardManager.setOpponentRecord(new Board(opponentRecord));
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    BoardManager record = new BoardManager();
    Board board = record.getBoard1();
    final List<Coord> shotCoordinates = new ArrayList<>();
    int numShots = Math.min(board.numNonGuessedCoord(), board.numShipsRemaining());
    getView().displayLines(String.format("Please enter %d shots:", numShots), true);
    while (shotCoordinates.size() != numShots) {
      List<Integer> coordinates = new ArrayList<>();
      while (coordinates.size() != 2) {
        String[] inputs = getView().read().split(" ");
        try {
          for (String input : inputs) {
            coordinates.add(Integer.parseInt((input)));
          }
          // If the inputs are out of bounds.
          if (coordinates.get(0) < 0 || coordinates.get(0) >= board.getRawBoard()[0].length
              || coordinates.get(1) < 0 || coordinates.get(1) >= board.getRawBoard().length) {
            getView().displayLines(String.format("You've entered invalid shot coordinates.\n"
                + "Remember the first can't be more than %d and "
                + "the second can't be more than %d.",
                board.getRawBoard()[0].length, board.getRawBoard().length), true);
            coordinates.clear();
            // reset
            return takeShots();
          }
          // If there is a parsing error (number not entered).
        } catch (Exception e) {
          getView().displayLines(String.format("You've entered invalid shot coordinates.\n"
              + "Remember the first can't be more than %d and " + "the second can't be more "
              + "than %d.", board.getRawBoard()[0].length, board.getRawBoard().length), true);
          coordinates.clear();
          // reset
          return takeShots();
        }
      }
      shotCoordinates.add(new Coord(coordinates.get(0), coordinates.get((1))));
    }
    return shotCoordinates;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *        ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> damage = new ArrayList<>();
    BoardManager record = new BoardManager();
    char[][] board1 = record.getBoard1().getRawBoard();
    for (Coord shot : opponentShotsOnBoard) {
      if (List.of('C', 'B', 'D', 'S', 'H').contains(
          board1[shot.getY()][shot.getX()])) {
        damage.add(shot);
        board1[shot.getY()][shot.getX()] = 'H';
      } else {
        board1[shot.getY()][shot.getX()] = 'M';
      }
    }
    record.getBoard1().updateRawBoard(board1);
    return damage;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    getView().displayLines("You hit:" + shotsThatHitOpponentShips, true);
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    // future implementation
  }
}
