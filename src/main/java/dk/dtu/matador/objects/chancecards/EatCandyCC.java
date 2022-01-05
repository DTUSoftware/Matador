package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.Game;
import dk.dtu.matador.GameInstance;
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
        GameInstance game = Game.getGameInstance(PlayerManager.getInstance().getPlayerGame(playerID));
        if (!PlayerManager.getInstance().getPlayer(playerID).withdraw(candyPrice)) {
            GUIManager.getInstance().getGUI(game.getGUIID()).showMessage(game.getLanguageManager().getString("could_not_buy_candy"));
            game.getGameManager().finishGame();
        }

    }
}
