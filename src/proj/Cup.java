package proj;

import java.util.ArrayList;

public class Cup{
	private ArrayList<Dice> inCup;
	private ArrayList<Dice>outCup;
	
	public Cup(){
		inCup = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			Dice dice = DiceFactory.getDice(i);
			inCup.add(dice);
		}
		outCup = new ArrayList<>();
	}
	
	public void addDiceToCup(int pos) {
		if(outCup.size() > 0) {
			Dice dice = outCup.remove(pos);
			inCup.add(dice);
		}
	}
	
	public void removeDiceFromCup(int pos) {
		if(inCup.size() > 0) {
			Dice dice = inCup.remove(pos);
			outCup.add(dice);
		}
	}
	// remove dice with select Dice instance
	public void removeDiceFromCup(Dice dice) {
		if(inCup.size() > 0) {
			inCup.remove(dice);
			outCup.add(dice);
		}
	}
	
	public void rollDice() {
		for(int i = 0;i<inCup.size();i++) {
			inCup.get(i).roll();
		}
	}
	
	public ArrayList<Dice> getInDice() {
		return new ArrayList<>(inCup);
	}

	public ArrayList<Dice> getOutDice() {
		return new ArrayList<>(outCup);
	}
	
	public void resetCup() {
		for (int i=0; i<outCup.size(); i++) {
			addDiceToCup(i);
		}
	}
}
