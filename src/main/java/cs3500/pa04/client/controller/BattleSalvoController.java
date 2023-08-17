package cs3500.pa04.client.controller;

import cs3500.pa04.client.model.AiPlayer;
import cs3500.pa04.client.model.Board;
import cs3500.pa04.client.model.BoardManager;
import cs3500.pa04.client.model.ConsolePlayer;
import cs3500.pa04.client.model.Coord;
import cs3500.pa04.client.model.GamePlayer;
import cs3500.pa04.client.model.GameResult;
import cs3500.pa04.client.model.Player;
import cs3500.pa04.client.model.ShipType;
import cs3500.pa04.client.view.BattleSalvoView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the progression of the BattleSalvo game, taking in user input,
 * validating it, and making calls to the model to perform necessary actions.
 */
public class BattleSalvoController implements Controller {
  BattleSalvoView view;
  BoardManager record;
  GameResult result;
  Player player1;
  Player player2;

  /**
   * Creates a new BattleSalvoController instance.
   *
   * @param view - displays output to user
   */
  public BattleSalvoController(BattleSalvoView view) {
    this.view = view;
    player1 = new ConsolePlayer();
    player2 = new AiPlayer();
    record = new BoardManager();
    result = new GameResult();
  }

  /**
   * Controls the main progression of steps for a BattleSalvo game, including
   * game setup, getting the fleet, performing volleys, updating board views, and
   * determining when the game is over. Makes calls to respective class/methods in
   * the model.
   */
  public void run() {
    view.displayLines("Hello! Welcome to the OOD BattleSalvo Game!", true);

    // Game setup
    List<Integer> dimensions = getBoardDimensions();
    Map<ShipType, Integer> fleet = getFleet(dimensions.get(0), dimensions.get(1));
    player1.setup(dimensions.get(0), dimensions.get(1), fleet);
    player2.setup(dimensions.get(0), dimensions.get(1), fleet);

    // Initial Displaying of Boards
    Board opponentRecord = record.getOpponentRecord();
    Board board1 = record.getBoard1();
    showBoards(opponentRecord, board1);

    // Volleys
    GamePlayer.setView(view);
    while (!result.isGameOver(board1, record.getBoard2())) {
      List<Coord> aiShots = player2.takeShots();
      player1.reportDamage(aiShots);
      player2.reportDamage(player1.takeShots());
      record.updateOpponentRecord();
      opponentRecord = record.getOpponentRecord();
      board1 = record.getBoard1();
      showBoards(opponentRecord, board1);
    }

    // Game End
    view.displayLines("GAME END :D\n" + result.getEndState(board1.numShipsRemaining(),
        record.getBoard2().numShipsRemaining()).getStatement(), false);
  }

  /**
   * Gets the desired height and width of the boards from the user.
   *
   * @return - a list with the height and width
   */
  public List<Integer> getBoardDimensions() {
    List<Integer> dimensions  = new ArrayList<>();
    view.displayLines("Please enter a valid height and width below:", true);
    while (dimensions.size() != 2) {
      dimensions.clear();
      String[] inputs = view.read().split(" ");
      try {
        for (String input : inputs) {
          dimensions.add(Integer.parseInt((input)));
        }
        // If the inputs are out of bounds.
        if (dimensions.get(0) < 6 || dimensions.get(0) > 15 || dimensions.get(1) < 6
            || dimensions.get(1) > 15) {
          view.displayLines("You've entered invalid dimensions. Please remember that "
              + "the height and width\n" + "of the game must be in the range [6, 15], inclusive. "
              + "Try again!", true);
          dimensions.clear();
        }
        // If there is a parsing error (number not entered).
      } catch (Exception e) {
        view.displayLines("You've entered invalid dimensions. There should only be one "
            + "space between the\n" + "height and width. Try again!", true);
      }
    }
    return dimensions;
  }

  /**
   * Determines the maximum number of ships that a user can have in their fleet.
   *
   * @param height - the height of the boards
   * @param width - the width of the boards
   * @return int - the max number of ships
   */
  private int determineMaxNumShips(int height, int width) {
    return Math.min(height, width);
  }

  /**
   * Gets the number of each ship type for the fleet from the user.
   *
   * @param height - the height of the boards
   * @param width - the width of the boards
   * @return - map with each ship type and their corresponding number
   */
  public Map<ShipType, Integer> getFleet(int height, int width) {
    List<Integer> fleet = new ArrayList<>();
    int sum = 0;
    while (fleet.size() != 4) {
      sum = 0;
      fleet.clear();
      view.displayLines(String.format("Please enter your fleet in the order [%s, %s, %s, %s].\n"
              + "Remember, your fleet may not exceed size %d.", ShipType.CARRIER.getName(),
          ShipType.BATTLESHIP.getName(), ShipType.DESTROYER.getName(), ShipType.SUBMARINE.getName(),
          determineMaxNumShips(height, width)), true);
      String[] inputs = view.read().split(" ");
      try {
        for (String input : inputs) {
          int num = Integer.parseInt((input));
          // Make sure inputs are in bounds.
          if (num > 0) {
            sum += num;
            fleet.add(num);
          } else {
            view.displayLines("You've entered invalid fleet sizes.", true);
          }
        }
        // If there are parsing errors (number not entered).
      } catch (Exception e) {
        view.displayLines("You've entered invalid fleet sizes.", true);
      }
    }
    Map<ShipType, Integer> fleetMap = new LinkedHashMap<>();
    // Make sure sum of the inputs does not exceed the smallest dimension.
    if (sum > determineMaxNumShips(height, width)) {
      view.displayLines("You've entered invalid fleet sizes.", true);
      return getFleet(height, width);
    } else {
      fleetMap.put(ShipType.CARRIER, fleet.get(0));
      fleetMap.put(ShipType.BATTLESHIP, fleet.get(1));
      fleetMap.put(ShipType.DESTROYER, fleet.get(2));
      fleetMap.put(ShipType.SUBMARINE, fleet.get(3));
      return fleetMap;
    }
  }

  /**
   * Makes call to the view to show the user's board and a record
   * of the opponent's board (data accumulated), for every round.
   *
   * @param opponentRecord - opponent record board
   * @param board1 - user's board
   */
  public void showBoards(Board opponentRecord, Board board1) {
    view.displayLines("Opponent Board Data:", false);
    view.displayBoard(opponentRecord);
    view.displayLines("Your Board:", false);
    view.displayBoard(board1);
  }
}
