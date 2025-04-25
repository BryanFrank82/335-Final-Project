package proj;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;


class GameBoardTest {
	private PlayerLibrary library;
    private GameBoard board;
    private Player human1;
    private Player human2;
    private Player computer;
    
    
    @BeforeEach
    public void setUp() {
    	
    	library = new PlayerLibrary();
        board = new GameBoard(library);

        human1 = new Player("Alice", Player.PlayerType.HUMAN);
        human2 = new Player("Bob", Player.PlayerType.HUMAN);
        computer = new Player("CPU", Player.PlayerType.COMPUTER);

        library.addPlayer(human1);
        library.addPlayer(human2);
        library.addPlayer(computer);
    	
    }
    
    @Test
    public void testAddPlayer() {
        assertTrue(board.addPlayer(human1));
        assertTrue(board.addPlayer(computer));
        Player unknown = new Player("Unknown", Player.PlayerType.HUMAN);
        assertFalse(board.addPlayer(unknown));
    }
    
    @Test
    void testGetCurrentPlayer() {
        board.addPlayer(human1);
        assertEquals(human1, board.getCurrentPlayer());
    }
    
    
    @Test
    void testNextPlayerFlow() {
        board.addPlayer(human1);
        board.addPlayer(human2);
        assertEquals(0, board.getCurrentIndex());
        assertTrue(board.hasnext());
        assertTrue(board.nextplayer());
        assertEquals(human2, board.getCurrentPlayer());
        assertTrue(board.hasnext());
        assertTrue(board.nextplayer()); 
        assertFalse(board.hasnext());
    }
    
    
    @Test
    void testResetTurns() {
        board.addPlayer(human1);
        board.addPlayer(human2);
        board.nextplayer();
        board.resetTurns();
        assertEquals(0, board.getCurrentIndex());
        assertEquals(human1, board.getCurrentPlayer());
    }
    
    
    @Test
    void testGetOrderHumanBeforeComputer() {
        board.addPlayer(human1);
        board.addPlayer(human2);
        board.addPlayer(computer);
        board.getOrder();
        List<Player> ordered = board.getPlayers();
        assertEquals(3, ordered.size());
        assertEquals(Player.PlayerType.HUMAN, ordered.get(0).getType());
        assertEquals(Player.PlayerType.HUMAN, ordered.get(1).getType());
        assertEquals(Player.PlayerType.COMPUTER, ordered.get(2).getType());
    }

    
    


}
