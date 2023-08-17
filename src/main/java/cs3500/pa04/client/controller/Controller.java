package cs3500.pa04.client.controller;

/**
 * Controls the progression of some workflow/steps, potentially taking
 * user input, validating it, and then calling for the appropriate action
 * from the model.
 */
public interface Controller {

  /**
   * Initiates a sequence of steps to follow.
   */
  void run();
}
