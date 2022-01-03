package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.awt.*;
import java.util.UUID;

public class GoToJailField extends Field {
    public GoToJailField() {
        super(Color.BLUE, "go_to_jail", true);
    }

    @Override
    public void doLandingAction(UUID playerID) {
        // Move the player to jail
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("go_to_jail_message"));
        PlayerManager.getInstance().getPlayer(playerID).jail();
        GameManager.getInstance().setPlayerBoardPosition(playerID, GameManager.getInstance().getGameBoard().getFieldPosition(GameManager.getInstance().getGameBoard().getFieldIDFromType("JailField")), false);
    }

    @Override
    public void doLeavingAction(UUID playerID) {

    }

    @Override
    public void reloadLanguage() {
        super.getGUIField().setTitle(LanguageManager.getInstance().getString("field_"+super.getFieldName()+"_name"));
        super.getGUIField().setDescription(LanguageManager.getInstance().getString("field_"+super.getFieldName()+"_description"));
    }
}
