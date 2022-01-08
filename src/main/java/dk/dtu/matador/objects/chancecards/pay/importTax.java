package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class importTax extends PayCC{
    private double importtaxprice = 200;

    public importTax() {
        super("importTax");
    }
    public importTax(double importtaxprice) {
        super("importTax");
        this.importtaxprice = importtaxprice;
    }

    public void doCardAction(UUID playerID) {
        double money = importtaxprice;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > money) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("importTax_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
