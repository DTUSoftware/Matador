package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class OldFurniture extends ReceiveCC {
    private double oldFurnitureReceiveAmount = 1000.0;

    public OldFurniture() {
        super("OldFurniture");
    }
    public OldFurniture(double oldFurnitureReceiveAmount) {
        super("OldFurniture");
        this.oldFurnitureReceiveAmount = oldFurnitureReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(oldFurnitureReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("old_furniture_receive_amount"));
    }
}
