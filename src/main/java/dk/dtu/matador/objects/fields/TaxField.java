package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.objects.Account;
import dk.dtu.matador.objects.Player;
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

        GUIManager gui = GUIManager.getInstance();
        boolean answer = gui.askTaxField();
        
        Player player = PlayerManager.getInstance().getPlayer(playerID);

        if (answer == true ) {
            player.withdraw(4000);
        } else {
            double playerBalanceWithDeeds = player.getBalance();
            playerBalanceWithDeeds += player.getDeedBalance();
            player.withdraw(playerBalanceWithDeeds * 0.1);
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
