package proj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Dice {
	private static final List<Value> numbers = Collections.unmodifiableList(Arrays.asList(Value.values()));
	private Value currentValue;
	private final Random RANDOM = new Random();
	
	public Dice() {
		
	}
	
	public void roll() {
		currentValue = numbers.get(RANDOM.nextInt(0,6));
	}
	
	public Value getCurrentValue() {
		return currentValue;
	}
}
