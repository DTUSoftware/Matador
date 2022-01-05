package dk.dtu.matador;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Main class for the program
 */
public class Game {
    private final static double startingBalance = 30000.0;
    private static double startPassReward = 4000.0;
    public final static boolean debug = ((System.getenv("debug") != null) || (System.getProperty("debug") != null));

    private static HashMap<UUID, GameInstance> games = new HashMap<>();

    public static void main(String[] args) {
        // Create a new game
        GameInstance game = new GameInstance();
        games.put(game.getGameID(), game);
    }

    public static GameInstance getGameInstance(UUID gameID) {
        return games.get(gameID);
    }

    public static UUID[] getGameInstances() {
        return games.keySet().toArray(new UUID[0]);
    }

    public static void setStartPassReward(double startPassReward) {
        Game.startPassReward = startPassReward;
    }

    public static double getStartPassReward() {
        return startPassReward;
    }

    public static double getStartBalance() {
        return startingBalance;
    }

    public static void logDebug(String message) {
        System.out.println("[DEBUG] " + message);
    }
}
