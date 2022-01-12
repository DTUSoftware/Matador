package dk.dtu.matador.objects.chancecards.receive;

public class PremiumBond extends ReceiveCC {
    private double premiumBondReceiveAmount = 1000.0;

    public PremiumBond() {
        super("premium_bond", 1000.0);
    }
    public PremiumBond(double premiumBondReceiveAmount) {
        super("premium_bond", premiumBondReceiveAmount);
        this.premiumBondReceiveAmount = premiumBondReceiveAmount;
    }
}
