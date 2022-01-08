package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class OldFurniture extends ReceiveCC {
    private double furnitureReceiveAmount = 1000.0;

    public OldFurniture() {
        super("OldFurniture");
    }
    public OldFurniture(double furnitureReceiveAmount) {
        super("OldFurniture");
        this.furnitureReceiveAmount = furnitureReceiveAmount;
    }

    public void doCardAction(UUID playerID) {
        double money = furnitureReceiveAmount;

        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
