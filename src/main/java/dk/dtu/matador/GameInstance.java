package dk.dtu.matador;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.GameGUI;
import dk.dtu.matador.objects.Player;

import java.util.UUID;

public class GameInstance {
    private UUID gameID;
    private UUID guiID;
    private GameManager gameM;
    private LanguageManager lm = new LanguageManager();
    private static GUIManager guiM = GUIManager.getInstance();
    private static PlayerManager pm = PlayerManager.getInstance();

    public GameInstance() {
        gameID = UUID.randomUUID();

        gameM = new GameManager(gameID);

        Game.logDebug("Fields array: " + gameM.getGameBoard().fieldsToString());

        // Initialize the GUI
        GameGUI gui = guiM.newGUI(gameM.getGameBoard().getGUIFields());
        guiID = gui.getGUIID();

        // Ask for language and reload the GameBoard
        gui.askLanguage();
        gameM.getGameBoard().reloadLanguage();

        // Create players
        int amount_of_players = gui.askPlayers();
        for (int i = 1; i <= amount_of_players; i++) {
            String playerName = gui.getUserString(lm.getString("enter_player_name").replace("{player_number}", Integer.toString(i)));
            Player player = pm.createPlayer(gameID, playerName, Game.getStartBalance());

            // Place player at start
            gui.movePlayerField(player.getID(), 0);
        }

        do {
            // Add players to the game queue
            gameM.setupGame(pm.getPlayerIDs(gameID));

            // Play the game
            gameM.play();
        }
        while (gui.askPrompt(lm.getString("play_again")));

        gui.closeGUI();
    }

    public UUID getGameID() {
        return gameID;
    }

    public UUID getGUIID() {
        return guiID;
    }

    public GameManager getGameManager() {
        return gameM;
    }

    public LanguageManager getLanguageManager() {
        return lm;
    }
}
