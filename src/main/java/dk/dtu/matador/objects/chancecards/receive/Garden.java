package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Garden extends ReceiveCC {
    private double gardenReceiveAmount = 200.0;

    public Garden() {
        super("Garden");
    }
    public Garden(double gardenReceiveAmount) {
        super("Garden");
        this.gardenReceiveAmount = gardenReceiveAmount;
    }

    public void doCardAction(UUID playerID) {
        double money = gardenReceiveAmount;

        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
