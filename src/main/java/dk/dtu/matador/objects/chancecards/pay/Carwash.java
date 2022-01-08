package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Carwash extends PayCC{
    private double washprice = 300;

    public Carwash() {
        super("Carwash");
    }
    public Carwash(double washprice) {
        super("Carwash");
        this.washprice = washprice;
    }

    public void doCardAction(UUID playerID) {
        double money = washprice;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > washprice) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("Carwash_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
