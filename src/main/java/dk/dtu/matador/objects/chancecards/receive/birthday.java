package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class birthday extends ReceiveCC {
    private double birthdayReceiveAmountFromEveryPLayer = 200.0;

    public birthday() {
        super("birthday");
    }
    public birthday(double birthdayReceiveAmountFromEveryPLayer) {
        super("birthday");
        this.birthdayReceiveAmountFromEveryPLayer = birthdayReceiveAmountFromEveryPLayer;
    }

    public void doCardAction(UUID playerID) {
        double money = birthdayReceiveAmountFromEveryPLayer;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > birthdayReceiveAmountFromEveryPLayer) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("Birthday_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
