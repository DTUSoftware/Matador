package dk.dtu.matador.objects.chancecards.receive;

public class PremiumBond extends ReceiveCC {
    private double premiumBondReceiveAmount = 1000.0;

    public PremiumBond() {
        super("premiumBond", 1000.0);
    }
    public PremiumBond(double premiumBondReceiveAmount) {
        super("premiumBond", premiumBondReceiveAmount);
        this.premiumBondReceiveAmount = premiumBondReceiveAmount;
    }
}
