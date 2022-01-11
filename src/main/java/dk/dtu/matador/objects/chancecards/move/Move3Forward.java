package dk.dtu.matador.objects.chancecards.move;

import dk.dtu.matador.managers.GameManager;

import java.util.UUID;

public class Move3Forward extends MoveToFieldCC{

    private boolean giveStartReward = true;

    public Move3Forward() {super("move_3_forward", true);}


    @Override
    public void doCardAction(UUID playerID) {

        if (GameManager.getInstance().getPlayerPosition(playerID) > 36) {
            GameManager.getInstance().setPlayerBoardPosition(playerID,
                    GameManager.getInstance().getPlayerPosition(playerID) - 37,
                    giveStartReward

            );
        } else {
            GameManager.getInstance().setPlayerBoardPosition(playerID,
                    GameManager.getInstance().getPlayerPosition(playerID) + 3,
                    giveStartReward
            );
        }
    }
}
