package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.client.model.ShipAdapter;
import java.util.List;

/**
 * Stores the generated fleet.
 *
 * @param fleet - fleet generated after setup
 */
public record SetupResponseJson(
    @JsonProperty("fleet") List<ShipAdapter> fleet) {
}
