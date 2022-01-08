package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class parkTicket extends PayCC{
    private double parkticketprice = 200;

    public parkTicket() {
        super("parkTicket");
    }
    public parkTicket(double parkticketprice) {
        super("parkTicket");
        this.parkticketprice = parkticketprice;
    }

    public void doCardAction(UUID playerID) {
        double money = parkticketprice;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > money) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("parkTicket_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
