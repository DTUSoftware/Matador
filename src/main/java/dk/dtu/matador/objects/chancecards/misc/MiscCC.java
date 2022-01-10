package dk.dtu.matador.objects.chancecards.misc;

import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.chancecards.ChanceCard;

import java.util.UUID;

public abstract class MiscCC extends ChanceCard {

    /**
     * Initiates a new ChanceCard.
     *
     * @param cardName  The programmable card name,
     *                  also used in the language file.
     */
    MiscCC(String cardName) {
        super(cardName);
    }

    @Override
    public void doCardAction(UUID playerID) {
    }
}