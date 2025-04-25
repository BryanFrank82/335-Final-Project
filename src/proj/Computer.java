package proj;

import java.util.ArrayList;

public interface Computer {
	// choose dice to keep
	ArrayList<Object> choose(ArrayList<Dice> roll, Scoreboard scoreboard);
	
	// roll until scored
	void roll(Scoreboard scoreboard);
	
}
