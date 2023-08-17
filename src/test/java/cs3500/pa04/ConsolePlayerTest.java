package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.client.model.ConsolePlayer;
import cs3500.pa04.client.model.Coord;
import cs3500.pa04.client.model.GamePlayer;
import cs3500.pa04.client.model.Player;
import cs3500.pa04.client.view.BattleSalvoView;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ConsolePlayer class.
 */
public class ConsolePlayerTest {
  Player player;
  BattleSalvoView view;
  List<Coord> shotsThatHitOpponentShips;

  /**
   * Actions to perform before every test.
   */
  @BeforeEach
  public void setup() {
    shotsThatHitOpponentShips = new ArrayList<>();
    view = new BattleSalvoView(new StringReader("")); // Just a dummy value.
    player = new ConsolePlayer();
  }

  /**
   * Test the reporting of a player's successful hits.
   */
  @Test
  public void successfulHitsTest() {
    shotsThatHitOpponentShips.add(new Coord(3, 4));
    shotsThatHitOpponentShips.add(new Coord(4, 5));
    GamePlayer.setView(new BattleSalvoView());
    player.successfulHits(shotsThatHitOpponentShips);
    assertEquals("You hit:[(3, 4), (4, 5)]\n"
        + "--------------------------------------------", view.getConsoleOutput());
  }

  /**
   * Validate the behavior of name().
   */
  @Test
  public void nameTest() {
    assertEquals("Console Player", player.name());
  }
}
