package cs3500.pa04.client.model;

/**
 * The direction/orientation of a ship.
 */
public enum Direction {
  VERTICAL("VERTICAL"),
  HORIZONTAL("HORIZONTAL");

  private final String label;

  /**
   * Creates a new Direction instance.
   */
  Direction(String label) {
    this.label = label;
  }

  /**
   * Returns the label associated with this direction.
   */
  public String getLabel() {
    return label;
  }

}
