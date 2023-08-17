package cs3500.pa04.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.client.controller.ProxyController;
import cs3500.pa04.client.model.AiPlayer;
import cs3500.pa04.client.model.Board;
import cs3500.pa04.client.model.BoardManager;
import cs3500.pa04.client.model.GamePlayer;
import cs3500.pa04.client.model.ShipType;
import cs3500.pa04.client.view.BattleSalvoView;
import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ReportDamageJson;
import cs3500.pa04.json.SetupArgsJson;
import cs3500.pa04.json.SuccessfulHitsJson;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ProxyController class.
 */
public class ProxyControllerTest {
  private final ObjectMapper mapper = new ObjectMapper();
  private ByteArrayOutputStream log;
  private ProxyController controller;
  private BattleSalvoView view;
  private Map<ShipType, Integer> fleet;

  /**
   * Actions to perform before the class runs any tests.
   */
  @BeforeAll
  public static void setupAll() {
    GamePlayer.setRandSeed(1234567);
  }

  /**
   * Actions to perform before every test.
   */
  @BeforeEach
  public void setup() {
    this.log = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
    view = new BattleSalvoView();
    fleet = new LinkedHashMap<>();
    // setup
    fleet.put(ShipType.CARRIER, 2);
    fleet.put(ShipType.BATTLESHIP, 1);
    fleet.put(ShipType.DESTROYER, 2);
    fleet.put(ShipType.SUBMARINE, 1);
  }

  /**
   * Makes sure the client is able to join the server.
   */
  @Test
  public void testJoin() {
    // Make the sample message.
    JsonNode joinArgs = convertToSerializedMessageJson("join",
        mapper.createObjectNode());
    // Create a client with all necessary messages.
    Mocket socket = new Mocket(this.log, List.of(joinArgs.toString()));
    // Make an instance of ProxyController.
    this.controller = new ProxyController(socket, new AiPlayer());
    // Run the controller and check behavior.
    this.controller.run();
    // Check that a JoinJson was returned.
    String expected =
        "{\"method-name\":\"join\",\"arguments\":"
            + "{\"name\":\"tanisharajgor\",\"game-type\":\"SINGLE\"}}\n";
    checkForExactResponse(expected);
  }

