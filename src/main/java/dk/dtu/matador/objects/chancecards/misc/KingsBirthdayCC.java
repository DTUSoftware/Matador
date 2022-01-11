package dk.dtu.matador.objects.chancecards.misc;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.chancecards.ChanceCard;

import java.util.UUID;

public class KingsBirthdayCC extends MiscCC {
    public KingsBirthdayCC() {
        super("kings_birthday");
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).giveBailCard();
    }
}

/**
 * I anledning af kongens fødselsdag benådes De herved for fængsel.
 * Dette kort kan opbevares indtil De får brug for det, eller De kan sælge det. (antal kort: 2)
 */

