package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;

import java.util.UUID;

/**
 * Funktion af kort;
 * Ryk 1 felt frem,
 * eller tag et chancekort mere.
 */

public class MoveOrDrawCC  extends ChanceCard {
    /**
     * Initiates a new ChanceCard.
     */
    public MoveOrDrawCC() {
        super("moveordraw");
    }

    @Override
    public void doCardAction(UUID playerID) {
        GameManager gm = GameManager.getInstance();
        boolean move = GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("moveordraw_prompt"));
        if (!move) {
            ChanceCard cc = gm.getGameBoard().getChanceCard();
            cc.showCardMessage();
            cc.doCardAction(playerID);
        }
        else {
            gm.setPlayerBoardPosition(playerID, (gm.getPlayerPosition(playerID)+1) % gm.getGameBoard().getFieldAmount(), true);
        }
    }
}