package dk.dtu.matador.objects.chancecards.receive;

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
