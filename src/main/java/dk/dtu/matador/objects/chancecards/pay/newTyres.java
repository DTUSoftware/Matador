package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class newTyres extends PayCC{
    private double tyrescost = 1000;

    public newTyres() {
        super("newTyres");
    }
    public newTyres(double tyrescost) {
        super("newTyres");
        this.tyrescost = tyrescost;
    }

    public void doCardAction(UUID playerID) {
        double money = tyrescost;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > money) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("newTyres_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
