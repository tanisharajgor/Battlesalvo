package cs3500.pa04.client;

import cs3500.pa04.client.controller.BattleSalvoController;
import cs3500.pa04.client.controller.ProxyController;
import cs3500.pa04.client.model.AiPlayer;
import cs3500.pa04.client.view.BattleSalvoView;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {

  /**
   * This method connects to the server at the given host and port, builds a proxy referee
   * to handle communication with the server, and sets up a client player.
   *
   * @param host the server host
   * @param port the server port
   * @throws IOException if there is a communication issue with the server
   */
  private static void runClient(String host, int port)
      throws IOException, IllegalStateException {
    Socket server = new Socket(host, port);

    ProxyController proxyController = new ProxyController(server, new AiPlayer());
    proxyController.run();
  }

  /**
   * The main entrypoint into the code as the Client. Given a host and port as parameters, the
   * client is run. If there is an issue with the client or connecting,
   * an error message will be printed.
   *
   * @param args The expected parameters are the server's host and port
   */
  public static void main(String[] args) {
    Readable reader = new InputStreamReader(System.in);
    BattleSalvoView view = new BattleSalvoView(reader);
    if (args.length == 2) {
      String host = args[0];
      int port = Integer.parseInt(args[1]);
      try {
        runClient(host, port);
      } catch (IOException | IllegalStateException e) {
        view.displayLines("Unable to connect to the server.", false);
      }
    } else if (args.length == 0) {
      BattleSalvoController controller = new BattleSalvoController(view);
      controller.run();
    } else {
      throw new IllegalArgumentException(
          "To connect to the server provide (host, port) and to play locally (against AI)"
              + "provide 0 command-line arguments.");
    }
  }
}