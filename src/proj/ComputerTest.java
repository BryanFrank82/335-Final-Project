package proj;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComputerTest {

    private ComputerHard hardPlayer;
    private ComputerEasy easyPlayer;
    private Scoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        hardPlayer = new ComputerHard();
        easyPlayer = new ComputerEasy();
        scoreboard = new Scoreboard();
    }

    private void setDiceValues(ArrayList<Dice> diceList, Value... values) {
        for (int i = 0; i < values.length; i++) {
            Dice d = diceList.get(i);
            while (d.getCurrentValue() != values[i]) {
                d.roll();
            }
        }
    }

    @Test
    public void testHardModePrefersYahtzee() {
        Cup cup = new Cup();
        ArrayList<Dice> inDice = cup.getInDice();
        setDiceValues(inDice, Value.SIX, Value.SIX, Value.SIX, Value.SIX, Value.SIX);

        hardPlayer.rollDiceForTesting(inDice);
        hardPlayer.roll(scoreboard);

        assertEquals(50, scoreboard.getScores().get("Yahtzee"));
    }

    @Test
    public void testHardModePrefersLargeStraight() {
        Cup cup = new Cup();
        ArrayList<Dice> inDice = cup.getInDice();
        setDiceValues(inDice, Value.TWO, Value.THREE, Value.FOUR, Value.FIVE, Value.SIX);

        hardPlayer.rollDiceForTesting(inDice);
        hardPlayer.roll(scoreboard);

        assertEquals(40, scoreboard.getScores().get("Large Straight"));
    }

    @Test
    public void testHardModeChoosesFullHouse() {
        Cup cup = new Cup();
        ArrayList<Dice> inDice = cup.getInDice();
        setDiceValues(inDice, Value.THREE, Value.THREE, Value.THREE, Value.TWO, Value.TWO);

        hardPlayer.rollDiceForTesting(inDice);
        hardPlayer.roll(scoreboard);

        assertEquals(25, scoreboard.getScores().get("Full House"));
    }

    @Test
    public void testEasyModeChoosesAnyScore() {
        Cup cup = new Cup();
        ArrayList<Dice> inDice = cup.getInDice();
        setDiceValues(inDice, Value.ONE, Value.ONE, Value.TWO, Value.THREE, Value.FOUR);

        easyPlayer.rollDiceForTesting(inDice);
        easyPlayer.roll(scoreboard);

        boolean anyScored = scoreboard.getScores().values().stream().anyMatch(v -> v != null && v > 0);
        assertTrue(anyScored);
    }

    @Test
    public void testScoreboardOnlyOneCategoryFilledAfterRoll() {
        hardPlayer.roll(scoreboard);
        long filled = scoreboard.getScores().values().stream().filter(s -> s != null).count();
        assertEquals(1, filled);
    }

    @Test
    public void testScoreboardNoCategoryRepeated() {
        for (int i = 0; i < 13; i++) {
            hardPlayer.roll(scoreboard);
        }
        long filled = scoreboard.getScores().values().stream().filter(s -> s != null).count();
        assertEquals(13, filled);
    }

    @Test
    public void testTotalScoreAccumulates() {
        int prevTotal = 0;
        for (int i = 0; i < 5; i++) {
            hardPlayer.roll(scoreboard);
            assertTrue(scoreboard.getTotalScore() >= prevTotal);
            prevTotal = scoreboard.getTotalScore();
        }
    }

    @Test
    public void testNoInvalidCategorySelection() {
        for (int i = 0; i < 13; i++) {
            hardPlayer.roll(scoreboard);
        }
        for (Map.Entry<String, Integer> entry : scoreboard.getScores().entrySet()) {
            assertNotNull(entry.getValue());
        }
    }

    @Test
    public void testAllCategoriesFilledAfter13Rolls() {
        for (int i = 0; i < 13; i++) {
            easyPlayer.roll(scoreboard);
        }
        assertEquals(13, scoreboard.getScores().entrySet().stream().filter(e -> e.getValue() != null).count());
    }
}