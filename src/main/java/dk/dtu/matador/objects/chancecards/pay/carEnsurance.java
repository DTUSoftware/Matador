package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class carEnsurance extends PayCC{
    private double ensurancecost = 1000;

    public carEnsurance() {
        super("carEnsurance");
    }
    public carEnsurance(double ensurancecost) {
        super("carEnsurance");
        this.ensurancecost = ensurancecost;
    }

    public void doCardAction(UUID playerID) {
        double money = ensurancecost;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > ensurancecost) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("carEnsurance_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
