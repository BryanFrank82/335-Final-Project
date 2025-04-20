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
	
	private void removeSimilar(ArrayList<Dice> inDice, int value) {
		ArrayList<Dice> toMove = new ArrayList<>();
		for (int i = 0; i<inDice.size(); i++) {
			Dice curr = inDice.get(i);
			if (curr.getCurrentValue().ordinal() == value) {
				toMove.add(curr);
			}
		}
		for (Dice d: toMove) {
			cup.removeDiceFromCup(d);
		}
	}
	
	private void keepDice(ArrayList<Dice> roll) {
		ArrayList<Dice> inDice = cup.getInDice();
		int[] freqIn = new int[6]; // index 0 = ONE, 5 = SIX
		int[] freqTotal = new int[6];
		for (Dice d : inDice) {
		    freqIn[d.getCurrentValue().ordinal()]++;
		}
		for (Dice d : roll) {
		    freqTotal[d.getCurrentValue().ordinal()]++;
		}
		for (int i=0 ; i<freqIn.length; i++) {
			// inCount >= 3
		    if (freqIn[i] > 2) {
		        removeSimilar(inDice, i);
		        return;
		    }
		}
		for (int i=0 ; i<freqIn.length; i++) {
			// inCount >= 2
		    if (freqIn[i] > 1) {
		        removeSimilar(inDice, i);
		        return;
		    }
		}
		for (int i=0 ; i<freqTotal.length; i++) {
			// totalCount >= 3
		    if (freqTotal[i] > 2) {
		        removeSimilar(inDice, i);
		        return;
		    }
		}
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
			// if chosen score is atleast 14 it will select it
			if (choice != null && (Integer)choice.get(1) >= 14) {
				scoreboard.setScore((String)choice.get(0), (Integer)choice.get(1));
				break;
			} else {
				// select dice to keep, reroll, and increment count
				keepDice(roll);
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
