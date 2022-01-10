package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class TheClassLottery extends ReceiveCC {
    private double theClassLotteryReceiveAmount = 1000.0;

    public TheClassLottery() {super("theClassLottery", 1000.0);}
    public TheClassLottery(double theClassLotteryReceiveAmount) {
        super("theClassLottery", theClassLotteryReceiveAmount);
        this.theClassLotteryReceiveAmount = theClassLotteryReceiveAmount;
    }
}
