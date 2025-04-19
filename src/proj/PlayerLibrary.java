package proj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerLibrary {
	//player and historical Score
	 private Map<Player, Integer> playerData;
	 
	 public PlayerLibrary() {
		 playerData = new HashMap<>();
	    }
	 
	 
	 public boolean addPlayer(Player player) {
	        if (playerData.containsKey(player)) {
	            return false;
	        }
	        playerData.put(player, 0);
	        return true;
	    }
	 
	 public boolean removePlayer(Player player) {
	        return playerData.remove(player) != null;
	    }
	 
	 public void addScore(Player player, int points) {
	        if (!playerData.containsKey(player)) {
	            return;
	        }
	        int newScore = playerData.get(player) + points;
	        playerData.put(player, newScore);
	    }
	 
	 public Integer getScore(Player player) {
	        return playerData.get(player);
	    }
	 
	 public List<Player> getRanking() {
	        return playerData.entrySet().stream()
	                .sorted(Map.Entry.<Player, Integer>comparingByValue().reversed())
	                .map(Map.Entry::getKey)
	                .collect(Collectors.toList());
	    }
	 
	 public boolean containsPlayer(Player player) {
	        return playerData.containsKey(player);
	    }
	 
	 public Integer getHistoryScore(Player player) {
	        return playerData.get(player);
	    }
	 
	 

}
