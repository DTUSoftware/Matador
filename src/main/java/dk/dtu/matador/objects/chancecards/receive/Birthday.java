package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Birthday extends ReceiveCC {
    private double birthdayReceiveAmountFromEveryPLayer = 200.0;

    public Birthday() {
        super("Birthday");
    }
    public Birthday(double birthdayReceiveAmountFromEveryPLayer) {
        super("Birthday");
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
