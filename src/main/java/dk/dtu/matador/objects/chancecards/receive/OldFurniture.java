package dk.dtu.matador.objects.chancecards.receive;

public class OldFurniture extends ReceiveCC {
    private double oldFurnitureReceiveAmount = 1000.0;

    public OldFurniture() {
        super("old_furniture", 1000.0);
    }
    public OldFurniture(double oldFurnitureReceiveAmount) {
        super("old_furniture", oldFurnitureReceiveAmount);
        this.oldFurnitureReceiveAmount = oldFurnitureReceiveAmount;
    }
}
