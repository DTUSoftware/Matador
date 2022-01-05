package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.Player;

import java.awt.*;
import java.util.UUID;

public class JailField extends Field {
    private double jailBailOut = 1500.0;

    public JailField() {
        super(Color.ORANGE, "jail", true);
    }
    public JailField(double jailBailOut) {
        super(Color.ORANGE, "jail", true);
        this.jailBailOut = jailBailOut;
    }

    @Override
    public void doLandingAction(UUID playerID) {
        if (PlayerManager.getInstance().getPlayer(playerID).isJailed()) {
            getGUI().showMessage(getLanguageManager().getString("jailed"));
        }
        else {
            getGUI().showMessage(getLanguageManager().getString("visiting_jail"));
        }
    }

    @Override
    public void doLeavingAction(UUID playerID) {
        Player player = PlayerManager.getInstance().getPlayer(playerID);
        if (player.isJailed()) {
            // if the player has a bail card
            if (player.takeBailCard()) {
                getGUI().showMessage(getLanguageManager().getString("card_bailout"));
                player.unJail();
            }
            else {
                // if the player can pay bailout fees
                if (player.withdraw(jailBailOut)) {
                    getGUI().showMessage(getLanguageManager().getString("paid_bailout").replace("{amount}", Float.toString(Math.round(jailBailOut))));
                    player.unJail();
                }
                else {
                    getGUI().showMessage(getLanguageManager().getString("could_not_pay_bailout"));
                    getGameManager().finishGame();
                }
            }

        }
    }

    @Override
    public void reloadLanguage() {
        super.getGUIField().setTitle(getLanguageManager().getString("field_"+super.getFieldName()+"_name"));
        super.getGUIField().setDescription(getLanguageManager().getString("field_"+super.getFieldName()+"_description"));
    }
}
