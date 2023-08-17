package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Stores name of the method and arguments.
 *
 * @param methodName the name of the method called
 * @param arguments  the arguments passed
 */
public record MessageJson(
    @JsonProperty("method-name") String methodName,
    @JsonProperty("arguments") JsonNode arguments
) {
}
