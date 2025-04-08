package proj;
import java.util.Random;

public class Computer {
    public enum Difficulty {
        EASY, HARD
    }
	private Difficulty mode;
	
	public Computer(Difficulty mode) {
		this.mode = mode;
	}
	
	public Score choose(int[] roll) {
		Score maxChoice;
		Score[] scores = roll.getScores();
		// if no possible scoring exhists
		if (scores.size() == 0) {
			return null;
		}
		// finds score choice with the maximum points
		if (mode == Difficulty.HARD) {
			int max = 0;
			for (int i = 0; i<scores.length; i++) {
				int points = scores[i].calculate(roll);
				if (points > max) {
					max = points;
					maxChoice = scores[i];
				}
			}
		// chooses a random score choice
		} else {
			Random random = new Random();
	        int randIndex = random.nextInt(scores.length);
	        Score randomScore = scores[randIndex];
			return randomScore;
		}
		return maxChoice;
	}
	
	public void roll(Dice dice, Scoreboard scoreboard) {
		int rollCount = 1;
		int[] roll = dice.getRoll();
		Score choice = choose(roll);
		while (choice != null && rollCount<3) {
			if (choice != null) {
				scoreboard.score(choice);
				break;
			} else {
				// reroll and increment count
				rollCount += 1;
				roll = dice.getRoll();
				choice = choose(roll);
			}
		}
		// last roll
		if (rollCount == 3) {
			scoreboard.score(choice);
		}
	}
}
