package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class CarRepair extends PayCC{
    private double repairprice = 3000;

    public CarRepair() {
        super("carRepair");
    }
    public CarRepair(double repairprice) {
        super("carEnsurance");
        this.repairprice = repairprice;
    }

    public void doCardAction(UUID playerID) {
        double money = repairprice;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > repairprice) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("carRepair_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}
