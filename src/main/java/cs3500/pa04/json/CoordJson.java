package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stores coordinates in Json format.
 *
 * @param xCoord - the x coordinate
 * @param yCoord - the y coordinate
 */
public record CoordJson(
    @JsonProperty("x") int xCoord,
    @JsonProperty("y") int yCoord
) {
}
