package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.Game;
import dk.dtu.matador.GameInstance;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

/**
 * GRATIS FELT!
 * Ryk frem til Skaterparken
 * for at lave det perfekte grind!
 * Hvis ingen ejer den,
 * får du den GRATIS!
 * Ellers skal du BETALE
 * leje til ejeren
 */

public class SkateparkCC extends ChanceCard {
    /**
     * Initiates a new ChanceCard.
     */
    public SkateparkCC() {
        super("skatepark");
    }

    @Override
    public void doCardAction(UUID playerID) {
        GameInstance game = Game.getGameInstance(PlayerManager.getInstance().getPlayerGame(playerID));
        UUID skate_park = game.getGameManager().getGameBoard().getFieldIDFromType("skate_park");
        if (skate_park == null) {
            GUIManager.getInstance().getGUI(game.getGUIID()).showMessage(game.getLanguageManager().getString("error_string"));
            return;
        }

        game.getGameManager().setPlayerBoardPosition(
                playerID,
                game.getGameManager().getGameBoard().getFieldPosition(skate_park),
                true,
                true
        );
    }
}