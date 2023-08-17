package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stores join information.
 *
 * @param name - name of the player
 * @param gameType - whether the game is SINGLE or MULTI
 */
public record JoinJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") String gameType) {
}
