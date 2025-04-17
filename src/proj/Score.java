package proj;

import java.util.*;

// to evaluate the possible score for each category. 
// return: Map <String, int> String: category, int: score
public class Score {

	// return MAP <category, score> for all the category. 
	public Map<String, Integer> evaluate(ArrayList<Dice> diceList) {
		int[] dice = convertDiceToValues(diceList);

		Map<String, Integer> scores = new LinkedHashMap<>();
		scores.put("Ones", scoreUpper(dice, 1));
		scores.put("Twos", scoreUpper(dice, 2));
		scores.put("Threes", scoreUpper(dice, 3));
		scores.put("Fours", scoreUpper(dice, 4));
		scores.put("Fives", scoreUpper(dice, 5));
		scores.put("Sixes", scoreUpper(dice, 6));
		scores.put("Three of a Kind", scoreThreeOfAKind(dice));
		scores.put("Four of a Kind", scoreFourOfAKind(dice));
		scores.put("Full House", scoreFullHouse(dice));
		scores.put("Small Straight", scoreSmallStraight(dice));
		scores.put("Large Straight", scoreLargeStraight(dice));
		scores.put("Yahtzee", scoreYahtzee(dice));
		scores.put("Chance", scoreChance(dice));

		return scores;
	}

	
	// convert the value to integer for later use
	private int[] convertDiceToValues(List<Dice> diceList) {
		int[] values = new int[diceList.size()];
		for (int i = 0; i < diceList.size(); i++) {
			Value val = diceList.get(i).getCurrentValue();
			if (val == Value.ONE)
				values[i] = 1;
			else if (val == Value.TWO)
				values[i] = 2;
			else if (val == Value.THREE)
				values[i] = 3;
			else if (val == Value.FOUR)
				values[i] = 4;
			else if (val == Value.FIVE)
				values[i] = 5;
			else if (val == Value.SIX)
				values[i] = 6;
		}
		return values;
	}

	
	private int scoreUpper(int[] dice, int target) {
		int score = 0;
		for (int d : dice) {
			if (d == target) {
				score += d;
			}
		}
		return score;
	}

	private int scoreThreeOfAKind(int[] dice) {
		for (int i = 1; i <= 6; i++) {
			int count = 0;
			for (int d : dice) {
				if (d == i)
					count++;
			}
			if (count >= 3) {
				int total = 0;
				for (int d : dice)
					total += d;
				return total;
			}
		}
		return 0;
	}

	private int scoreFourOfAKind(int[] dice) {
		for (int i = 1; i <= 6; i++) {
			int count = 0;
			for (int d : dice) {
				if (d == i)
					count++;
			}
			if (count >= 4) {
				int total = 0;
				for (int d : dice)
					total += d;
				return total;
			}
		}
		return 0;
	}

	private int scoreFullHouse(int[] dice) {
		int[] counts = new int[7];
		for (int d : dice)
			counts[d]++;

		boolean hasThree = false;
		boolean hasTwo = false;

		for (int i = 1; i <= 6; i++) {
			if (counts[i] == 3) {
				hasThree = true;
			} else {
				if (counts[i] == 2)
					hasTwo = true;
			}
		}

		return (hasThree && hasTwo) ? 25 : 0;
	}

	private int scoreSmallStraight(int[] dice) {
		boolean[] present = new boolean[7]; // 1 to 6
		for (int d : dice)
			present[d] = true;

		if ((present[1] && present[2] && present[3] && present[4])
				|| (present[2] && present[3] && present[4] && present[5])
				|| (present[3] && present[4] && present[5] && present[6])) {
			return 30;
		}
		return 0;
	}

	private int scoreLargeStraight(int[] dice) {
		boolean[] present = new boolean[7];
		for (int d : dice)
			present[d] = true;

		if ((present[1] && present[2] && present[3] && present[4] && present[5])
				|| (present[2] && present[3] && present[4] && present[5] && present[6])) {
			return 40;
		}
		return 0;
	}

	private int scoreYahtzee(int[] dice) {
		int first = dice[0];
		for (int i = 1; i < dice.length; i++) {
			if (dice[i] != first)
				return 0;
		}
		return 50;
	}

	private int scoreChance(int[] dice) {
		int total = 0;
		for (int d : dice)
			total += d;
		return total;
	}
}
