package dk.dtu.matador.objects.chancecards.receive;

public class TheClassLottery extends ReceiveCC {
    private double theClassLotteryReceiveAmount = 1000.0;

    public TheClassLottery() {super("theClassLottery", 1000.0);}
    public TheClassLottery(double theClassLotteryReceiveAmount) {
        super("theClassLottery", theClassLotteryReceiveAmount);
        this.theClassLotteryReceiveAmount = theClassLotteryReceiveAmount;
    }
}
