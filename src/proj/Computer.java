package proj;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Computer {
    public enum Difficulty {
        EASY, HARD
    }
	private Difficulty mode;
	private Cup cup;
	
	public Computer(Difficulty mode) {
		this.mode = mode;
		this.cup = new Cup();
	}
	
	public ArrayList<Object> choose(ArrayList<Dice> roll) {
		// finalChoice = [String, Integer] representation of score choice made
		ArrayList<Object> finalChoice = new ArrayList<>();
		Score choices = new Score();
		String maxScore = null;
		Map<String, Integer> scores = choices.evaluate(roll);
		// if no possible scoring exhists
		if (scores.size() == 0) {
			return null;
		}
		// finds score choice with the maximum points
		if (mode == Difficulty.HARD) {
			int max = 0;
			for (Map.Entry<String, Integer> entry : scores.entrySet()) {
	            if (entry.getValue() > max) {
	                max = entry.getValue();
	                maxScore = entry.getKey();
	            }
			}
			finalChoice.add(maxScore);
			finalChoice.add(max);
			return finalChoice;
		// chooses a random score choice
		} else {
			Random random = new Random();
			ArrayList<String> possibleScores = new ArrayList<>(scores.keySet());
	        int randIndex = random.nextInt(possibleScores.size());
	        maxScore = possibleScores.get(randIndex);
	        Integer value = scores.get(maxScore);
	        finalChoice.add(maxScore);
			finalChoice.add(value);
			return finalChoice;
		}
		
	}
	
	public void roll(Dice dice, Scoreboard scoreboard) {
		int rollCount = 1;
		this.cup.rollDice();
		ArrayList<Dice> roll = new ArrayList<>();
		roll.addAll(cup.getInDice());
		roll.addAll(cup.getOutDice());
		ArrayList<Object> choice = choose(roll);
		while (rollCount<3) {
			if (choice != null) {
				scoreboard.setScore((String)choice.get(0), (Integer)choice.get(1));
				break;
			} else {
				// reroll and increment count
				rollCount += 1;
				this.cup.rollDice();
				roll = new ArrayList<>();
				roll.addAll(cup.getInDice());
				roll.addAll(cup.getOutDice());
				choice = choose(roll);
			}
		}
		// last roll
		if (rollCount == 3) {
			scoreboard.setScore((String)choice.get(0), (Integer)choice.get(1));
		}
	}
}
