import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jmait.sportradar.Match;
import pl.jmait.sportradar.ScoreBoard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {

    private ScoreBoard board;

    @BeforeEach
    void setUp() {
        board = new ScoreBoard();
    }

    @Test
    void startsAndFinishesMatch() {
        long id = board.startMatch("Mexico", "Canada");
        assertNotNull(board.getMatch(id));
        board.finishMatch(id);
        assertThrows(RuntimeException.class, () -> board.getMatch(id));
    }

    @Test
    void throwsWhenStartingMatchWithTeamAlreadyInGame() {
        board.startMatch("Mexico", "Canada");
        assertThrows(RuntimeException.class, () -> board.startMatch("Mexico", "Italy"));
        assertThrows(RuntimeException.class, () -> board.startMatch("Italy", "Canada"));
        assertThrows(RuntimeException.class, () -> board.startMatch("Canada", "Mexico"));

    }

    @Test
    void throwsWhenStartingMatchWithIdenticalTeams() {
        assertThrows(RuntimeException.class, () -> board.startMatch("Mexico", "mexico"));
        assertThrows(RuntimeException.class, () -> board.startMatch("Mexico", "Mexico"));

    }

    @Test
    void throwsWhenUpdatingScoreWithNegativeValue() {
        long id = board.startMatch("Mexico", "Canada");
        assertThrows(RuntimeException.class, () -> board.updateMatchScore(id, 1, -1));
        assertThrows(RuntimeException.class, () -> board.updateMatchScore(id, -1, 1));
        assertThrows(RuntimeException.class, () -> board.updateMatchScore(id, -1, -1));

    }

    @Test
    void updatesScore() {
        long id = board.startMatch("Spain", "Brazil");
        board.updateMatchScore(id, 10, 2);

        Match match = board.getMatch(id);
        assertEquals(10, match.getHomeScore());
        assertEquals(2, match.getAwayScore());
        assertEquals(12, match.totalScore());
    }
    @Test
    void forceUpdatesScore() {
        long id = board.startMatch("Spain", "Brazil");
        board.forceUpdateMatchScore(id, 10, 2);

        Match match = board.getMatch(id);
        assertEquals(10, match.getHomeScore());
        assertEquals(2, match.getAwayScore());
        assertEquals(12, match.totalScore());
    }
    @Test
    void throwsWhenUpdatingScoreWithLowerValue() {
        long id = board.startMatch("Spain", "Brazil");
        board.updateMatchScore(id, 10, 2);
        assertThrows(Exception.class, () -> board.updateMatchScore(id, 9, 2));
        assertThrows(Exception.class, () -> board.updateMatchScore(id, 9, 1));
        assertThrows(Exception.class, () -> board.updateMatchScore(id, 9, 3));

    }
    @Test
    void summaryFollowsRequiredOrdering() {
        long m1 = board.startMatch("Mexico", "Canada");
        long m2 = board.startMatch("Spain", "Brazil");
        long m3 = board.startMatch("Germany", "France");
        long m4 = board.startMatch("Uruguay", "Italy");
        long m5 = board.startMatch("Argentina", "Australia");

        board.updateMatchScore(m1, 0, 5);
        board.updateMatchScore(m2, 10, 2);
        board.updateMatchScore(m3, 2, 2);
        board.updateMatchScore(m4, 6, 6);
        board.updateMatchScore(m5, 3, 1);

        List<Match> summary = board.getSummary();

        assertEquals("Uruguay", summary.get(0).getHomeTeam());
        assertEquals("Spain", summary.get(1).getHomeTeam());
        assertEquals("Mexico", summary.get(2).getHomeTeam());
        assertEquals("Argentina", summary.get(3).getHomeTeam());
        assertEquals("Germany", summary.get(4).getHomeTeam());
    }
}