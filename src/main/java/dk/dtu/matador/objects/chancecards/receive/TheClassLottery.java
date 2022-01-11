package dk.dtu.matador.objects.chancecards.receive;

public class TheClassLottery extends ReceiveCC {
    private double theClassLotteryReceiveAmount = 1000.0;

    public TheClassLottery() {super("the_class_lottery", 1000.0);}
    public TheClassLottery(double theClassLotteryReceiveAmount) {
        super("the_class_lottery", theClassLotteryReceiveAmount);
        this.theClassLotteryReceiveAmount = theClassLotteryReceiveAmount;
    }
}
