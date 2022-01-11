package dk.dtu.matador.objects.chancecards.receive;

public class TheLocalAuthority extends ReceiveCC {
    private double theLocalAuthorityReceiveAmount = 3000.0;

    public TheLocalAuthority() {super("the_local_authority", 3000.0);}
    public TheLocalAuthority(double theLocalAuthorityReceiveAmount) {
        super("the_local_authority", theLocalAuthorityReceiveAmount);
        this.theLocalAuthorityReceiveAmount = theLocalAuthorityReceiveAmount;
    }
}
