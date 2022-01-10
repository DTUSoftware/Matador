package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Garden extends ReceiveCC {
    private double gardenReceiveAmount = 1000.0;

    public Garden() {
        super("Garden");
    }
    public Garden(double gardenReceiveAmount) {
        super("Garden");
        this.gardenReceiveAmount = gardenReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(gardenReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("garden_receive_amount"));
    }
}
