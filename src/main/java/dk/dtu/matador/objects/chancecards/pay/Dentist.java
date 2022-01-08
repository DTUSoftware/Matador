package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Dentist extends PayCC{
    private double dentistbill = 2000;

    public Dentist() {
        super("Dentist");
    }
    public Dentist(double dentistbill) {
        super("Dentist");
        this.dentistbill = dentistbill;
    }

    public void doCardAction(UUID playerID) {
        double money = dentistbill;
        if (PlayerManager.getInstance().getPlayer(playerID).getBalance() > dentistbill) {
            PlayerManager.getInstance().getPlayer(playerID).withdraw(money);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("Dentist_chancecard_message"));
        }
        else {
            GameManager.getInstance().finishGame();
        }
    }
}


