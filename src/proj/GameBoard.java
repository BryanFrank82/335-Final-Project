import java.util.ArrayList;
import java.util.List;


public class GameBoard {
	private List<Player> players;
	private int currentIndex;
	
	
	
	 public GameBoard() {
	        this.players = new ArrayList<>();
	        this.currentIndex = 0;
	    }
	 
	 public void addPlayer(Player player) {
	        players.add(player);
	    }
	 
	 
	 public Player getCurrentPlayer() {
	        if (players.isEmpty()) return null;
	        return players.get(currentIndex);
	    }
	 
	 public void resetTurns() {
	        currentIndex = 0;
	    }
	 
	 public List<Player> getPlayers() {
	        return new ArrayList<>(players);
	    }
	 
	 public boolean hasnext(){
		 return currentIndex < players.size();
	 }
	 
	 public boolean nextplayer() {
		 if (hasnext()) {
			 currentIndex += 1;
			 return true;
		 }
		 return false;
	 }

}
