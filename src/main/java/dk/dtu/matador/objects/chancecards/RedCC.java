package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.Game;
import dk.dtu.matador.GameInstance;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.awt.*;
import java.util.UUID;

/**
 * Funktion af kort;
 * Ryk frem til et rødt felt.
 * Hvis det er ledigt, får du det GRATIS
 * Ellers skal du BETALE leje til ejeren
 */

public class RedCC extends ChanceCard {
    /**
     * Initiates a new ChanceCard.
     */
    public RedCC() {
        super("_chancecard_messagered");
    }

    @Override
    public void doCardAction(UUID playerID) {
        GameInstance game = Game.getGameInstance(PlayerManager.getInstance().getPlayerGame(playerID));
        UUID fieldID = game.getGameManager().getGameBoard().getNextFieldIDWithColor(
                playerID,
                new Color[] { Color.RED }
        );
        if (fieldID == null) {
            GUIManager.getInstance().getGUI(game.getGUIID()).showMessage(game.getLanguageManager().getString("error_string"));
            System.out.println("Field ID is null!");
            return;
        }

        game.getGameManager().setPlayerBoardPosition(
                playerID,
                game.getGameManager().getGameBoard().getFieldPosition(fieldID),
                true,
                true
        );
    }
}