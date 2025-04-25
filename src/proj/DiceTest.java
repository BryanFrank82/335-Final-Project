package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class DiceTest {
	@Test
	public void checkDice() {
		Dice dice = new Dice();
		assertEquals(dice.getCurrentValue(), null);
		dice.roll();
		System.out.print(dice.getCurrentValue());
	}
}
