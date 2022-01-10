package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Birthday extends ReceiveCC {
    private double birthdaygiftFromEveryPLayer = 200.0;

    public Birthday() {
        super("Birthday");
    }
    public Birthday(double birthdaygiftFromEveryPLayer) {
        super("Birthday");
        this.birthdaygiftFromEveryPLayer = birthdaygiftFromEveryPLayer;
    }

    public void doCardAction(UUID playerID) {
        double money = birthdaygiftFromEveryPLayer;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > birthdaygiftFromEveryPLayer) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("Birthday_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
