package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class EllevenRight extends ReceiveCC {
    private double ellevenRightReceiveAmount = 1000.0;

    public EllevenRight() {
        super("EllevenRight");
    }
    public EllevenRight(double ellevenRightReceiveAmount) {
        super("EllevenRight");
        this.ellevenRightReceiveAmount = ellevenRightReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(ellevenRightReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("elleven_right_receive_amount"));
    }
}