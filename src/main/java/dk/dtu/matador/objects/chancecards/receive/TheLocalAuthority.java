package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class TheLocalAuthority extends ReceiveCC {
    private double theLocalAuthorityReceiveAmount = 3000.0;

    public TheLocalAuthority() {super("theLocalAuthority", 3000.0);}
    public TheLocalAuthority(double theLocalAuthorityReceiveAmount) {
        super("theLocalAuthority", theLocalAuthorityReceiveAmount);
        this.theLocalAuthorityReceiveAmount = theLocalAuthorityReceiveAmount;
    }
}
