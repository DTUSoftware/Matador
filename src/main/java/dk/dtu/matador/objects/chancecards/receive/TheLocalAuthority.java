package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class TheLocalAuthority extends ReceiveCC {
    private double localAuthorityReceiveAmount = 3000.0;

    public TheLocalAuthority() {
        super("TheLocalAuthority");
    }
    public TheLocalAuthority(double localAuthorityReceiveAmount) {
        super("TheLocalAuthority");
        this.localAuthorityReceiveAmount = localAuthorityReceiveAmount;
    }

    public void doCardAction(UUID playerID) {
        double money = localAuthorityReceiveAmount;

        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
