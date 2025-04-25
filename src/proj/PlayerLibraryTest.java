package proj;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
class PlayerLibraryTest {
	private PlayerLibrary library;
    private Player player1;
    private Player player2;
    private Player player3;
    
    @BeforeEach
    void setUp() {
        library = new PlayerLibrary();
        player1 = new Player("Alice", Player.PlayerType.HUMAN);
        player2 = new Player("Bob", Player.PlayerType.COMPUTER);
        player3 = new Player("Charlie", Player.PlayerType.HUMAN);
    }
    
    @Test
    void testAddPlayer() {
        assertTrue(library.addPlayer(player1));
        assertFalse(library.addPlayer(player1));
    }
    
    @Test
    void testRemovePlayer() {
        library.addPlayer(player1);
        assertTrue(library.removePlayer(player1));
        assertFalse(library.containsPlayer(player1));
    }
    
    
    @Test
    void testAddAndGetScore() {
        library.addPlayer(player1);
        assertEquals(0, library.getScore(player1));

        library.addScore(player1, 10);
        assertEquals(10, library.getScore(player1));

        library.addScore(player1, 5);
        assertEquals(15, library.getScore(player1));
    }
    
    @Test
    void testContainsPlayer() {
        library.addPlayer(player2);
        assertTrue(library.containsPlayer(player2));
        assertFalse(library.containsPlayer(player3));
    }
    
    @Test
    void testGetRankingOrder() {
        library.addPlayer(player1);
        library.addPlayer(player2);
        library.addPlayer(player3);

        library.addScore(player1, 20);
        library.addScore(player2, 30);
        library.addScore(player3, 10);

        List<Player> ranking = library.getRanking();
        assertEquals(player2, ranking.get(0));
        assertEquals(player1, ranking.get(1));
        assertEquals(player3, ranking.get(2));
    }
    
    
    
    @Test
    void testGetHistoryScore() {
        library.addPlayer(player1);
        library.addScore(player1, 50);
        assertEquals(50, library.getHistoryScore(player1));
    }
    
    
    

}
