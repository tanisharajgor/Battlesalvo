package cs3500.pa04.client.model;

/**
 * The ending state of a BattleSalvo game.
 */
public enum EndState {
  WIN("You won."),
  LOSE("You lost."),
  DRAW("There was a draw.");

  private final String statement;

  /**
   * Creates a new EndState instance.
   */
  EndState(String statement) {
    this.statement = statement;
  }

  /**
   * Get the statement associated with this end state.
   */
  public String getStatement() {
    return statement;
  }
}
