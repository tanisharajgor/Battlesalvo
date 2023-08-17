package cs3500.pa04;

import cs3500.pa04.client.model.ConsolePlayer;
import cs3500.pa04.client.model.Player;
import cs3500.pa04.client.model.ShipType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the GamePlayer class.
 */
public class GamePlayerTest {
  Player player;
  char[][] board;
  Map<ShipType, Integer> specifications;
  int height;
  int width;

  /**
   * Actions to perform before the start of every test.
   */
  @BeforeEach
  public void setup() {
    specifications = new HashMap<>();
    height = 6;
    width = 6;
    board = new char[height][width];
    player = new ConsolePlayer();
  }

  /**
   * Test to validate board set up.
   */
  @Test
  public void setUpBoardTest() {
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    player.setup(height, width, specifications);
  }
}
