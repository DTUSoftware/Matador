package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Raise extends ReceiveCC {
    private double raiseReceiveAmount = 1000.0;

    public Raise() {
        super("Raise");
    }
    public Raise(double raiseReceiveAmount) {
        super("Raise");
        this.raiseReceiveAmount = raiseReceiveAmount;
    }

    public void doCardAction(UUID playerID) {
        double money = raiseReceiveAmount;

        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
