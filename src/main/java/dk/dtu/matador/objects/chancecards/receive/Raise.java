package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Raise extends ReceiveCC {
    private double raiseReceiveAmount = 1000.0;

    public Raise() {super("Raise");}
    public Raise(double raiseReceiveAmount) {
        super("Raise");
        this.raiseReceiveAmount = raiseReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(raiseReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("raise_receive_amount"));
    }
}
