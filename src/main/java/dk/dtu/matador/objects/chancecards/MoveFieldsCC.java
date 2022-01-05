package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.Game;
import dk.dtu.matador.GameInstance;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.PlayerManager;

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
        GameInstance game = Game.getGameInstance(PlayerManager.getInstance().getPlayerGame(playerID));
        int moveAmount = GUIManager.getInstance().getGUI(game.getGUIID()).askNumber(1, 5);
        GameManager gm = game.getGameManager();
        gm.setPlayerBoardPosition(playerID, (gm.getPlayerPosition(playerID)+moveAmount) % gm.getGameBoard().getFieldAmount(), true);
    }
}