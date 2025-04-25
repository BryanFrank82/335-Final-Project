package proj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CupTest{
	@Test
	public void CheckCup() {
		Cup cup = new Cup();
		assertEquals(cup.getInDice().size(), 5);
		assertEquals(cup.getOutDice().size(), 0);
		
		cup.removeDiceFromCup(0);
		assertEquals(cup.getInDice().size(), 4);
		assertEquals(cup.getOutDice().size(), 1);
		
		cup.removeDiceFromCup(0);
		assertEquals(cup.getInDice().size(), 3);
		assertEquals(cup.getOutDice().size(), 2);
		
		cup.removeDiceFromCup(0);
		assertEquals(cup.getInDice().size(), 2);
		assertEquals(cup.getOutDice().size(), 3);
		
		cup.removeDiceFromCup(0);
		assertEquals(cup.getInDice().size(), 1);
		assertEquals(cup.getOutDice().size(), 4);
		
		cup.removeDiceFromCup(0);
		assertEquals(cup.getInDice().size(), 0);
		assertEquals(cup.getOutDice().size(), 5);
		
		cup.removeDiceFromCup(0);
		
		cup.addDiceToCup(0);
		assertEquals(cup.getInDice().size(), 1);
		assertEquals(cup.getOutDice().size(), 4);
		
		cup.addDiceToCup(0);
		assertEquals(cup.getInDice().size(), 2);
		assertEquals(cup.getOutDice().size(), 3);
		
		cup.addDiceToCup(0);
		assertEquals(cup.getInDice().size(), 3);
		assertEquals(cup.getOutDice().size(), 2);
		
		cup.addDiceToCup(0);
		assertEquals(cup.getInDice().size(), 4);
		assertEquals(cup.getOutDice().size(), 1);
		
		cup.addDiceToCup(0);
		assertEquals(cup.getInDice().size(), 5);
		assertEquals(cup.getOutDice().size(), 0);
		
		cup.addDiceToCup(0);
		
		cup.rollDice();
		
		cup.resetCup();
		assertEquals(cup.getInDice().size(), 5);
	}
}
