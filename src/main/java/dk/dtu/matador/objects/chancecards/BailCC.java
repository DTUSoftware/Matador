package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

/**
 * Du løslades uden omkostninger.
 * Behold dette kort, indtil du får brug for det.
 */
public class BailCC extends ChanceCard {
    /**
     * Initiates a new Bail ChanceCard.
     */
    public BailCC() {
        super("bail");
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).giveBailCard();
    }
}
