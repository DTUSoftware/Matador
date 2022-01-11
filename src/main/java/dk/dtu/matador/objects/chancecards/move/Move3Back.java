package dk.dtu.matador.objects.chancecards.move;

import dk.dtu.matador.managers.GameManager;

import java.util.UUID;

public class Move3Back extends MoveToFieldCC{
    private boolean giveStartReward = false;

    public Move3Back() {super("move_3_back", false);}


    @Override
    public void doCardAction(UUID playerID) {
        GameManager.getInstance().setPlayerPosition(playerID,
                GameManager.getInstance().getPlayerPosition(playerID) - 3,
                giveStartReward
        );
    }
}
