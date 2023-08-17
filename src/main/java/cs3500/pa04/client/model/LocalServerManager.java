package cs3500.pa04.client.model;

import cs3500.pa04.client.view.BattleSalvoView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Manages player board and opponent board on the local server.
 */
public class LocalServerManager {
  BoardManager record;
  Board opponentRecord;
  Player player;
  List<Coord> volley;
  List<Coord> allShotsTaken;

  /**
   * Constructs a new instance of LocalServerManager with the given player.
   *
   * @param player - the Player the server is managing
   */
  public LocalServerManager(Player player) {
    this.record = new BoardManager();
    this.player = player;
    this.allShotsTaken = new ArrayList<>();
    GamePlayer.setView(new BattleSalvoView());
  }

  /**
   * Sets the record of the opponent's board.
   *
   * @param height - the height of the board
   * @param width  - the width of the board
   */
  public void setOppoRecord(int height, int width) {
    char[][] opponentRawRecord = new char[height][width];
    for (char[] row : opponentRawRecord) {
      Arrays.fill(row, '0');
    }
    BoardManager.setOpponentRecord(new Board(opponentRawRecord));
  }

  /**
   * Update the record of the opponent's board.
   */
  public void updateOpponentRecord(List<Coord> hits) {
    // Get the missed shots.
    opponentRecord = record.getOpponentRecord();
    char[][] boardToUpdate = opponentRecord.getRawBoard();
    for (int i = 0; i < boardToUpdate.length; i++) {
      for (int j = 0; j < boardToUpdate[0].length; j++) {
        if (contains(hits, new Coord(j, i))) {
          boardToUpdate[i][j] = 'H';
        }
        if (contains(getMisses(hits), new Coord(j, i))) {
          boardToUpdate[i][j] = 'M';
        }
      }
    }
    opponentRecord.updateRawBoard(boardToUpdate);
  }

  /**
   * Returns a list of the specified ships.
   *
   * @param height    - board height
   * @param width     - board width
   * @param fleetSpec - how many of each ship type there is
   * @return - list of the ships on the board
   */
  public List<ShipAdapter> getFleet(int height, int width, Map<ShipType, Integer> fleetSpec) {
    List<ShipAdapter> fleet = new ArrayList<>();
    for (Ship ship : player.setup(height, width, fleetSpec)) {
      fleet.add(new ShipAdapter(ship));
    }
    return fleet;
  }

  /**
   * Returns a list of the shots taken.
   *
   * @return - the player's volley
   */
  public List<Coord> getVolley() {
    volley = player.takeShots();
    allShotsTaken.addAll(volley);
    return volley;
  }

  /**
   * Returns a list of the hit ships on this board.
   *
   * @param opponentShotsOnBoard - tiles the opponent shot at
   * @return - a list of the tiles that hit a ship
   */
  public List<Coord> getDamage(List<Coord> opponentShotsOnBoard) {
    return player.reportDamage(opponentShotsOnBoard);
  }

  /**
   * Ends the game with the specified game result and reason.
   *
   * @param result - whether the game was a win, a loss, or a tie
   * @param reason - why the game ended
   */
  public void reportEndState(GameResult result, String reason) {
    player.endGame(result, reason);
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
   * Returns the misses based on the given shots.
   *
   * @param hits - the shots that were taken
   * @return - the shots that missed a ship
   */
  private List<Coord> getMisses(List<Coord> hits) {
    List<Coord> misses = new ArrayList<>();
    for (Coord coord : volley) {
      if (!contains(hits, coord)) {
        misses.add(coord);
      }
    }
    return misses;
  }

  /**
   * Sets the number of coordinates unfired at on the opponent's board.
   */
  public void numNonGuessedOppoCoord() {
    int numCoords = 0;
    for (int i = 0; i < record.getBoard2().getRawBoard().length; i++) {
      for (int j = 0; j < record.getBoard2().getRawBoard()[0].length; j++) {
        if (!contains(allShotsTaken, new Coord(j, i))) {
          numCoords++;
        }
      }
    }
    GamePlayer.setNumOppoCoordLeft(numCoords);
  }
}
