package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;

import java.util.UUID;

/**
 * The ChanceCard class has all the stuff one would need to create new Chance Cards.
 */
public abstract class ChanceCard {
    private final String cardName;

    /**
     * Initiates a new ChanceCard.
     *
     * @param cardName  The programmable card name,
     *                  also used in the language file.
     */
    public ChanceCard(String cardName) {
        this.cardName = cardName;
    }

    /**
     * Shows the card message in the GUI.
     */
    public void showCardMessage(){
        String message = LanguageManager.getInstance().getString(cardName+"_chancecard_message");
        GUIManager.getInstance().showChanceCard(message);
    }

    /**
     * Abstract method for performing the said action of a chance card.
     *
     * @param playerID  The UUID of the Player that drew the card.
     */
    public abstract void doCardAction(UUID playerID);

    public String getCardName() {
        return cardName;
    }
}
