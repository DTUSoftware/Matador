package dk.dtu.matador.objects;

/**
 * The DiceCup class is a package-private class, such that it
 * should get created through an instance of the class (ex.
 * an instance of DiceManager).
 * An instance of DiceCup holds two Die instances, which are
 * created on initialization.
 *
 * Note: from cdio2, though improved
 */
public class DiceCup {
    private final Die[] dice = new Die[2];
    public double getSumD = getSum();

    /**
     * Constructer function DiceCup
     * When a new DiceCup instance is created, two Die instances are
     * created with it.
     */
    public DiceCup() {
        dice[0] = new Die();
        dice[1] = new Die();
    }

    /**
     * Rafflecup function that raffles the dice like a
     * real life raffle.
     */
    public void raffle() {
        for (Die die : dice) {
            die.raffle();
        }
    }

    /**
     * getValues function that gives out the values of
     * the individues dice
     *
     * @return The facevalue of the individual dice
     */
    public int[] getValues() {
        return new int[]{dice[0].getValue(), dice[1].getValue()};
    }

    /**
     * getSum function that gives the sum of both dice
     *
     * @return the sum of both dice
     */
    public int getSum() {
        return (dice[0].getValue() + dice[1].getValue());
    }
}

