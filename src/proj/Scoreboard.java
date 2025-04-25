package proj;

import java.util.*;

public class Scoreboard {
    private final Map<String, Integer> scores;

    public Scoreboard() {
        scores = new LinkedHashMap<>(); // LinkedHashMap to make sure it is in order
        for (String category : List.of("Ones", "Twos", "Threes", "Fours", "Fives", "Sixes",
                                       "Three of a Kind", "Four of a Kind", "Full House",
                                       "Small Straight", "Large Straight", "Yahtzee", "Chance")) {
            scores.put(category, null);
        }
    }

    public boolean setScore(String category, int score) {
        if (scores.containsKey(category) && scores.get(category) == null) {
            scores.put(category, score);
            return true;
        }
        return false;
    }

    // return the score
    public Map<String, Integer> getScores() {
        return Collections.unmodifiableMap(scores);
    }

    // return the total score
    public int getTotalScore() {
    	int total = 0;
        for (Integer score : scores.values()) {
            if (score != null) {
                total += score;
            }
        }
        return total;
    }
    
    // return the list of the category that still empty(null)
    public ArrayList<String> getRemainingCategories() {
        ArrayList<String> remaining = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() == null) {
                remaining.add(entry.getKey());
            }
        }
        return remaining;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Scorecard:\n");
        for (var entry : scores.entrySet()) {
            String val = entry.getValue() == null ? "-" : entry.getValue().toString();
            sb.append(String.format("%-18s : %s\n", entry.getKey(), val));
        }
        sb.append("Total Score         : ").append(getTotalScore());
        return sb.toString();
    }
}