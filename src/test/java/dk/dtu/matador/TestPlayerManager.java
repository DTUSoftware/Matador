package dk.dtu.matador;

import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerManager {
    PlayerManager pm = PlayerManager.getInstance();

    @Test
    public void testPlayerManager() {
        Player player = pm.createPlayer(UUID.randomUUID(), "Test Tester");

        assertEquals("Test Tester", player.getName());
        assertEquals(0.0, player.getBalance());

        assertFalse(player.withdraw(100));
        assertEquals(0, player.getBalance());

        player.deposit(100);
        assertEquals(100, player.getBalance());

        assertTrue(player.withdraw(100));
        assertEquals(0, player.getBalance());

        double bal = 1000000000000.0;
        player.setBalance(bal);
        assertEquals(bal, player.getBalance());;

        player.setBalance(100);
        assertEquals(100, player.getBalance());;
    }

    @Test
    public void testStartingBalance() {
        Player player = pm.createPlayer(UUID.randomUUID(), "Test Tester", 3000);
        assertEquals(3000, player.getBalance());
    }
}
