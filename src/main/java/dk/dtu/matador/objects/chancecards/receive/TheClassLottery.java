package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class TheClassLottery extends ReceiveCC {
    private double theClassLotteryReceiveAmount = 1000.0;

    public TheClassLottery() {super("TheClassLottery");}
    public TheClassLottery(double theClassLotteryReceiveAmount) {
        super("TheClassLottery");
        this.theClassLotteryReceiveAmount = theClassLotteryReceiveAmount;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).deposit(theClassLotteryReceiveAmount);
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("the_class_lottery_receive_amount"));
    }
}
