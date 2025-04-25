package proj;

import java.util.*;

public class ComputerHard extends Player implements Computer{
	private Cup cup;
	public ComputerHard() {
        super("Computer Hard", Player.PlayerType.COMPUTER);
    }
	// This method removes all dice of a select value
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
	/*
	 * This method helps decide which dice to keep. If there are 2+ similar
	 * dice in a roll or 3+ similar dice in total, it will transfer the
	 * corresponding dice.
	 */
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
	// detects if a score is special (bottom 6)
	private boolean isSpecial(String score) {
		Set<String> specialScores = new HashSet<>(Arrays.asList(
		        "Three of a Kind", "Four of a Kind", "Full House",
		        "Small Straight", "Large Straight", "Yahtzee"));
		return specialScores.contains(score); 
	}
	
	// returns a map of eligible special scores (bottom 6)
	private Map<String,Integer> findSpecial(Map<String, Integer> scores) {
		    Map<String, Integer> specials = new HashMap<>();
		    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
		        if (isSpecial(entry.getKey())) {
		            specials.put(entry.getKey(), entry.getValue());
		        }
		    }
		    return specials;
	}
	
	/*
	 * This method chooses between eligible scoring options depending
	 * on the difficulty level
	 */
	public ArrayList<Object> choose(ArrayList<Dice> roll, Scoreboard scoreboard) {
		// finalChoice = [String, Integer] representation of score choice made
		ArrayList<Object> finalChoice = new ArrayList<>();
		Score choices = new Score();
		String maxScore = null;
		ArrayList<String> remaining = scoreboard.getRemainingCategories();
		Map<String, Integer> scores = choices.evaluate(roll);
		// removes all scores that are already taken or aren't possible
		scores.entrySet().removeIf(e -> !remaining.contains(e.getKey()) 
				|| e.getValue() == 0);
		// if no possible scoring exhists
		if (scores.size() == 0) {
			return null;
		}
		// if a special score exhists, return the highest one (if above 10)
		if (!findSpecial(scores).isEmpty()) {
			int max = -1;
			for (Map.Entry<String, Integer> entry : scores.entrySet()) {
	            if (entry.getValue() > max) {
	                max = entry.getValue();
	                maxScore = entry.getKey();
	            }
			}
			if (max >= 10) {
				finalChoice.add(maxScore);
				finalChoice.add(max);
				return finalChoice;
			}
		}
		// else return the highest possible score
		int max = -1;
		for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxScore = entry.getKey();
            }
		}
		finalChoice.add(maxScore);
		finalChoice.add(max);
		return finalChoice;
	}

	
	/*
	 * This method rolls the die an decides whether to reroll or not
	 * base on scoring options and numeric potential
	 */
	public void roll(Scoreboard scoreboard) {
		int rollCount = 1;
		this.cup.rollDice();
		ArrayList<Dice> roll = new ArrayList<>();
		roll.addAll(cup.getInDice());
		roll.addAll(cup.getOutDice());
		ArrayList<Object> choice = choose(roll, scoreboard);
		while (rollCount<3) {
			// if chosen score is atleast 13 or is special (and > 10)
			if (choice != null && (((Integer)choice.get(1) >= 13) || 
					isSpecial((String)choice.get(0))))  {
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
				choice = choose(roll, scoreboard);
			}
		}
		// last roll
		if (rollCount == 3) {
			if (choice != null) {
				scoreboard.setScore((String)choice.get(0), (Integer)choice.get(1));
			} else {
				// randomly assigns zero score if no score is possible
				Random random = new Random();
				ArrayList<String> remaining = scoreboard.getRemainingCategories();
				int randIndex = random.nextInt(remaining.size());
				String category = remaining.get(randIndex);
				scoreboard.setScore(category, 0);
			}
		}
		this.cup.resetCup();
	}
	
	// TEMPORARY METHOD FOR FIXED ROLLING (TESTING)
	public void rollDiceForTesting(ArrayList<Dice> fixedDice) {
	    this.cup = new Cup() {
	        @Override
	        public ArrayList<Dice> getInDice() {
	            return fixedDice;
	        }

	        @Override
	        public void rollDice() {
	            // skip actual rolling
	        }
	    };
	}
	
	public ArrayList<Dice> getCurrentDice() {
	    ArrayList<Dice> allDice = new ArrayList<>();
	    allDice.addAll(cup.getInDice());
	    allDice.addAll(cup.getOutDice());
	    return allDice;
	}

	public void resetCup() {
	    this.cup = new Cup();
	}


}

