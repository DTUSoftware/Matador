package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class PremiumBond extends ReceiveCC {
    private double bondReceiveAmount = 1000.0;

    public PremiumBond() {
        super("PremiumBond");
    }
    public PremiumBond(double bondReceiveAmount) {
        super("PremiumBond");
        this.bondReceiveAmount = bondReceiveAmount;
    }

    public void doCardAction(UUID playerID) {
        double money = bondReceiveAmount;

        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
