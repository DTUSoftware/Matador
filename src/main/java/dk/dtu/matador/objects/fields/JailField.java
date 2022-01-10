package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.Player;
import dk.dtu.matador.objects.DiceCup;

import java.awt.*;
import java.util.UUID;

public class JailField extends Field {
    private static DiceCup diceCup;
    private double jailBailOut = 4000.0;
    public JailField(Color color, Color textColor) {
        super(color, textColor, "jail", true);
    }
    public JailField(Color color, Color textColor, double jailBailOut) {
        super(color, textColor, "jail", true);
        this.jailBailOut = jailBailOut;
    }

    @Override
    public void doLandingAction(UUID playerID) {
        if (PlayerManager.getInstance().getPlayer(playerID).isJailed()) {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("jailed"));
        }
        else {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("visiting_jail"));
        }
    }

    @Override
    public void doLeavingAction(UUID playerID) {
        Player player = PlayerManager.getInstance().getPlayer(playerID);
        if (player.isJailed()) {
            player.jailedTimeUp();
            // if the player has a bail card
            if (player.takeBailCard()) {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("card_bailout"));
                player.unJail();
            }
            else {
                if (player.getJailedTime()<3 && GUIManager.getInstance().askJailRoll()){
                    diceCup.raffle();
                    int[] diceValues = diceCup.getValues();
                    GUIManager.getInstance().updateDice(diceValues[0], diceValues[1]);
                    if (diceCup.getValues()[0] == diceCup.getValues()[1]){
                        player.unJail();
                        GameManager gm = GameManager.getInstance();
                        gm.setPlayerBoardPosition(playerID, (gm.getPlayerPosition(playerID)+diceCup.getSum()) % gm.getGameBoard().getFieldAmount(), true);
                    }
                }
                // if the player can pay bailout fees
                else if (player.withdraw(jailBailOut)) {
                    GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("paid_bailout").replace("{amount}", Float.toString(Math.round(jailBailOut))));
                    player.unJail();
                }
                else {
                    GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("could_not_pay_bailout"));
                    GameManager.getInstance().finishGame();
                }
            }

        }
    }

    @Override
    public void reloadLanguage() {
        super.getGUIField().setTitle(LanguageManager.getInstance().getString("field_"+super.getFieldName()+"_name"));
        super.getGUIField().setDescription(LanguageManager.getInstance().getString("field_"+super.getFieldName()+"_description"));
    }
}
