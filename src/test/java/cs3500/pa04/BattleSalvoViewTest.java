package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.client.model.Board;
import cs3500.pa04.client.view.BattleSalvoView;
import java.util.Arrays;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the BattleSalvoView class.
 */
public class BattleSalvoViewTest {
  BattleSalvoView view;

  /**
   * Actions to perform before each test.
   */
  @BeforeEach
  public void setup() {
    view = new BattleSalvoView();
  }

  /**
   * Test to display the given lines to the user.
   */
  @Test
  public void displayLinesTest() {
    view.displayLines("This is a test.", false);
    assertEquals("This is a test.", view.getConsoleOutput());
  }

  /**
   * Test to display the given board to the user.
   */
  @Test
  public void displayBoard() {
    char[][] board = new char[6][6];
    for (char[] row : board) {
      Arrays.fill(row, '0');
    }
    view.displayBoard(new Board(board, null));
    Scanner scanner = new Scanner(view.getConsoleOutput());
    while (scanner.hasNextInt()) {
      assertEquals(0, scanner.nextInt());
    }
  }
}
