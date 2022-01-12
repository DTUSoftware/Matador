package dk.dtu.matador;

import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestDeedManager {
    PlayerManager pm = PlayerManager.getInstance();
    DeedManager dm = DeedManager.getInstance();
    @Test
    public void testdeedOwnership() {
        Player player = pm.createPlayer("Tester");

    }
}
