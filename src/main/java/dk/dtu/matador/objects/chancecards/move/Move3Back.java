package dk.dtu.matador.objects.chancecards.move;

import dk.dtu.matador.managers.GameManager;

import java.util.UUID;

public class Move3Back extends MoveToFieldCC{
    private boolean giveStartReward = false;

    public Move3Back() {super("move3Back", false);}


    @Override
    public void doCardAction(UUID playerID) {
        if (GameManager.getInstance().getPlayerPosition(playerID) < 3) {
            GameManager.getInstance().setPlayerBoardPosition(playerID,
                    GameManager.getInstance().getPlayerPosition(playerID) + 37,
                    giveStartReward

            );
        } else {
            GameManager.getInstance().setPlayerBoardPosition(playerID,
                    GameManager.getInstance().getPlayerPosition(playerID) - 3,
                    giveStartReward
            );
        }
    }
}
