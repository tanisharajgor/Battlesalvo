package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.client.model.EndState;
import cs3500.pa04.client.model.GameResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the GameResult class.
 */
public class GameResultTest {
  GameResult result;

  /**
   * Actions to perform before every test.
   */
  @BeforeEach
  public void setup() {
    result = new GameResult();
  }

  /**
   * Validate the determination of the end state of the game
   * based on each player's remaining number of ships.
   */
  @Test
  public void getEndStateTest() {
    assertEquals(EndState.LOSE, result.getEndState(0, 2));
    assertEquals(EndState.WIN, result.getEndState(3, 0));
    assertEquals(EndState.DRAW, result.getEndState(2, 2));
  }
}
