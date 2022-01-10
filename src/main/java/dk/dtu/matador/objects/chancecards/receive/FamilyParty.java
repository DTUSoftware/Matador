package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class FamilyParty extends ReceiveCC {
    private double FamilyPartyReceiveAmountFromEveryPLayer = 500.0;

    public FamilyParty() {
        super("familyParty", 500.0);
    }
    public FamilyParty(double birthdayReceiveAmountFromEveryPLayer) {
        super("familyParty", birthdayReceiveAmountFromEveryPLayer);
        this.FamilyPartyReceiveAmountFromEveryPLayer = birthdayReceiveAmountFromEveryPLayer;
    }

    @Override
    public void doCardAction(UUID playerID) {
        double money = 0.0;
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("familyparty_chancecard_message"));
        for (UUID otherPlayerID : PlayerManager.getInstance().getPlayerIDs()) {
            if (otherPlayerID != playerID) {
                if (PlayerManager.getInstance().getPlayer(otherPlayerID).withdraw(FamilyPartyReceiveAmountFromEveryPLayer)) {
                    money = money + FamilyPartyReceiveAmountFromEveryPLayer;
                }
                else {
                    GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("could_not_pay_family_party")
                            .replace("{player_name}", PlayerManager.getInstance().getPlayer(otherPlayerID).getName())
                            .replace("{family_party_player_name}", PlayerManager.getInstance().getPlayer(playerID).getName())
                    );
                    GameManager.getInstance().finishGame();
                }
            }
        }
        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
