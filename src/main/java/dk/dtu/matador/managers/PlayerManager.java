package dk.dtu.matador.managers;

import dk.dtu.matador.objects.Player;
import gui_fields.GUI_Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * The PlayerManager keeps track of the players.
 */
public class PlayerManager {
    private static PlayerManager playerManager;

    private final HashMap<UUID, Player> players = new HashMap<>();
    private final HashMap<UUID, GUI_Player> guiPlayers = new HashMap<>();

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
    public Player createPlayer(String name) {
        Player player = new Player(name);
        UUID playerID = player.getID();
        players.put(playerID, player);

        GUI_Player guiPlayer = null;
        if (GUIManager.getInstance().guiInitialized()) {
            guiPlayer = GUIManager.getInstance().createGUIPlayer(player.getName(), player.getBalance());
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
    public Player createPlayer(String name, double startingBalance) {
        Player player = new Player(name, startingBalance);
        UUID playerID = player.getID();
        players.put(playerID, player);

        GUI_Player guiPlayer = null;
        if (GUIManager.getInstance().guiInitialized()) {
            guiPlayer = GUIManager.getInstance().createGUIPlayer(player.getName(), player.getBalance());
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
    public UUID[] getPlayerIDs() {
        return players.keySet().toArray(new UUID[0]);
    }
}
