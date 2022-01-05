package dk.dtu.matador;

import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {
    UUID gameID = UUID.randomUUID();
    GameManager gm = new GameManager(gameID);
    PlayerManager pm = PlayerManager.getInstance();

    @Test
    public void testGameSetup() {
        Player player1 = pm.createPlayer(gameID, "Test Tester");
        Player player2 = pm.createPlayer(gameID, "Test Tester2");
        gm.setupGame(new UUID[] {player1.getID(), player2.getID()});
        assertEquals(0, gm.getPlayerPosition(player1.getID()));
        assertEquals(0, gm.getPlayerPosition(player2.getID()));
    }

    @Test
    public void testPlayerPositions() {
        Player player1 = pm.createPlayer(gameID, "Test Tester3");
        Player player2 = pm.createPlayer(gameID, "Test Tester4");
        gm.setupGame(new UUID[] {player1.getID(), player2.getID()});
        gm.setPlayerPosition(player1.getID(), 30, false);
        assertEquals(30, gm.getPlayerPosition(player1.getID()));
        gm.setPlayerBoardPosition(player2.getID(), 20, false);
        assertEquals(20, gm.getPlayerPosition(player2.getID()));
    }
}
