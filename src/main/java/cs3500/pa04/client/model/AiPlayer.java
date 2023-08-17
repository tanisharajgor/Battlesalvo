package cs3500.pa04.client.model;

import cs3500.pa04.client.view.BattleSalvoView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an AI player in a game of BattleSalvo.
 */
public class AiPlayer extends GamePlayer implements Player {
  char[][] board;
  int height;
  int width;
  BattleSalvoView view;
  List<Coord> allShotsTaken;
  Coord prevShot;
  Coord shotToLookFor;

  /**
   * Constructs an instance of an AiPlayer.
   */
  public AiPlayer() {
    allShotsTaken = new ArrayList<>();
  }

  /**
   * Get the player's name.
   *
   * @return - the player's name
   */
  @Override
  public String name() {
    return "AI Player";
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         - the height of the board, range: [6, 15] inclusive
   * @param width          - the width of the board, range: [6, 15] inclusive
   * @param specifications - a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return - the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    Board placedBoard = setUpBoard(board, height, width, specifications);
    this.height = height;
    this.width = width;
    saveBoard(placedBoard);
    return placedBoard.getShips();
  }

  /**
   * Saves the current state of a given board.
   *
   * @param board - the board to save
   */
  private void saveBoard(Board board) {
    BoardManager.setBoard2(board);
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
    Board board = record.getBoard2();
    Board opponentRecord = record.getOpponentRecord();
    int numShots = Math.min(GamePlayer.getNumOppoCoordLeft(), board.numShipsRemaining());
    List<Coord> shotCoordinates = new ArrayList<>();
    List<Ship> fleet = board.getShips();
    int range = fleet.size();
    while (shotCoordinates.size() != numShots) {
      int row;
      int col;
      if (shotToLookFor == null) {
        row = generateRandomInt(height);
        col = generateRandomInt(width);
      } else {
        row = prevShot.getY();
        col = prevShot.getX();
      }
      // Attempt to get a new ship each time.
      Ship ship = fleet.get(generateRandomInt(range));
      Coord shot = new Coord(col, row);
      if (opponentRecord.getRawBoard()[row][col] == 'H') {
        if (col + ship.getSize() <= opponentRecord.getRawBoard()[0].length) {
          shot = new Coord(col + 1, row);
        } else if (row - ship.getSize() >= -1) {
          shot = new Coord(col, row - 1);
        } else if (col - ship.getSize() >= -1) {
          shot = new Coord(col - 1, row);
        } else if (row + ship.getSize() <= opponentRecord.getRawBoard().length) {
          shot = new Coord(col, row + 1);
        }
        // If none of these work, then go to another random area with no hit.
      }
      if (!contains(shotCoordinates, shot) && !contains(allShotsTaken, shot)) {
        shotCoordinates.add(shot);
        prevShot = shot;
      }
    }
    allShotsTaken.addAll(shotCoordinates);
    return shotCoordinates;
  }

  /**
   * Checks if the specified coordinate is in the given list.
   *
   * @return - if the list contains the coordinate
   */
  private boolean contains(List<Coord> list, Coord coordToCheck) {
    for (Coord coord : list) {
      if (coord.getX() == coordToCheck.getX() && coord.getY() == coordToCheck.getY()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard - the opponent's shots on this player's board
   * @return - a filtered list of the given shots that contain all locations of shots that hit a
   * ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> damage = new ArrayList<>();
    BoardManager record = new BoardManager();
    char[][] board2 = record.getBoard2().getRawBoard();
    for (Coord shot : opponentShotsOnBoard) {
      if (List.of('C', 'B', 'D', 'S', 'H').contains(
          board2[shot.getY()][shot.getX()])) {
        damage.add(shot);
        board2[shot.getY()][shot.getX()] = 'H';
      } else {
        board2[shot.getY()][shot.getX()] = 'M';
      }
    }
    record.getBoard2().updateRawBoard(board2);
    return damage;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips - the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    // No need to display to AI user. This will be used for extension purposes in the
    // later assignments (ex: ConsolePlayer vs. ConsolePlayer).
//     view.displayLines("You hit:" + shotsThatHitOpponentShips, true);
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result - if the player has won, lost, or forced a draw
   * @param reason - the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    view = GamePlayer.getView();
    view.displayLines(result.getServerEndState() + "\n" + reason, false);
  }
}
