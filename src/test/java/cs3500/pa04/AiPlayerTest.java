package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.client.model.AiPlayer;
import cs3500.pa04.client.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AIPlayer class.
 */
public class AiPlayerTest {
  Player player;

  /**
   * Actions to perform before every test.
   */
  @BeforeEach
  public void setup() {
    player = new AiPlayer();
  }

  /**
   * Validate the behavior of name().
   */
  @Test
  public void nameTest() {
    assertEquals("AI Player", player.name());
  }
}
