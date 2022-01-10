package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class JointParty extends ReceiveCC {
    private double JointPartyReceiveAmountFromEveryPLayer = 500.0;

    public birthday() {
        super("JointParty");
    }
    public birthday(double birthdayReceiveAmountFromEveryPLayer) {
        super("JointParty");
        this.JointPartyReceiveAmountFromEveryPLayer = birthdayReceiveAmountFromEveryPLayer;
    }

    public void doCardAction(UUID playerID) {
        double money = JointPartyReceiveAmountFromEveryPLayer;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > JointPartyReceiveAmountFromEveryPLayer) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("JointParty_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
