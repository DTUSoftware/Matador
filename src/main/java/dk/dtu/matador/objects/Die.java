package dk.dtu.matador.objects;

import java.util.Random;

/**
 * The Die class is a single dice that can be raffled and give
 * a face value of the die.
 *
 * Note: from cdio2
 */
public class Die {
    private static final Random rand = new Random();
    private int minDieValue = 1;
    private int maxDieValue = 6;

    private int faceValue = 6;

    /**
     * Constructor for the die class where you can set the max dice value
     * fx if you want a D8 instead of a D6. Default is D6
     * @param maxDieValue is the amount of sides you want on the die
     */
    public Die(int maxDieValue){
        this.maxDieValue = maxDieValue;
    }
    public Die() {}

    /**
     * Raffle function to set the facevalue of a die randomly
     * We use Random to generate a random Integer between the min
     * and max die values.
     * We use Random.nextInt() instead of Math.random(), since it is
     * both faster and more efficient
     * (see https://stackoverflow.com/a/738651/12418245).
     */
    public void raffle() {
        faceValue = rand.nextInt(maxDieValue) + minDieValue;
    }

    /**
     * Function getValue that returns the face value of the dice
     */
    public int getValue() {
        return faceValue;
    }


}

