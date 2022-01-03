package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;

import java.util.UUID;

/**
 * Funktion af kort;
 * Ryk op til
 * 5 felter frem.
 */

public class MoveFieldsCC  extends ChanceCard {
    /**
     * Initiates a new ChanceCard.
     */
    public MoveFieldsCC() {
        super("movefields");
    }

    @Override
    public void doCardAction(UUID playerID) {
        int moveAmount = GUIManager.getInstance().askNumber(1, 5);
        GameManager gm = GameManager.getInstance();
        gm.setPlayerBoardPosition(playerID, (gm.getPlayerPosition(playerID)+moveAmount) % gm.getGameBoard().getFieldAmount(), true);
    }
}