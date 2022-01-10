package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

/**
 * only if you have less then 15000 value in all
 */

public class MatadorLegatet {
    private double matadorLegatetReceiveAmount = 40000.0;
    private double matadorLegatetNeedsToBeUnder = 15000.0;

    public MatadorLegatet() {
        super("MatadorLegatet");
    }
    public MatadorLegatet(double matadorLegatetReceiveAmount) {
        super("MatadorLegatet");
        this.matadorLegatetReceiveAmount = matadorLegatetReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        if (player.getNetWorth(); < matadorLegatetNeedsToBeUnder){
        PlayerManager.getInstance().getPlayer(playerID).deposit(matadorLegatetReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("matador_legatet_receive_amount"));
    } else {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("matador_legatet_too_rich"));}
    }
}
