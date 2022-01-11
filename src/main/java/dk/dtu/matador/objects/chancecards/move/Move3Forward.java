package dk.dtu.matador.objects.chancecards.move;

import dk.dtu.matador.managers.GameManager;

import java.util.UUID;

public class Move3Forward extends MoveToFieldCC{

    private boolean giveStartReward = true;

    public Move3Forward() {super("move3Forward", true);}


    @Override
    public void doCardAction(UUID playerID) {
        GameManager.getInstance().setPlayerBoardPosition(playerID,
                GameManager.getInstance().getPlayerPosition(playerID)+3,
                giveStartReward
        );
    }
}
