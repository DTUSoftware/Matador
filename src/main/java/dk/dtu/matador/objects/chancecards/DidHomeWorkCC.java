package dk.dtu.matador.objects.chancecards;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

/**
 * Du har lavet alle dine lektier!
 * MODTAG M2
 * Fra banken
 */

public class DidHomeWorkCC extends ChanceCard {
    private double homeworkReward = 2000.0;

    public DidHomeWorkCC() {
        super("didhomework");
    }
    public DidHomeWorkCC(double homeworkReward) {
        super("didhomework");
        this.homeworkReward = homeworkReward;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(homeworkReward);
    }
}