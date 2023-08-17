package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa04.client.controller.BattleSalvoController;
import cs3500.pa04.client.model.GamePlayer;
import cs3500.pa04.client.view.BattleSalvoView;
import java.io.StringReader;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the BattleSalvoController class.
 */
public class BattleSalvoControllerTest {
  BattleSalvoController controller;
  BattleSalvoView view;

  /**
   * Actions to perform before every test.
   */
  @BeforeEach
  public void setup() {
    GamePlayer.setRandSeed(1234567);
  }

  /**
   * Make sure a normal, valid run of the game works.
   */
  @Test
  public void runNoErrorsTest() {
    StringBuilder inputs = new StringBuilder();
    // To get board dimensions.
    inputs.append("6 6\n");
    // To get fleet.
    inputs.append("2 1 2 1\n");
    // To get shot coordinates.
    inputs.append("0 0\n2 0\n3 0\n4 0\n5 0\n0 1\n");
    inputs.append("2 1\n3 1\n4 1\n5 1\n0 2\n1 2\n");
    inputs.append("2 2\n3 2\n4 2\n5 2\n0 3\n1 3\n");
    inputs.append("2 3\n3 3\n4 3\n5 3\n0 4\n1 4\n");
    inputs.append("3 4\n4 4\n3 5\n4 5\n2 2\n5 0");
    view = new BattleSalvoView(new StringReader(inputs.toString()));
    controller = new BattleSalvoController(view);
    controller.run();
    assertEquals("GAME END :D\nYou won.", view.getConsoleOutput());
  }

  /**
   * Game where no arguments are given.
   */
  @Test
  public void runNoArgsTest() {
    StringBuilder inputs = new StringBuilder();
    view = new BattleSalvoView(new StringReader(inputs.toString()));
    controller = new BattleSalvoController(view);
    assertThrows(NoSuchElementException.class, () -> controller.run());
  }

  /**
   * Game where some invalid arguments are given, but
   * it does end successfully.
   */
  // Invalid arguments
  @Test
  public void runInvalidArgsTest() {
    StringBuilder inputs = new StringBuilder();
    // To get board dimensions.
    inputs.append("6 18\n");  // Input out of bounds
    inputs.append("0 12\n");
    inputs.append("18 12\n");
    inputs.append("6 0\n");
    inputs.append("6   6\n"); // Too many spaces in between inputs
    inputs.append("6 6\n");
    // To get fleet.
    inputs.append("2 1 2 2\n"); // Input sum greater than allotted fleet
    inputs.append("Test\n"); // Not an integer
    inputs.append("2 1 2 -1\n"); // Input out of bounds
    inputs.append("2 1 2 1\n");
    // To get shot coordinates.
    inputs.append("0 10\n"); // Input out of bounds
    inputs.append("-1 10\n");
    inputs.append("10 -1\n");
    inputs.append("Test\n"); // Not an integer
    inputs.append("0 0\n2 0\n3 0\n4 0\n5 0\n0 1\n");
    inputs.append("2 1\n3 1\n4 1\n5 1\n0 2\n1 2\n");
    inputs.append("2 2\n3 2\n4 2\n5 2\n0 3\n1 3\n");
    inputs.append("2 3\n3 3\n4 3\n5 3\n0 4\n1 4\n");
    inputs.append("3 4\n4 4\n3 5\n4 5\n2 2\n5 0");
    // Run the controller.
    view = new BattleSalvoView(new StringReader(inputs.toString()));
    controller = new BattleSalvoController(view);
    controller.run();
    assertEquals("GAME END :D\nYou won.", view.getConsoleOutput());
  }
}
