package model;

import java.util.ArrayList;

public class Cup{
	private ArrayList<Dice> inCup;
	private ArrayList<Dice>outCup;
	
	public Cup(){
		inCup = new ArrayList<Dice>();
		for(int i = 0;i<5;i++) {
			Dice dice = new Dice();
			inCup.add(dice);
		}
		outCup = new ArrayList<Dice>();
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
	
	public void rollDice() {
		for(int i = 0;i<inCup.size();i++) {
			inCup.get(i).roll();
		}
	}
	
	public ArrayList<Dice> getInDice() {
		ArrayList<Dice> inCupCopy = inCup;
		return inCupCopy;
	}
	
	public ArrayList<Dice> getOutDice() {
		ArrayList<Dice> outCupCopy = outCup;
		return outCupCopy;
	}
}
