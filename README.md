# Football Scoreboard

This project is a simple **in-memory Java library** that models a **Live Football World Cup Scoreboard**, as specified in a coding exercise.

## Features

- Start new matches with an initial score of `0–0`.
- Force update match scores (allowing any scores bigger than 0).
- Update match scores (rules as in force but also not allowing decreases — scores can never decrease).
- Finish matches and remove them from the scoreboard.
- Get a live summary of ongoing matches, sorted by:
  1. **Total score** (descending).
  2. **Most recently started** among matches with equal scores.

## Technologies Used

- Java 21
- Maven
- JUnit 5 (for unit testing)
- Lombok (to reduce boilerplate code)

## Requirements

- Java 21

## Build & Test Instructions

To build the project and run the unit tests, execute:

```bash
mvn clean verify
```
## Code Quality & Principles

- ✅ **Clean Code**: clear names, small focused methods, no duplication
- ✅ **TDD**: full unit test coverage with descriptive, independent JUnit 5 tests
- ✅ **Validation**: prevents invalid input (e.g. same team, reused team, negative or decreasing scores)
- ✅ **SOLID**: simple OOP design with proper separation of concerns
- ✅ **Lightweight**: pure Java 21 with Lombok, no runtime dependencies

## Example Usage

Below is a simple example of how to use the `ScoreBoard` class:

```java
import com.example.scoreboard.ScoreBoard;
import com.example.scoreboard.Match;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ScoreBoard board = new ScoreBoard();

        // Start new matches
        long match1 = board.startMatch("Mexico", "Canada");
        long match2 = board.startMatch("Spain", "Brazil");

        // Update scores
        board.updateMatchScore(match1, 0, 5);   // Mexico 0 - Canada 5
        board.updateMatchScore(match2, 10, 2);  // Spain 10 - Brazil 2

        // Retrieve summary
        List<Match> summary = board.getSummary();
        for (Match match : summary) {
            System.out.printf("%s %d - %s %d%n",
                match.getHomeTeam(),
                match.getHomeScore(),
                match.getAwayTeam(),
                match.getAwayScore()
            );
        }

        // Finish a match
        board.finishMatch(match1);
    }
}
