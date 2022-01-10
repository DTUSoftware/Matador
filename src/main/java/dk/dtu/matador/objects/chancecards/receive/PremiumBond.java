package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class PremiumBond extends ReceiveCC {
    private double premiumBondReceiveAmount = 1000.0;

    public PremiumBond() {
        super("PremiumBond");
    }
    public PremiumBond(double premiumBondReceiveAmount) {
        super("PremiumBond");
        this.premiumBondReceiveAmount = premiumBondReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(premiumBondReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("premiumBond_receive_amount"));
    }
}
