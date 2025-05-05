package pl.jmait.sportradar;

import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static pl.jmait.sportradar.utils.TextStrings.*;

public class ScoreBoard {
    /**
     * Stores matches by their identifier preserving insertion order.
     */
    private final Map<Long, Match> matches = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    /**
     * Starts a new football match with initial score.
     *
     * @param homeTeam name of the home team (non‑null)
     * @param awayTeam name of the away team (non‑null and different from home)
     * @return identifier of the created match
     * @throws IllegalArgumentException if teams are invalid
     * @throws IllegalStateException    if any of the teams is already playing
     */
    public long startMatch(@NonNull String homeTeam, @NonNull String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        ensureTeamsAreAvailable(homeTeam, awayTeam);

        long id = idGenerator.incrementAndGet();
        Match match = new Match(homeTeam, awayTeam, System.nanoTime());
        matches.put(id, match);
        return id;
    }

    /**
     * Force updates the score of a given match. New team's score can be lower than current team score.
     *
     * @param matchId    identifier of the match
     * @param homeScore  goals scored by the home team (non‑negative)
     * @param awayScore  goals scored by the away team (non‑negative)
     * @throws NoSuchElementException   if the match does not exist
     * @throws IllegalArgumentException if any score is negative
     */
    public void forceUpdateMatchScore(long matchId, int homeScore, int awayScore) {
        Match match = getMatch(matchId);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
    }

    /**
     * Updates the score of a given match. New team's score cannot be lower than current team score
     *
     * @param matchId    identifier of the match
     * @param homeScore  goals scored by the home team (non‑negative)
     * @param awayScore  goals scored by the away team (non‑negative)
     * @throws NoSuchElementException   if the match does not exist
     * @throws IllegalArgumentException if any score is negative
     * @throws IllegalArgumentException if team's score is lower than its current score.
     */
    public void updateMatchScore(long matchId, int homeScore, int awayScore) {
        Match match = getMatch(matchId);
        match.updateHomeScore(homeScore);
        match.updateAwayScore(awayScore);
    }

    /**
     * Finishes (removes) a match from the board.
     *
     * @param matchId identifier of the match
     * @throws NoSuchElementException if the match does not exist
     */
    public void finishMatch(long matchId) {
        if (matches.remove(matchId) == null) {
            throw new NoSuchElementException(MATCH_NOT_FOUND);
        }
    }

    /**
     * Returns a summary of matches ordered by total score (descending) and by the most recent
     * added to the system when totals are equal.
     *
     * @return list of matches in presentation order
     */
    public List<Match> getSummary() {
        return matches.values()
                .stream()
                .sorted((m1, m2) -> {
                    int totalCompare = Integer.compare(m2.totalScore(), m1.totalScore());
                    if (totalCompare != 0) {
                        return totalCompare;
                    }
                    return Long.compare(m2.getStartTimestamp(), m1.getStartTimestamp());
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a match by its identifier.
     *
     * @param matchId identifier
     * @return match
     * @throws NoSuchElementException if not found
     */
    public Match getMatch(long matchId) {
        Match match = matches.get(matchId);
        if (match == null) {
            throw new NoSuchElementException(MATCH_NOT_FOUND);
        }
        return match;
    }

    private static void validateTeams(String homeTeam, String awayTeam) {
        if (homeTeam.isBlank() || awayTeam.isBlank()) {
            throw new IllegalArgumentException(TEAM_NAMES_BLANK);
        }
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException(SIMILAR_TEAMS_NAMES);
        }
    }

    private void ensureTeamsAreAvailable(String homeTeam, String awayTeam) {
        matches.values().forEach(existing -> {
            if (existing.getHomeTeam().equalsIgnoreCase(homeTeam)
                    || existing.getAwayTeam().equalsIgnoreCase(homeTeam)
                    || existing.getHomeTeam().equalsIgnoreCase(awayTeam)
                    || existing.getAwayTeam().equalsIgnoreCase(awayTeam)) {
                throw new IllegalStateException(TEAM_IS_PLAYING);
            }
        });
    }
}