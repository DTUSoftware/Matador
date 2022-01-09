package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class IllegalStop extends PayCC{
    private double stopfine = 1000;

    public IllegalStop() {
        super("illegalStop");
    }
    public IllegalStop(double stopfine) {
        super("illegalStop");
        this.stopfine = stopfine;
    }

    public void doCardAction(UUID playerID) {
        double money = stopfine;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > money) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("illegalStop_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
