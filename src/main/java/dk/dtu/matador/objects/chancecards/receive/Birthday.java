package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Birthday extends ReceiveCC {
    private double birthdaygiftFromEveryPLayer = 200.0;

    public Birthday() {
        super("birthday", 200.0);
    }
    public Birthday(double birthdaygiftFromEveryPLayer) {
        super("birthday", birthdaygiftFromEveryPLayer);
        this.birthdaygiftFromEveryPLayer = birthdaygiftFromEveryPLayer;
    }

    @Override
    public void doCardAction(UUID playerID) {
        double money = 0.0;
        for (UUID otherPlayerID : PlayerManager.getInstance().getPlayerIDs()) {
            if (otherPlayerID != playerID) {
                if (PlayerManager.getInstance().getPlayer(otherPlayerID).withdraw(birthdaygiftFromEveryPLayer)) {
                    money = money + birthdaygiftFromEveryPLayer;
                }
                else {
                    GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("could_not_pay_birthday")
                            .replace("{player_name}", PlayerManager.getInstance().getPlayer(otherPlayerID).getName())
                            .replace("{birthday_player_name}", PlayerManager.getInstance().getPlayer(playerID).getName())
                    );
                    GameManager.getInstance().finishGame();
                }
            }
        }
        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
