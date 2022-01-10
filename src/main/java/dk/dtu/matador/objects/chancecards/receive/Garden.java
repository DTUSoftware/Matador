package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Garden extends ReceiveCC {
    private double gardenReceiveAmount = 1000.0;

    public Garden() {
        super("garden", 1000.0);
    }
    public Garden(double gardenReceiveAmount) {
        super("garden", gardenReceiveAmount);
        this.gardenReceiveAmount = gardenReceiveAmount;
    }
}
