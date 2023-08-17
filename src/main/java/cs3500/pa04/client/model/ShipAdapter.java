package cs3500.pa04.client.model;


/**
 * Stores ship data.
 */
public class ShipAdapter {
  private Coord coord;
  private int length;
  private Direction direction;

  /**
   * Constructs a new instance of ShipAdapter.
   *
   * @param ship - the ship being stored
   */
  public ShipAdapter(Ship ship) {
    this.coord = ship.getPlacement().get(0);
    this.length = ship.getSize();
    this.direction = ship.getDirection();
  }

  /**
   * Gets the coord location of the ship.
   *
   * @return - the coord stored for the ship
   */
  public Coord getCoord() {
    return coord;
  }

  /**
   * Gets the length of the ship stored.
   *
   * @return - the length of the ship
   */
  public int getLength() {
    return length;
  }

  /**
   * Gets the direction of the ship stored.
   *
   * @return - the direction the ship is in
   */
  public Direction getDirection() {
    return direction;
  }
}
