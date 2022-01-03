package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;

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
        UUID skate_park = GameManager.getInstance().getGameBoard().getFieldIDFromType("skate_park");
        if (skate_park == null) {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
            return;
        }

        GameManager.getInstance().setPlayerBoardPosition(
                playerID,
                GameManager.getInstance().getGameBoard().getFieldPosition(skate_park),
                true,
                true
        );
    }
}