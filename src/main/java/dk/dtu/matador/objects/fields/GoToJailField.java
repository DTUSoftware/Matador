package dk.dtu.matador.objects.fields;

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
        getGUI().showMessage(getLanguageManager().getString("go_to_jail_message"));
        PlayerManager.getInstance().getPlayer(playerID).jail();
        getGameManager().setPlayerBoardPosition(playerID, getGameBoard().getFieldPosition(getGameBoard().getFieldIDFromType("JailField")), false);
    }

    @Override
    public void doLeavingAction(UUID playerID) {

    }

    @Override
    public void reloadLanguage() {
        super.getGUIField().setTitle(getLanguageManager().getString("field_"+super.getFieldName()+"_name"));
        super.getGUIField().setDescription(getLanguageManager().getString("field_"+super.getFieldName()+"_description"));
    }
}
