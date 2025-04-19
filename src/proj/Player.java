import java.util.ArrayList;
import java.util.List;



public class Player {
	public enum PlayerType { HUMAN, COMPUTER }
	private String name;
	private List<Integer> gameScore;
	//3 times
	private int rollCount;
	
	//first all false
	private List<Boolean> keepFlags;
	private PlayerType type;
	
	public Player(String name, PlayerType type) {
        this.name = name;
        this.gameScore = new ArrayList<>();;
        this.rollCount = 0;
        this.type = type;
	    keepFlags = new ArrayList<>();
	}
	
	public PlayerType getType() {
		
		return type;
		
	}
	
	
	public String getName() {
        return name;
    }
	
	
	public List<Integer> getgameScore() {
        return new ArrayList<>(gameScore);
    }
	
	public int getGameScoreSum() {
        return gameScore.stream().mapToInt(Integer::intValue).sum();
    }
	

	
	public int getRollCount() {
        return rollCount;
    }
	
	public List<Boolean> getKeepFlags() {
		  return new ArrayList<>(keepFlags);
	}
	
	//add the score
	public void addGameScore(int points) {
        gameScore.add(points);
    }
	
	public void endGame() {
        gameScore.clear();
        resetRollCount();
    }
	
	
	public void incrementRollCount() {
        this.rollCount++;
    }
	
	public void resetRollCount() {
        this.rollCount = 0;
    }
	
	public boolean ifcanroll() {
		if (rollCount < 3){
			return true;
		}
		return false;
	}
	
	public void initializeKeepFlags(int diceCount) {
	    for (int i = 0; i < diceCount; i++) {
	        keepFlags.add(false);
	    }
	}
	
	public void selectDice(int diceIndex, boolean value) {
		if (diceIndex < 0 || diceIndex >= keepFlags.size()) {
			return;
		}
		keepFlags.set(diceIndex, value);
	}
	
	public void resetKeepFlags() {
		  for (int i = 0; i < keepFlags.size(); i++) {
		    keepFlags.set(i, false);
		  }
	}

}
