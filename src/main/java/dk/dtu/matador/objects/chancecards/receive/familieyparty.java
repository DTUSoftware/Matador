package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class familieyparty extends ReceiveCC {
    private double familieypartyReceiveAmountFromEveryPLayer = 500.0;

    public birthday() {
        super("familieyparty");
    }
    public birthday(double birthdayReceiveAmountFromEveryPLayer) {
        super("familieyparty");
        this.familieypartyReceiveAmountFromEveryPLayer = birthdayReceiveAmountFromEveryPLayer;
    }

    public void doCardAction(UUID playerID) {
        double money = familieypartyReceiveAmountFromEveryPLayer;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > familieypartyReceiveAmountFromEveryPLayer) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("familieyparty_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
