import java.util.HashMap;
import java.util.Map;

public class DiceFactory {
	private static final Map<Integer, Dice> dicePool = new HashMap<>();

	public static Dice getDice(int id) {
		if (!dicePool.containsKey(id)) {
			dicePool.put(id, new Dice());
		}
		return dicePool.get(id);
	}
}
