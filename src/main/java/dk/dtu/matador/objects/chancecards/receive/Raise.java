package dk.dtu.matador.objects.chancecards.receive;

public class Raise extends ReceiveCC {
    private double raiseReceiveAmount = 1000.0;

    public Raise() {super("raise", 1000.0);}
    public Raise(double raiseReceiveAmount) {
        super("raise", raiseReceiveAmount);
        this.raiseReceiveAmount = raiseReceiveAmount;
    }
}
