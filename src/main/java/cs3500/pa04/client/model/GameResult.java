package cs3500.pa04.client.model;

/**
 * Determines whether the current game of BattleSalvo is over.
 */
public class GameResult {
  EndState result;

  /**
   * Constructs a new instance of GameResult
   */
  public GameResult() {
    this.result = null;
  }

  /**
   * Constructs a new instance of GameResult with the passed result
   *
   * @param result - the end result for the game
   */
  public GameResult(EndState result) {
    this.result = result;
  }

  /**
   * Check whether the game is over.
   *
   * @param board1 - player one's board
   * @param board2 - player two's board
   */
  public boolean isGameOver(Board board1, Board board2) {
    // Game is over when ConsolePlayer returns an empty shot list.
    return board1.numShipsRemaining() == 0 || board2.numShipsRemaining() == 0;
  }

  /**
   * Get the end state of the game (whether player one
   * wins, loses, or forces a draw).
   *
   * @param player1NumShipsRem - the number of ships remaining for player one
   * @param player2NumShipsRem - the number of ships remaining for player two
   */
  public EndState getEndState(int player1NumShipsRem, int player2NumShipsRem) {
    if (player1NumShipsRem < player2NumShipsRem) {
      return EndState.LOSE;
    } else if (player1NumShipsRem > player2NumShipsRem) {
      return EndState.WIN;
    } else {
      return EndState.DRAW;
    }
  }

  /**
   * Gets the end state for the server.
   *
   * @return - game result for the server
   */
  public EndState getServerEndState() {
    return result;
  }
}
