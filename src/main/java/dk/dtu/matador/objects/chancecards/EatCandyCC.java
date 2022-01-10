package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.*;

import java.util.UUID;


/**
 * Du har spist
 * for meget slik
 * BETAL 2000 til banken
 */
public class EatCandyCC extends ChanceCard {
    private double candyPrice = 1000.0;

    public EatCandyCC() {
        super("eatcandy");
    }
    public EatCandyCC(double candyPrice) {
        super("eatcandy");
        this.candyPrice = candyPrice;
    }

    @Override
    public void doCardAction(UUID playerID) {
        if (!PlayerManager.getInstance().getPlayer(playerID).withdraw(candyPrice)) {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("could_not_buy_candy"));
        }

    }
}
