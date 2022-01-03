package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;

import java.util.UUID;

/**
 * Ryk frem til
 * Strandpromenaden.
 */
public class BoardWalkCC extends ChanceCard {
    /**
     * Initiates a new ChanceCard.
     */
    public BoardWalkCC() {
        super("boardwalk");
    }

    @Override
    public void doCardAction(UUID playerID) {
        UUID boardWalk = GameManager.getInstance().getGameBoard().getFieldIDFromType("boardwalk");
        if (boardWalk == null) {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
            System.out.println("Field ID is null!");
            return;
        }

        GameManager.getInstance().setPlayerBoardPosition(
                playerID,
                GameManager.getInstance().getGameBoard().getFieldPosition(boardWalk),
                true
        );
    }
}
