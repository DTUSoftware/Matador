package dk.dtu.matador.objects.chancecards.move;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class GoToJail extends MoveToFieldCC{

    public GoToJail() {super("go_to_jail", "JailField", false);}

    @Override
    public void doCardAction(UUID playerID) {
        UUID fieldID = GameManager.getInstance().getGameBoard().getFieldIDFromType(super.getFieldName());
        if (fieldID == null) {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
            System.out.println("Field ID is null!");
            return;
        }

        PlayerManager.getInstance().getPlayer(playerID).jail();
        GameManager.getInstance().setPlayerBoardPosition(
                playerID,
                GameManager.getInstance().getGameBoard().getFieldPosition(fieldID),
                super.isGivenStartReward()
        );
    }
}
