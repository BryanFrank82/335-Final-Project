package proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Model {
	
	private Map<Player, Integer> playerWins = new HashMap<>();
	private Map<Player, Integer> playerLosses = new HashMap<>();
	private ArrayList<Player> allPlayers = new ArrayList<>();

	public void addPlayer(Player p) {
	    if (!allPlayers.contains(p)) {
	        allPlayers.add(p);
	    }
	}

	public void recordWin(Player p) {
	    playerWins.put(p, playerWins.getOrDefault(p, 0) + 1);
	}

	public void recordLoss(Player p) {
	    playerLosses.put(p, playerLosses.getOrDefault(p, 0) + 1);
	}

	public int getWins(Player p) {
	    return playerWins.getOrDefault(p, 0);
	}

	public int getLosses(Player p) {
	    return playerLosses.getOrDefault(p, 0);
	}

	public ArrayList<Player> getAllPlayers() {
	    return allPlayers;
	}


}