  /**
   * Makes sure the client is able to run setup on the server.
   */
  @Test
  public void testSetup() {
    Map<ShipType, Integer> fleet = new LinkedHashMap<>();
    fleet.put(ShipType.CARRIER, 2);
    fleet.put(ShipType.BATTLESHIP, 1);
    fleet.put(ShipType.DESTROYER, 2);
    fleet.put(ShipType.SUBMARINE, 1);
    SetupArgsJson arguments = new SetupArgsJson(6, 6, fleet);
    JsonNode setupArgs = convertToSerializedMessageJson("setup",
        JsonUtils.serializeRecord(arguments));
    Mocket socket = new Mocket(this.log, List.of(setupArgs.toString()));
    this.controller = new ProxyController(socket, new AiPlayer());
    this.controller.run();
    String expected = "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":["
        + "{\"coord\":{\"y\":5,\"x\":0},\"length\":6,\"direction\":\"HORIZONTAL\"},"
        + "{\"coord\":{\"y\":2,\"x\":0},\"length\":6,\"direction\":\"HORIZONTAL\"},"
        + "{\"coord\":{\"y\":4,\"x\":0},\"length\":5,\"direction\":\"HORIZONTAL\"},"
        + "{\"coord\":{\"y\":0,\"x\":1},\"length\":4,\"direction\":\"HORIZONTAL\"},"
        + "{\"coord\":{\"y\":3,\"x\":0},\"length\":4,\"direction\":\"HORIZONTAL\"},"
        + "{\"coord\":{\"y\":1,\"x\":1},\"length\":3,\"direction\":\"HORIZONTAL\"}"
        + "]}}\n";
    checkForExactResponse(expected);
  }

  /**
   * Tests if the correct game result is passed back when the game is lost.
   */
  @Test
  public void testEndGameLose() {
    JsonNode endGameArgs = convertToSerializedMessageJson("end-game",
        JsonUtils.serializeRecord(new EndGameJson("LOSE", "You lost.")));
    Mocket socket = new Mocket(this.log, List.of(endGameArgs.toString()));
    this.controller = new ProxyController(socket, new AiPlayer());
    this.controller.run();
    assertEquals("LOSE\nYou lost.", view.getConsoleOutput());
  }

  /**
   * Tests if the correct game result is passed back when the game is tied.
   */
  @Test
  public void testEndGameDraw() {
    JsonNode endGameArgs = convertToSerializedMessageJson("end-game",
        JsonUtils.serializeRecord(new EndGameJson("Draw", "You forced a draw.")));
    Mocket socket = new Mocket(this.log, List.of(endGameArgs.toString()));
    this.controller = new ProxyController(socket, new AiPlayer());
    this.controller.run();
    assertEquals("DRAW\nYou forced a draw.", view.getConsoleOutput());
  }

  /**
   * Tests that the server runs and plays the game properly.
   */
  @Test
  public void testRun() {
    // join
    JsonNode joinArgs = convertToSerializedMessageJson("join",
        mapper.createObjectNode());
    SetupArgsJson arguments = new SetupArgsJson(6, 6, fleet);
    JsonNode setupArgs = convertToSerializedMessageJson("setup",
        JsonUtils.serializeRecord(arguments));
    // take-shots
    JsonNode takeShotsArgs = convertToSerializedMessageJson("take-shots",
        mapper.createObjectNode());
    // report-damage
    JsonNode reportDamageArgs = convertToSerializedMessageJson("report-damage",
        JsonUtils.serializeRecord(new ReportDamageJson(new ArrayList<>())));
    // successful-hits
    JsonNode successfulHitsArgs = convertToSerializedMessageJson("successful-hits",
        JsonUtils.serializeRecord(new SuccessfulHitsJson(new ArrayList<>())));
    // end-game
    JsonNode endGameArgs = convertToSerializedMessageJson("end-game",
        JsonUtils.serializeRecord(new EndGameJson("WIN", "You won.")));
    Mocket socket = new Mocket(this.log, List.of(joinArgs.toString(), setupArgs.toString(),
        takeShotsArgs.toString(), reportDamageArgs.toString(), successfulHitsArgs.toString(),
        endGameArgs.toString()));
    this.controller = new ProxyController(socket, new AiPlayer());
    this.controller.run();
    assertEquals("WIN\nYou won.", view.getConsoleOutput());
  }

  /**
   * Tests if the server takes shots properly.
   */
  @Test
  public void testTakeShots() {
    AiPlayer player = new AiPlayer();
    char[][] opponentRawRecord = new char[6][6];
    for (char[] row : opponentRawRecord) {
      int rand = player.generateRandomInt(2);
      if (rand == 0) {
        Arrays.fill(row, 'H');
      } else {
        Arrays.fill(row, 'M');
      }
    }
    BoardManager.setOpponentRecord(new Board(opponentRawRecord));
    SetupArgsJson arguments = new SetupArgsJson(6, 6, fleet);
    JsonNode setupArgs = convertToSerializedMessageJson("setup",
        JsonUtils.serializeRecord(arguments));
    JsonNode takeShotsArgs = convertToSerializedMessageJson("take-shots",
        mapper.createObjectNode());
    Mocket socket = new Mocket(this.log, List.of(setupArgs.toString(), takeShotsArgs.toString()));
    this.controller = new ProxyController(socket, new AiPlayer());
    this.controller.run();
    String expected = "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":["
        + "{\"coord\":{\"y\":0,\"x\":3},\"length\":6,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"y\":0,\"x\":2},\"length\":6,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"y\":0,\"x\":4},\"length\":5,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"y\":0,\"x\":5},\"length\":4,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"y\":0,\"x\":0},\"length\":4,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"y\":0,\"x\":1},\"length\":3,\"direction\":\"VERTICAL\"}]}}\n"
        + "{\"method-name\":\"take-shots\",\"arguments\":{\"coordinates\":["
        + "{\"y\":0,\"x\":4},{\"y\":2,\"x\":3},{\"y\":5,\"x\":4},{\"y\":5,\"x\":3},"
        + "{\"y\":5,\"x\":2},{\"y\":0,\"x\":2}"
        + "]}}\n";
    checkForExactResponse(expected);
  }

  /**
   * Makes sure that reportDamage() works as intended.
   */
  @Test
  public void testReportDamage() {
    JsonNode reportDamageArgs = convertToSerializedMessageJson("report-damage",
        JsonUtils.serializeRecord(new ReportDamageJson(
            List.of(new CoordJson(1, 3), new CoordJson(2, 3)))));
    Mocket socket = new Mocket(this.log, List.of(reportDamageArgs.toString()));
    this.controller = new ProxyController(socket, new AiPlayer());
    this.controller.run();
    String expected = "{\"method-name\":\"report-damage\",\"arguments\":"
        + "{\"coordinates\":[{\"y\":3,\"x\":1},{\"y\":3,\"x\":2}]}}\n";
    checkForExactResponse(expected);
  }

  /**
   * Tests that the response given from the log is the same as the expected string.
   *
   * @param expected - string that should match the log
   */
  private void checkForExactResponse(String expected) {
    System.out.println(expected);
    System.out.println(logToString());
    assertEquals(expected, logToString());
  }

  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return log.toString(StandardCharsets.UTF_8);
  }

  /**
   * Converts the method and response to a serialized record that is usable.
   *
   * @param methodName - name of the method to serialize
   * @param response   - the response to that method
   * @return - the message in the form of JsonNode
   */
  private JsonNode convertToSerializedMessageJson(String methodName, JsonNode response) {
    return JsonUtils.serializeRecord(new MessageJson(methodName, response));
  }
}
