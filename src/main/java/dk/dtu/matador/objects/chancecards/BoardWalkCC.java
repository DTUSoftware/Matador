package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.Game;
import dk.dtu.matador.GameInstance;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

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
        GameInstance game = Game.getGameInstance(PlayerManager.getInstance().getPlayerGame(playerID));
        UUID boardWalk = game.getGameManager().getGameBoard().getFieldIDFromType("boardwalk");
        if (boardWalk == null) {
            GUIManager.getInstance().getGUI(game.getGUIID()).showMessage(game.getLanguageManager().getString("error_string"));
            System.out.println("Field ID is null!");
            return;
        }

        game.getGameManager().setPlayerBoardPosition(
                playerID,
                game.getGameManager().getGameBoard().getFieldPosition(boardWalk),
                true
        );
    }
}
