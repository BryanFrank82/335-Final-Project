package proj;


import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerTest {

	@Test
	void testPlayer() {
		Player player1 = new Player("player1", Player.PlayerType.HUMAN);
		Player player2 = new Player("player2", Player.PlayerType.COMPUTER);
		assertEquals(player1.getType(),Player.PlayerType.HUMAN);
		assertEquals(player2.getType(),Player.PlayerType.COMPUTER);
		assertEquals(player1.getName(),"player1");
		assertEquals(player2.getName(),"player2");
		
		
	}
	
	
	@Test
    public void testAddAndGetGameScore() {
		Player player1 = new Player("player1", Player.PlayerType.HUMAN);
		player1.addGameScore(10);
        player1.addGameScore(15);
        List<Integer> scores = player1.getgameScore();
        assertEquals(2, scores.size());
        assertEquals(10, (int) scores.get(0));
        assertEquals(15, (int) scores.get(1));
        assertEquals(25, player1.getGameScoreSum());
	}
	
	@Test
    public void testRollCountLogic() {
		Player player1 = new Player("player1", Player.PlayerType.HUMAN);
        assertEquals(0, player1.getRollCount());
        assertTrue(player1.ifcanroll());

        player1.incrementRollCount();
        player1.incrementRollCount();
        player1.incrementRollCount();

        assertEquals(3, player1.getRollCount());
        assertFalse(player1.ifcanroll());

        player1.resetRollCount();
        assertEquals(0, player1.getRollCount());
        assertTrue(player1.ifcanroll());
    }
	
	
	@Test
    public void testKeepFlags() {
		Player player1 = new Player("player1", Player.PlayerType.HUMAN);
        player1.initializeKeepFlags(3);
        List<Boolean> flags = player1.getKeepFlags();
        assertEquals(3, flags.size());
        for (Boolean flag : flags) {
            assertFalse(flag);
        }

        player1.selectDice(1, true);
        flags = player1.getKeepFlags();
        assertTrue(flags.get(1));

        player1.resetKeepFlags();
        flags = player1.getKeepFlags();
        for (Boolean flag : flags) {
            assertFalse(flag);
        }
    }
	
	
	@Test
    public void testEndGameResetsState() {
		
		Player player1 = new Player("player1", Player.PlayerType.HUMAN);
        player1.addGameScore(10);
        player1.incrementRollCount();
        player1.endGame();

        assertEquals(0, player1.getgameScore().size());
        assertEquals(0, player1.getRollCount());
    }
	
	

}
