package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stores end game result and the reason why in Json format.
 *
 * @param result - whether the game was a win, a loss, or a tie
 * @param reason - why the game resulted the way it did
 */
public record EndGameJson(
    @JsonProperty("result") String result,
    @JsonProperty("reason") String reason
) {
}
