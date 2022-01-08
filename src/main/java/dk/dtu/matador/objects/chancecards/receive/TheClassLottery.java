package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class TheClassLottery extends ReceiveCC {
    private double lotteryReceiveAmount = 500.0;

    public TheClassLottery() {
        super("TheClassLottery");
    }
    public TheClassLottery(double lotteryReceiveAmount) {
        super("TheClassLottery");
        this.lotteryReceiveAmount = lotteryReceiveAmount;
    }

    public void doCardAction(UUID playerID) {
        double money = lotteryReceiveAmount;

        PlayerManager.getInstance().getPlayer(playerID).deposit(money);
    }
}
