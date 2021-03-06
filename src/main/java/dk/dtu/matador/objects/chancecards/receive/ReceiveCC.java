package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.chancecards.ChanceCard;

import java.util.UUID;

public abstract class ReceiveCC extends ChanceCard {
    private double receiveAmount = 0.0;

    /**
     * Initiates a new ChanceCard.
     *
     * @param cardName  The programmable card name,
     *                  also used in the language file.
     */
    ReceiveCC(String cardName, double receiveAmount) {
        super(cardName);
        this.receiveAmount = receiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        double money = receiveAmount;

        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}

/**
 * De har vundet i klasselotteriet. Modtag 500 kr. (Antal kort: 2)
 * De modtager Deres aktieudbytte. Modtag kr 1000 af banken (antal: 3)
 * Kommunen har eftergivet et kvartals skat. Hæv i banken 3000 kr.
 * De have en række med elleve rigtige i tipning, modtag kl 1000
 * Grundet dyrtiden har De fået gageforhøjelse, modtag kr 1000.
 * Deres præmieobligation er udtrykket. De modtager 1000 kr af banken. (antal 2)
 * De har solg nogle gamle møbler på auktion. Modtag 1000 kr af banken.
 * Værdien af egen avl fra nyttehaven udgør 200 kr som de modtager af banken
 * De modtager “Matador-legatet” på kr 40.000, men kun hvis værdier ikke overstiger 15.000 kr
 * Det er deres fødselsdag. Modtag af hver medspiller 200 kr.
 * De har lagt penge ud til et sammenskudsgilde. Mærkværdigvis betaler alle straks. Modtag fra hver medspiller 500 kr.
 * De skal holde familiefest og får et tilskud fra hver medspiller på 500 kr.
 */

