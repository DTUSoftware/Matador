package dk.dtu.matador.managers;

import dk.dtu.matador.Game;
import dk.dtu.matador.objects.GameGUI;
import dk.dtu.matador.objects.Player;
import gui_fields.GUI_Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * The PlayerManager keeps track of the players.
 */
public class PlayerManager {
    private static PlayerManager playerManager;

    private final HashMap<UUID, ArrayList<UUID>> gamePlayers = new HashMap<>();  // gameID, Player[]
    private final HashMap<UUID, UUID> playerGames = new HashMap<>(); // this might be a tad excessive, but better
                                                                     // than looping through the hashmap above...
                                                                     // playerID, gameID
    private final HashMap<UUID, Player> players = new HashMap<>();  // playerID, Player
    private final HashMap<UUID, GUI_Player> guiPlayers = new HashMap<>();  // playerID, GUI_Player

    private PlayerManager() {}

    public static PlayerManager getInstance() {
        if (playerManager == null) {
            playerManager = new PlayerManager();
        }

        return playerManager;
    }

    /**
     * Creates a new player with a given name.
     *
     * @param name  The name of the player.
     * @return      The created Player object.
     */
    public Player createPlayer(UUID gameID, String name) {
        if (!gamePlayers.containsKey(gameID)) {
            gamePlayers.put(gameID, new ArrayList<>());
        }

        Player player = new Player(name);
        UUID playerID = player.getID();
        players.put(playerID, player);
        gamePlayers.get(gameID).add(playerID);
        playerGames.put(playerID, gameID);

        GUI_Player guiPlayer = null;
        UUID guiID = Game.getGameInstance(gameID).getGUIID();
        GameGUI gui = GUIManager.getInstance().getGUI(guiID);
        if (gui != null) {
            guiPlayer = gui.createGUIPlayer(player.getName(), player.getBalance());
        }
        guiPlayers.put(playerID, guiPlayer);

        return player;
    }

    /**
     * Creates a new player with a given name and a starting balance.
     *
     * @param name              The name of the player.
     * @param startingBalance   The starting balance of the player.
     * @return                  The created Player object.
     */
    public Player createPlayer(UUID gameID, String name, double startingBalance) {
        if (!gamePlayers.containsKey(gameID)) {
            gamePlayers.put(gameID, new ArrayList<>());
        }

        Player player = new Player(name, startingBalance);
        UUID playerID = player.getID();
        players.put(playerID, player);
        gamePlayers.get(gameID).add(playerID);
        playerGames.put(playerID, gameID);

        GUI_Player guiPlayer = null;
        UUID guiID = Game.getGameInstance(gameID).getGUIID();
        GameGUI gui = GUIManager.getInstance().getGUI(guiID);
        if (gui != null) {
            guiPlayer = gui.createGUIPlayer(player.getName(), player.getBalance());
        }
        guiPlayers.put(playerID, guiPlayer);

        return player;
    }

    /**
     * Gets a player using the UUID of the player.
     *
     * @param playerID  The UUID of the player.
     * @return          The Player object.
     * @throws NullPointerException If the player with the given UUID isn't found.
     */
    public Player getPlayer(UUID playerID) {
        return players.get(playerID);
    }

    public UUID getPlayerGame(UUID playerID) {
        return playerGames.get(playerID);
    }

    /**
     * Gets a GUI_Player object using the UUID of the player.
     *
     * @param playerID  The UUID of the player.
     * @return          The GUI_Player object.
     * @throws NullPointerException If the GUI player with the given player UUID isn't found.
     */
    public GUI_Player getGUIPlayer(UUID playerID) {
        return guiPlayers.get(playerID);
    }

    /**
     * Gets an array with all of the player UUID's.
     * @return An array with player UUID's.
     */
    public UUID[] getPlayerIDs(UUID gameID) {
        return gamePlayers.get(gameID).toArray(new UUID[0]);
    }
}
