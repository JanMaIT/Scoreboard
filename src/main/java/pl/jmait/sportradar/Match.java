package pl.jmait.sportradar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static pl.jmait.sportradar.utils.TextStrings.SCORE_DECREASED;
import static pl.jmait.sportradar.utils.TextStrings.SCORE_NEGATIVE;

/**
 * Represents a football match between two teams.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Match {
    @NonNull
    private final String homeTeam;
    @NonNull
    private final String awayTeam;
    private int homeScore = 0;
    private int awayScore = 0;

    /**
     * Timestamp (nanoTime) when the match was started.
     */
    private final long startTimestamp;

    /**
     * Returns the aggregate score of both teams.
     *
     * @return total score
     */
    public int totalScore() {
        return homeScore + awayScore;
    }

    /**
     * Sets new score for Home Team.
     *
     * @throws IllegalArgumentException if score is negative
     */
    public void setHomeScore(int newScore) {
        IsPositive(newScore);
        this.homeScore = newScore;
    }

    /**
     * Sets new score for Away Team.
     *
     * @throws IllegalArgumentException if score is negative
     */
    public void setAwayScore(int newScore) {
        IsPositive(newScore);
        this.awayScore = newScore;
    }

    /**
     * Updates score for Home Team checking if new score is not lower than current score.
     *
     * @throws IllegalArgumentException if score is negative
     * @throws IllegalArgumentException if score is lower than current score
     */
    public void updateHomeScore(int newScore) {
        IsNotLower(homeScore, newScore);
        this.homeScore = newScore;
    }

    /**
     * Updates score for Away Team checking if new score is not lower than current score.
     *
     * @throws IllegalArgumentException if score is negative
     * @throws IllegalArgumentException if score is lower than current score
     */
    public void updateAwayScore(int newScore) {
        IsNotLower(awayScore, newScore);
        this.awayScore = newScore;
    }

    private static void IsPositive(int score) {
        if (score < 0) {
            throw new IllegalArgumentException(SCORE_NEGATIVE);
        }
    }
    private static void IsNotLower(int oldScore, int newScore) {
        if (newScore < oldScore) {
            throw new IllegalArgumentException(SCORE_DECREASED);
        }
    }
}