package cs3500.pa04.client.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.client.model.Coord;
import cs3500.pa04.client.model.EndState;
import cs3500.pa04.client.model.GameResult;
import cs3500.pa04.client.model.LocalServerManager;
import cs3500.pa04.client.view.BattleSalvoView;
import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ReportDamageJson;
import cs3500.pa04.json.SetupArgsJson;
import cs3500.pa04.json.SetupResponseJson;
import cs3500.pa04.json.SuccessfulHitsJson;
import cs3500.pa04.client.model.Player;
import cs3500.pa04.json.TakeShotsJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Connects to the server as a proxy and handles json arguments, converting them into Java.
 */
public class ProxyController implements Controller {
  private final Socket server;
  private final ObjectMapper mapper = new ObjectMapper();
  private InputStream in;
  private PrintStream out;
  private BattleSalvoView view;
  private LocalServerManager localServerManager;

  /**
   * Constructs an instance of a proxy player.
   *
   * @param server - the socket connection to the server
   * @param player - the instance of a player
   */
  public ProxyController(Socket server, Player player) {
    this.server = server;
    try {
      this.localServerManager = new LocalServerManager(player);
      this.in = server.getInputStream();
      this.out = new PrintStream(server.getOutputStream());
      this.view = new BattleSalvoView();
    } catch (IOException e) {
      view.displayLines("There was an error in creating an instance of a proxy player.", true);
    }
  }

  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);
      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      view.displayLines("Disconnected from the server or parsing exception.", false);
    }
  }

  /**
   * Determines the type of request the server has sent and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message - the MessageJSON used to determine what the server has sent
   */
  public void delegateMessage(MessageJson message) {
    String methodName = message.methodName();
    JsonNode arguments = message.arguments();

    if ("join".equals(methodName)) {
      handleJoin();
    } else if ("setup".equals(methodName)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(methodName)) {
      handleTakeShots();
    } else if ("report-damage".equals(methodName)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(methodName)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(methodName)) {
      handleEndGame(arguments);
    } else {
      view.displayLines("Invalid message name.", false);
    }
  }

  /**
   * Handles the join method using json.
   */
  private void handleJoin() {
    JoinJson response = new JoinJson("tanisharajgor", "SINGLE");
    JsonNode jsonResponse =
        convertToSerializedMessageJson("join", JsonUtils.serializeRecord(response));
    this.out.println(jsonResponse);
  }

  /**
   * Handles the setup method using json.
   *
   * @param arguments - the arguments to pass to the setup json
   */
  private void handleSetup(JsonNode arguments) {
    SetupArgsJson setupArgs = this.mapper.convertValue(arguments, SetupArgsJson.class);
    localServerManager.setOppoRecord(setupArgs.height(), setupArgs.width());
    SetupResponseJson response = new SetupResponseJson(
        localServerManager.getFleet(setupArgs.height(), setupArgs.width(), setupArgs.fleetSpec())
    );
    JsonNode jsonResponse =
        convertToSerializedMessageJson("setup", JsonUtils.serializeRecord(response));
    this.out.println(jsonResponse);
  }

  /**
   * Handles the takeShots method using json.
   */
  private void handleTakeShots() {
    localServerManager.numNonGuessedOppoCoord();
    TakeShotsJson response = new TakeShotsJson(localServerManager.getVolley());
    JsonNode jsonResponse =
        convertToSerializedMessageJson("take-shots", JsonUtils.serializeRecord(response));
    this.out.println(jsonResponse);
  }

  /**
   * Handles the reportDamage method using json.
   *
   * @param arguments - the arguments to pass to the reportDamage json
   */
  private void handleReportDamage(JsonNode arguments) {
    ReportDamageJson reportDamageArgs = this.mapper.convertValue(arguments, ReportDamageJson.class);

    // Turn CoordJson into Coord list.
    List<Coord> coordinates = new ArrayList<>();
    for (CoordJson coord : reportDamageArgs.coordinates()) {
      coordinates.add(new Coord(coord.xCoord(), coord.yCoord()));
    }

    TakeShotsJson response = new TakeShotsJson(localServerManager.getDamage(coordinates));
    JsonNode jsonResponse =
        convertToSerializedMessageJson("report-damage", JsonUtils.serializeRecord(response));
    this.out.println(jsonResponse);
  }

  /**
   * Handles the successfulHits method using json.
   *
   * @param arguments - the arguments to pass to the successfulHits json
   */
  private void handleSuccessfulHits(JsonNode arguments) {
    SuccessfulHitsJson successfulHitsArgs =
        this.mapper.convertValue(arguments, SuccessfulHitsJson.class);
    // Turn CoordJson into Coord list.
    List<Coord> coordinates = new ArrayList<>();
    for (CoordJson coord : successfulHitsArgs.coordinates()) {
      coordinates.add(new Coord(coord.xCoord(), coord.yCoord()));
    }
    localServerManager.updateOpponentRecord(coordinates);

    JsonNode jsonResponse =
        convertToSerializedMessageJson("successful-hits",
            mapper.createObjectNode());
    this.out.println(jsonResponse);
  }

  /**
   * Handles the endGame method using json.
   *
   * @param arguments - the arguments to pass to the endGame json
   */
  private void handleEndGame(JsonNode arguments) {
    EndGameJson endGameJson = this.mapper.convertValue(arguments, EndGameJson.class);
    GameResult result;
    if (endGameJson.result().equals("WIN")) {
      result = new GameResult(EndState.WIN);
    } else if (endGameJson.result().equals("LOSE")) {
      result = new GameResult(EndState.LOSE);
    } else {
      result = new GameResult(EndState.DRAW);
    }
    localServerManager.reportEndState(result, endGameJson.reason());
    try {
      server.close();
    } catch (IOException e) {
      view.displayLines("Socket couldn't close.", false);
    }
    JsonNode jsonResponse =
        convertToSerializedMessageJson("end-game",
            mapper.createObjectNode());
    this.out.println(jsonResponse);
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
