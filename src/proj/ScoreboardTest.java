package proj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ScoreboardTest {

    @Test
    public void testSetAndGetScore() {
        Scoreboard board = new Scoreboard();
        boolean success = board.setScore("Ones", 3);
        assertTrue(success);
        assertEquals(3, board.getScores().get("Ones"));
    }

    @Test
    public void testSetScoreTwiceFails() {
        Scoreboard board = new Scoreboard();
        board.setScore("Twos", 4);
        boolean success = board.setScore("Twos", 10);
        assertFalse(success);
        assertEquals(4, board.getScores().get("Twos"));
    }

    @Test
    public void testTotalScore() {
        Scoreboard board = new Scoreboard();
        board.setScore("Fours", 8);
        board.setScore("Chance", 20);
        assertEquals(28, board.getTotalScore());
    }

    @Test
    public void testRemainingCategories() {
        Scoreboard board = new Scoreboard();
        board.setScore("Yahtzee", 50);
        board.setScore("Chance", 22);

        ArrayList<String> remaining = board.getRemainingCategories();
        assertFalse(remaining.contains("Yahtzee"));
        assertFalse(remaining.contains("Chance"));
        assertEquals(11, remaining.size()); // total 13 categories - 2 filled = 11
    }

    @Test
    public void testToStringIncludesTotal() {
        Scoreboard board = new Scoreboard();
        board.setScore("Fives", 15);
        String output = board.toString();
        assertTrue(output.contains("Fives"));
        assertTrue(output.contains("15"));
        assertTrue(output.contains("Total Score"));
    }
}
