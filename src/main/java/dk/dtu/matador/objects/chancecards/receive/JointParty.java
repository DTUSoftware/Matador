package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class JointParty extends ReceiveCC {
    private double JointPartyReceiveAmountFromEveryPLayer = 500.0;

    public JointParty() {
        super("joint_party", 500.0);
    }
    public JointParty(double birthdayReceiveAmountFromEveryPLayer) {
        super("joint_party", birthdayReceiveAmountFromEveryPLayer);
        this.JointPartyReceiveAmountFromEveryPLayer = birthdayReceiveAmountFromEveryPLayer;
    }

    @Override
    public void doCardAction(UUID playerID) {
        double money = 0.0;
        for (UUID otherPlayerID : PlayerManager.getInstance().getPlayerIDs()) {
            if (otherPlayerID != playerID) {
                if (PlayerManager.getInstance().getPlayer(otherPlayerID).withdraw(JointPartyReceiveAmountFromEveryPLayer)) {
                    money = money + JointPartyReceiveAmountFromEveryPLayer;
                }
                else {
                    GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("could_not_pay_joint_party")
                            .replace("{player_name}", PlayerManager.getInstance().getPlayer(otherPlayerID).getName())
                            .replace("{joint_party_player_name}", PlayerManager.getInstance().getPlayer(playerID).getName())
                    );
                    GameManager.getInstance().finishGame();
                }
            }
        }
        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
