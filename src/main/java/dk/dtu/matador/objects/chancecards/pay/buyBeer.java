package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class buyBeer extends PayCC{
    private double beerprice = 200;

    public buyBeer() {
        super("buyBeer");
    }
    public buyBeer(double beerprice) {
        super("buyBeer");
        this.beerprice = beerprice;
    }

    public void doCardAction(UUID playerID) {
        double money = beerprice;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > beerprice) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("buyBeer_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
