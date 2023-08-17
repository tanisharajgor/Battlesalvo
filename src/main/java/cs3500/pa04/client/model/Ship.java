package cs3500.pa04.client.model;

import java.util.List;

/**
 * Represents a ship in a game of BattleSalvo.
 */
public class Ship {
  private ShipType type;
  private int size;
  // List of coordinates that this ship takes up.
  private List<Coord> placement;
  private Direction direction;

  /**
   * Creates an instance of a ship.
   *
   * @param type - type of ship
   */
  public Ship(ShipType type) {
    this.type = type;
    this.size = type.getSize();
  }

  /**
   * Gets the type of this ship.
   *
   * @return - type of ship
   */
  public ShipType getType() {
    return type;
  }

  /**
   * Gets the size of this ship
   *
   * @return - size of ship
   */
  public int getSize() {
    return size;
  }

  /**
   * Sets the list of coordinates that this ship is placed in.
   *
   * @param placement - the list of coordinates to set
   */
  public void setPlacement(List<Coord> placement) {
    this.placement = placement;
    for (int i = 0; i < placement.size() - 1; i++) {
      if (placement.get(i).getX() != placement.get(i + 1).getX()) {
        direction = Direction.HORIZONTAL;
      } else {
        direction = Direction.VERTICAL;
      }
    }
  }

  /**
   * Gets the list of coordinates that this ship is placed on.
   *
   * @return - the list of placement coordinates
   */
  public List<Coord> getPlacement() {
    return placement;
  }

  /**
   * Set the direction of this ship to the given direction.
   *
   * @param direction - the direction to set for this ship
   */
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  /**
   * Gets the direction that this ship is oriented in.
   *
   * @return - the orientation of this ship
   */
  public Direction getDirection() {
    return direction;
  }
}
