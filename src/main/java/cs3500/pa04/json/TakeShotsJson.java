package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.client.model.Coord;
import java.util.List;

/**
 * Stores coordinates of the shots taken.
 *
 * @param coordinates - the tiles being shot at
 */
public record TakeShotsJson(
    @JsonProperty("coordinates") List<Coord> coordinates
) {
}
