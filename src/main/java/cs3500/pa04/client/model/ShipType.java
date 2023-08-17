package cs3500.pa04.client.model;

/**
 * Represents the different types of ships in BattleSalvo.
 */
public enum ShipType {
  SUBMARINE(3, "Submarine", 'S'),
  DESTROYER(4, "Destroyer", 'D'),
  BATTLESHIP(5, "Battleship", 'B'),
  CARRIER(6, "Carrier", 'C');

  private final int size;
  private final String name;
  private final char tag;

  /**
   * Creates a new instance of ShipType
   *
   * @param size - the size of this ship type
   * @param name - the name of this ship type
   * @param tag - the single character for this ship type
   */
  ShipType(int size, String name, char tag) {
    this.size = size;
    this.name = name;
    this.tag = tag;
  }

  /**
   * Get the size of this ship type.
   *
   * @return - the size
   */
  public int getSize() {
    return size;
  }

  /**
   * Get the name of this ship type.
   *
   * @return - the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the tag of this ship type.
   *
   * @return - the tag
   */
  public char getTag() {
    return tag;
  }
}
