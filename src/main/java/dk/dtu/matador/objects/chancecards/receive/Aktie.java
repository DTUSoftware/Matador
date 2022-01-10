package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Aktie extends ReceiveCC {
    private double aktieReceiveAmount = 1000.0;

    public Aktie() {
        super("Aktie");
    }
    public Aktie(double aktieReceiveAmount) {
        super("Aktie");
        this.aktieReceiveAmount = aktieReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(aktieReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("aktie_receive_amount"));
    }

}
