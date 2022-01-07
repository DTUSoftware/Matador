package dk.dtu.matador;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.Player;

/**
 * Main class for the program
 */
public class Game {
    private final static double startingBalance = 30000.0;
    private static double startPassReward = 4000.0;
    public static boolean debug = ((System.getenv("debug") != null) || (System.getProperty("debug") != null));

    public static void main(String[] args) {
        Game.logDebug("Fields array: " + GameManager.getInstance().getGameBoard().fieldsToString());

        // Assign GUI to variable, since we will be using it again
        GUIManager gm = GUIManager.getInstance();

        // Initialize the GUI
        gm.initializeGUI();

        // Ask for language and reload the GameBoard
        gm.askLanguage();
        GameManager.getInstance().getGameBoard().reloadLanguage();

        // Create players
        int amount_of_players = gm.askPlayers();
        for (int i = 1; i <= amount_of_players; i++) {
            String playerName = gm.getUserString(LanguageManager.getInstance().getString("enter_player_name").replace("{player_number}", Integer.toString(i)));

            // check for debug enable
            if (playerName.equals("cc_k0n4m1")) {
                debug = true;
                gm.showMessage("DEBUG MODE ENABLED!");
                playerName = gm.getUserString(LanguageManager.getInstance().getString("enter_player_name").replace("{player_number}", Integer.toString(i)));
            }

            Player player = PlayerManager.getInstance().createPlayer(playerName, startingBalance);

            // Place player at start
            gm.movePlayerField(player.getID(), 0);

        }

        do {
            // Add players to the game queue
            GameManager.getInstance().setupGame(PlayerManager.getInstance().getPlayerIDs());

            // Play the game
            GameManager.getInstance().play();
        }
        while (GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("play_again")));

        gm.closeGUI();
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
