package model;

import java.util.ArrayList;

public class Cup{
	private ArrayList<Dice> inCup;
	private ArrayList<Dice>outCup;
	
	public Cup(){
		for(int i = 0;i<5;i++) {
			Dice dice = new Dice();
			inCup.add(dice);
		}
	}
	
	public void addDiceToCup(int pos) {
		if(pos <= outCup.size()) {
			Dice dice = outCup.remove(pos);
			inCup.add(dice);
		}
	}
	
	public void removeDiceFromCup(int pos) {
		if(pos <= inCup.size()) {
			Dice dice = inCup.remove(pos);
			outCup.add(dice);
		}
	}
	
	public void rollDicex() {
		for(int i = 0;i<inCup.size();i++) {
			inCup.get(i).roll();
		}
	}
}
