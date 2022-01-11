package dk.dtu.matador.objects.chancecards.move;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;

import java.util.UUID;

public class GoToNearestFerry extends MoveToFieldCC{
    private boolean giveStartReward = false;
    private String ferry1 = "scandlines_helsingoer_helsingborg";
    private String ferry2 = "mols_linien";
    private String ferry3 = "scandlines_gedser_rostock";
    private String ferry4 = "scandlines_roedby_puttgarden";

     public GoToNearestFerry() {
        super("goToNearestFerry", false);
    }

    @Override
    public void doCardAction(UUID playerID) {
        if (GameManager.getInstance().getPlayerPosition(playerID) == 2 || GameManager.getInstance().getPlayerPosition(playerID) == 7) {
            UUID fieldname = GameManager.getInstance().getGameBoard().getFieldIDFromType(ferry1);
            if (fieldname == null) {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
                System.out.println("Field ID is null!");
                return;
            }

            GameManager.getInstance().setPlayerBoardPosition(
                    playerID,
                    GameManager.getInstance().getGameBoard().getFieldPosition(fieldname),
                    giveStartReward
            );

        } else if (GameManager.getInstance().getPlayerPosition(playerID) == 17) {
            UUID fieldname = GameManager.getInstance().getGameBoard().getFieldIDFromType(ferry2);
            if (fieldname == null) {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
                System.out.println("Field ID is null!");
                return;
            }

            GameManager.getInstance().setPlayerBoardPosition(
                    playerID,
                    GameManager.getInstance().getGameBoard().getFieldPosition(fieldname),
                    giveStartReward
            );
        } else if (GameManager.getInstance().getPlayerPosition(playerID) == 22) {
            UUID fieldname = GameManager.getInstance().getGameBoard().getFieldIDFromType(ferry3);
            if (fieldname == null) {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
                System.out.println("Field ID is null!");
                return;
            }

            GameManager.getInstance().setPlayerBoardPosition(
                    playerID,
                    GameManager.getInstance().getGameBoard().getFieldPosition(fieldname),
                    giveStartReward
            );

        } else if (GameManager.getInstance().getPlayerPosition(playerID) == 33 || GameManager.getInstance().getPlayerPosition(playerID) == 36) {
            UUID fieldname = GameManager.getInstance().getGameBoard().getFieldIDFromType(ferry4);
            if (fieldname == null) {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
                System.out.println("Field ID is null!");
                return;
            }

            GameManager.getInstance().setPlayerBoardPosition(
                    playerID,
                    GameManager.getInstance().getGameBoard().getFieldPosition(fieldname),
                    giveStartReward
            );
        }

    }
}