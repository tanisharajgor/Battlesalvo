package cs3500.pa04.client.model;

/**
 * Updates and manages board records, can also be used as an Adapter, for BattleSalvo.
 * Class which has some utility methods to store the states of the boards, but the players
 * can not save them themselves.
 */
public class BoardManager {
  // ConsolePlayer's Record
  private static Board board1;

  // AIPlayer's Record
  private static Board board2;

  // ConsolePlayer's View of AIPlayers Board (Current Data)
  // Also AiPlayer's record for the server mode
  private static Board opponentRecord;

  /**
   * Set the value of player one's board.
   *
   * @param board - the board to set to
   */
  public static void setBoard1(Board board) {
    board1 = board;
  }

  /**
   * Set the value of player two's board.
   *
   * @param board - the board to set to
   */
  public static void setBoard2(Board board) {
    board2 = board;
  }

  /**
   * Set the value of record of the opponent's board.
   *
   * @param record - the board to set to
   */
  public static void setOpponentRecord(Board record) {
    opponentRecord = record;
  }

  /**
   * Update the record of the opponent's board.
   */
  public void updateOpponentRecord() {
    // Get the missed shots.
    char[][] boardToUpdate = opponentRecord.getRawBoard();
    for (int i = 0; i < boardToUpdate.length; i++) {
      for (int j = 0; j < boardToUpdate.length; j++) {
        if (getBoard2().getRawBoard()[i][j] == 'H') {
          boardToUpdate[i][j] = 'H';
        } else if (getBoard2().getRawBoard()[i][j] == 'M') {
          boardToUpdate[i][j] = 'M';
        }
      }
    }
    opponentRecord.updateRawBoard(boardToUpdate);
  }

  /**
   * Retrieve the record of player one's board.
   *
   * @return player one's board record
   */
  public Board getBoard1() {
    return board1;
  }

  /**
   * Retrieve the record of player two's board.
   *
   * @return player two's board record
   */
  public Board getBoard2() {
    return board2;
  }

  /**
   * Retrieve the record of player one's view of the
   * opponent's board.
   *
   * @return opponent record
   */
  public Board getOpponentRecord() {
    return opponentRecord;
  }
}
