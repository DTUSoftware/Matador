package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class OldFurniture extends ReceiveCC {
    private double oldFurnitureReceiveAmount = 1000.0;

    public OldFurniture() {
        super("oldFurniture", 1000.0);
    }
    public OldFurniture(double oldFurnitureReceiveAmount) {
        super("oldFurniture", oldFurnitureReceiveAmount);
        this.oldFurnitureReceiveAmount = oldFurnitureReceiveAmount;
    }
}
