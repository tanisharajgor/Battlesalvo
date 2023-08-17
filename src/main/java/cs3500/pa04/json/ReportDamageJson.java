package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Stores locations of damaged ships.
 *
 * @param coordinates - coordinates of the damaged ships
 */
public record ReportDamageJson(
    @JsonProperty("coordinates") List<CoordJson> coordinates
) {
}
