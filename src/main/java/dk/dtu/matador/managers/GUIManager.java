package dk.dtu.matador.managers;

import dk.dtu.matador.Game;
import dk.dtu.matador.GameInstance;
import dk.dtu.matador.objects.GameGUI;
import dk.dtu.matador.objects.fields.Field;
import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * The GUIManager class, for managing the GUI. The
 * GUIManager is a constructor which initializes a
 * single instance of the dtudiplom GUI interface.
 * <p>
 * Note: Based on https://github.com/DTUSoftware/25_del1/blob/master/src/main/java/dk/dtu/spil/GUIManager.java
 * and on https://github.com/DTUSoftware/25_del2/blob/master/src/main/java/dk/dtu/cdio2/managers/GUIManager.java
 *
 * @author DTUSoftware, Gruppe 25
 * @version %I%, %G%
 * @since v0.0.1
 */
public class GUIManager {
    private static GUIManager guiManager;
    private HashMap<UUID, GameGUI> guiMap = new HashMap<>();  // guiID, GUI

    /**
     * The GUIManager constructor initializes the GUI instance, and
     * create a new instance of the dtudiplom GUI.
     */
    private GUIManager() {
    }

    public static GUIManager getInstance() {
        if (guiManager == null) {
            guiManager = new GUIManager();
        }

        return guiManager;
    }

    public GameGUI newGUI(GUI_Field[] fields) {
        GameGUI gui = new GameGUI(fields);

        UUID guiID = UUID.randomUUID();
        guiMap.put(guiID, gui);

        return gui;
    }

    public GameGUI getGUI(UUID guiID) {
        return guiMap.get(guiID);
    }

    // TODO: this could probably have some performance issues, oops
    private GameInstance getGameInstance(UUID guiID) {
        for (UUID gameID : Game.getGameInstances()) {
            GameInstance game = Game.getGameInstance(gameID);
            if (game.getGUIID().equals(guiID)) {
                return game;
            }
        }
        return null;
    }

    public LanguageManager getLanguageManager(UUID guiID) {
        GameInstance game = getGameInstance(guiID);
        if (game != null) {
            return game.getLanguageManager();
        }
        return null;
    }

    public GameManager getGameManager(UUID guiID) {
        GameInstance game = getGameInstance(guiID);
        if (game != null) {
            return game.getGameManager();
        }
        return null;
    }
}

