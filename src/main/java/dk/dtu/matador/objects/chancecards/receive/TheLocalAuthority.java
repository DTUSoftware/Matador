package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class TheLocalAuthority extends ReceiveCC {
    private double theLocalAuthorityReceiveAmount = 3000.0;

    public TheLocalAuthority() {super("TheLocalAuthority");}
    public TheLocalAuthority(double theLocalAuthorityReceiveAmount) {
        super("TheLocalAuthority");
        this.theLocalAuthorityReceiveAmount = theLocalAuthorityReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(theLocalAuthorityReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("the_local_authority_receive_amount"));
    }
}
