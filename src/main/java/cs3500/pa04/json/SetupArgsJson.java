package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.client.model.ShipType;
import java.util.Map;

/**
 * Stores setup information about board dimensions and fleet specifications.
 *
 * @param width - width of the board
 * @param height - height of the board
 * @param fleetSpec - specifications about how many of each ship there are
 */
public record SetupArgsJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> fleetSpec) {
}
