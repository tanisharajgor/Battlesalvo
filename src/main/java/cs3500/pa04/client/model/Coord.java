package cs3500.pa04.client.model;

/**
 * Represents a coordinate on a board.
 */
public class Coord {
  private final int xCoord;
  private final int yCoord;

  /**
   * Creates a new instance of a coordinate.
   *
   * @param x - the x coordinate
   * @param y - the y coordinate
   */
  public Coord(int x, int y) {
    this.xCoord = x;
    this.yCoord = y;
  }

  /**
   * Get the x coordinate of this coordinate.
   *
   * @return - x coordinate
   */
  public int getX() {
    return xCoord;
  }

  /**
   * Get the y coordinate of this coordinate.
   *
   * @return - y coordinate
   */
  public int getY() {
    return yCoord;
  }

  /**
   * Get the string representation of this coordinate.
   *
   * @return - the string representation (x,y)
   */
  public String toString() {
    return String.format("(%d, %d)", xCoord, yCoord);
  }
}
