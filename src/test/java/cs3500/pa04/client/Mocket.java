package cs3500.pa04.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;

/**
 * Mock class used to test classes that need a Socket.
 */
public class Mocket extends Socket {
  private final InputStream inputs;
  private final ByteArrayOutputStream log;

  /**
   * Creates an instance of Mocket.
   *
   * @param log           - the log that needs to be returned as an output
   * @param messageToSend - the message that needs to be sent in the input stream
   */
  public Mocket(ByteArrayOutputStream log, List<String> messageToSend) {
    this.log = log;
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    for (int i = 0; i < messageToSend.size(); i++) {
      printWriter.println(messageToSend.get(i));
    }
    this.inputs = new ByteArrayInputStream(stringWriter.toString().getBytes());
  }

  /**
   * Gets the input stream from the mocket.
   *
   * @return - given input stream
   */
  @Override
  public InputStream getInputStream() {
    return this.inputs;
  }

  /**
   * Gets the output log from the mocket.
   *
   * @return - given output stream
   */
  @Override
  public OutputStream getOutputStream() {
    return this.log;
  }
}
