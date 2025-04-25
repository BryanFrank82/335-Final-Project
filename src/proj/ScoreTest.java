package proj;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {

    // Since Dice doesn't have a constructor to set value, we set it via reflection
    private Dice setDice(Value val) {
        Dice d = new Dice();
        try {
            java.lang.reflect.Field field = Dice.class.getDeclaredField("currentValue");
            field.setAccessible(true);
            field.set(d, val);
        } catch (Exception e) {
            fail("Failed to set Dice value using reflection.");
        }
        return d;
    }

    @Test
    public void testYahtzeeScore() {
        ArrayList<Dice> dice = new ArrayList<>(List.of(
            setDice(Value.FIVE), setDice(Value.FIVE), setDice(Value.FIVE),
            setDice(Value.FIVE), setDice(Value.FIVE)
        ));

        Score score = new Score();
        Map<String, Integer> result = score.evaluate(dice);
        assertEquals(50, result.get("Yahtzee"));
    }

    @Test
    public void testFullHouseScore() {
        ArrayList<Dice> dice = new ArrayList<>(List.of(
            setDice(Value.TWO), setDice(Value.TWO), setDice(Value.THREE),
            setDice(Value.THREE), setDice(Value.THREE)
        ));

        Score score = new Score();
        Map<String, Integer> result = score.evaluate(dice);
        assertEquals(25, result.get("Full House"));
    }

    @Test
    public void testSmallStraightScore() {
        ArrayList<Dice> dice = new ArrayList<>(List.of(
            setDice(Value.TWO), setDice(Value.THREE), setDice(Value.FOUR),
            setDice(Value.FIVE), setDice(Value.TWO)
        ));

        Score score = new Score();
        Map<String, Integer> result = score.evaluate(dice);
        assertEquals(30, result.get("Small Straight"));
    }

    @Test
    public void testChanceScore() {
        ArrayList<Dice> dice = new ArrayList<>(List.of(
            setDice(Value.ONE), setDice(Value.TWO), setDice(Value.THREE),
            setDice(Value.FOUR), setDice(Value.FIVE)
        ));

        Score score = new Score();
        Map<String, Integer> result = score.evaluate(dice);
        assertEquals(15, result.get("Chance"));
    }
}
