package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Stores coordinates of the successful hits.
 *
 * @param coordinates - of the shots that hit a ship
 */
public record SuccessfulHitsJson(
    @JsonProperty("coordinates") List<CoordJson> coordinates
) {
}
