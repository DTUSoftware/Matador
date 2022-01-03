package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.objects.chancecards.ChanceCard;

import java.awt.*;
import java.util.UUID;

public class ChanceField extends Field {
    public ChanceField() {
        super(Color.ORANGE, "chance", true);
    }

    @Override
    public void doLandingAction(UUID playerID) {
        ChanceCard cc = GameManager.getInstance().getGameBoard().getChanceCard();
        cc.showCardMessage();
        cc.doCardAction(playerID);
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
