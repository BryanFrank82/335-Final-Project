package proj;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class GameBoard {
	private Map<Player, Integer> winCounts = new HashMap<>();
	private Map<Player, Integer> lossCounts = new HashMap<>();
	private List<Player> players;
	private int currentIndex;
	private final PlayerLibrary library;
	
	 public GameBoard(PlayerLibrary library) {
	        this.players = new ArrayList<>();
	        this.currentIndex = 0;
	        this.library = library;
	    }
	 
	 public boolean addPlayer(Player player) {
	        if (library.containsPlayer(player)) {
	            players.add(player);
	            return true;
	        }
	        return false;
	    }

	public int getCurrentIndex() {
		return currentIndex;
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
	 
	 
	 public void getOrder() {
		 List<Player> humans = new ArrayList<>();
	     List<Player> computers = new ArrayList<>();
	     
	     for (Player p : players) {
	            if (p.getType() == Player.PlayerType.HUMAN) {
	                humans.add(p);
	            } else {
	                computers.add(p);
	            }
	        }
	     
	     Map<Player, Integer> rollResults = new HashMap<>();
	     for (Player h : humans) {
	    	 Cup cup = new Cup();
	            cup.rollDice();
	            int sum = 0;
	            for (Dice d : cup.getInDice()) {
	            	sum += d.getCurrentValue().ordinal() + 1;
	            }
	            rollResults.put(h, sum);
	     }
	     humans.sort((a, b) -> rollResults.get(b) - rollResults.get(a));
	     players.clear();
	     players.addAll(humans);
	     players.addAll(computers);
		 
	 }
	 

		public void recordWin(Player p) {
		    winCounts.put(p, winCounts.getOrDefault(p, 0) + 1);
		}

		public void recordLoss(Player p) {
		    lossCounts.put(p, lossCounts.getOrDefault(p, 0) + 1);
		}

		public int getWins(Player p) {
		    return winCounts.getOrDefault(p, 0);
		}

		public int getLosses(Player p) {
		    return lossCounts.getOrDefault(p, 0);
		}


}
