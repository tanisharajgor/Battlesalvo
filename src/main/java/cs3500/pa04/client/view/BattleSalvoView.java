package cs3500.pa04.client.view;

import cs3500.pa04.client.model.Board;
import java.util.Scanner;

/**
 * The view for a BattleSalvo game, takes in input from the user and displays output
 * to the user.
 */
public class BattleSalvoView {
  // Putting the Readable and reading input in the view to help with extensibility.
  // Validate and processing will happen in the Controller.
  private final String DIVIDER = "\n--------------------------------------------";
  private Scanner inputReader;
  private static final StringBuilder consoleOutput = new StringBuilder();

  /**
   * Creates a new instance of BattleSalvoView.
   *
   * @param reader - a readable to help with reading user input
   */
  public BattleSalvoView(Readable reader) {
    inputReader = new Scanner(reader);
  }

  /**
   * Creates a new instance of BattleSalvoView.
   */
  // For testing in class methods, that do not need input.
  public BattleSalvoView() {
    inputReader = null;
  }

  /**
   * Display the given lines to the user.
   *
   * @param linesToDisplay - the lines to display
   * @param divider - whether a divider should be included
   */
  public void displayLines(String linesToDisplay, boolean divider) {
    consoleOutput.delete(0, consoleOutput.length());
    consoleOutput.append(linesToDisplay);
    if (divider) {
      consoleOutput.append(DIVIDER);
    }
    System.out.println(consoleOutput);
  }

  /**
   * Display the given board to the user.
   *
   * @param board - the board to display
   */
  public void displayBoard(Board board) {
    consoleOutput.delete(0, consoleOutput.length());
    char[][] rawBoard = board.getRawBoard();
    for (int i = 0; i < rawBoard.length; i++) {
      for (int j = 0; j < rawBoard[0].length; j++) {
        consoleOutput.append(rawBoard[i][j]);
        if (j != rawBoard[0].length - 1) {
          consoleOutput.append(" ");
        }
      }
      consoleOutput.append("\n");
    }
    System.out.println(consoleOutput);
  }

  /**
   * Read input from the user.
   *
   * @return - the user's input
   */
  public String read() {
    StringBuilder input = new StringBuilder();
    input.append(inputReader.nextLine());
    return input.toString();
  }

  /**
   * Return the most current statement displayed to the console.
   *
   * @return - the statement
   */
  public String getConsoleOutput() {
    return consoleOutput.toString();
  }
}
