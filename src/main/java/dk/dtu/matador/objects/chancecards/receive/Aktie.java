package dk.dtu.matador.objects.chancecards.receive;

public class Aktie extends ReceiveCC {
    private double aktieReceiveAmount = 1000.0;

    public Aktie() { super("aktie", 1000.0);}
    public Aktie(double aktieReceiveAmount) {
        super("aktie", aktieReceiveAmount);
        this.aktieReceiveAmount = aktieReceiveAmount;
    }
}
