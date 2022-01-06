package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.objects.Account;
import dk.dtu.matador.managers.DeedManager;

import java.awt.*;
import java.util.UUID;

public class TaxField extends Field {
    /**
     * Creates a field, and sets up its respective field on the GUI.
     */
    public TaxField(String taxSubtype) {
        super(Color.PINK, taxSubtype, true);
    }

    @Override
    public void doLandingAction(UUID playerID) {

        boolean answer = askTaxField();

        if (answer == true ) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(4000);
        } else {
            double playerBalanceWithDeeds = getBalance(PlayerManager.getInstance().getPlayer(playerID));
            playerBalanceWithDeeds = getDeedBalance(PlayerManager.getInstance().getPlayer(playerID));
            PlayerManager.getInstance().getPlayer(playerID).withdraw(playerBalanceWithDeeds * 0.1);
        }
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
