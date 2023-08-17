package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.client.Driver;
import cs3500.pa04.client.view.BattleSalvoView;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Driver class.
 */
class DriverTest {

  /**
   * Tests for the server mode, initiated by two command-line arguments.
   */
  @Test
  public void serverModeTest() {
    BattleSalvoView view = new BattleSalvoView();
    String[] args = {"0.0.0.0", "35001"};
    Driver.main(args);
    assertEquals("Unable to connect to the server.", view.getConsoleOutput());
  }

}