Files added for PA04 functionality:

Added Controller Interface and Proxy Controller.
- ProxyController was added to control the progression of steps for reading messages from the server, delegating the processing of those messages, and forming the respective responses.
- Controller interface was added to allow for abstraction (if more modes want to be added).

LocalServerManager class was added to perform local actions in server mode.
- These include things such as keeping a record of the opponent board (as part of AI caching strategy).
- Does not control the timing of when these actions happen, they are called by ProxyController (part of the model)/

Added JSON records for join, setup, takeShots, reportDamage, successfulHits, endGame. As well as a ShipAdapter.
- These hold properties for constructing client response messages.
- ShipAdapter is used to put Ship instances into the proper format for setup.

Added server connection and command-line argument initiation to the Driver.
- Method runClient() takes in a host and port, and runs an instance of ProxyController.
- main() reads the given command-line arguments and starts up the appropriate mode.
