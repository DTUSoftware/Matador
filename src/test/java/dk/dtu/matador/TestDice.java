package dk.dtu.matador;

import dk.dtu.matador.objects.DiceCup;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for performing unit tests on the DiceManager class.
 *
 * Note: Inspired by old code @ https://github.com/DTUSoftware/25_del1/blob/master/src/test/java/dk/dtu/spil/TestDiceManager.java
 */
public class TestDice {
    /**
     * We test Die and DiceCup creation.
     */
    @Test
    public void testDiceCupAndDieCreation() {
        DiceCup testCup = new DiceCup();
        assertArrayEquals(new int[] {6, 6}, testCup.getValues());
        assertEquals(12, testCup.getSum());
    }

    /**
     *  We test 1000 Die rolls, to ensure that the values are only
     *  between 1 and 6.
     */
    @Test
    public void testDiceCupRaffle() {
        DiceCup testCup = new DiceCup();

        // We test the dice cup for 1000 throws
        for (int i = 0; i < 1000; i++) {
            testCup.raffle();
            int[] diceValues = testCup.getValues();

            // Ensure that the value is between 1 and 6
            assertTrue((1 <= diceValues[0] && diceValues[0] <= 6) &&
                    (1 <= diceValues[1] && diceValues[1] <= 6));
            // Ensure that the sum is between 2 and 12
            int diceSum = testCup.getSum();
            assertTrue(2 <= diceSum && diceSum <= 12);
        }
    }

    /**
     * We test the DiceCup for 10 million rolls, and ensuring that
     * the probability of the rolls are theoretically correct.
     */
    @Test
    public void testRaffleProbability() {
        // We create a HashMap to keep how many instances of the
        // different die values that we have rolled
        HashMap<Integer, Integer> allDiceValues = new HashMap<Integer, Integer>() {{
            put(1, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 0);
            put(6, 0);
        }};
        // We also keep an integer to keep track of how many equal throws are made
        int equalThrows = 0;

        DiceCup testCup = new DiceCup();

        // We test the dice cup for x amount of throws
        int throwAmount = 10000000;
        for (int i = 0; i < throwAmount; i++) {
            testCup.raffle();
            int[] diceValues = testCup.getValues();

            // If the values are equal, add one to equalThrows
            if (diceValues[0] == diceValues[1]) {
                equalThrows++;
            }

            // Add the values of the two dice to the HashMap
            allDiceValues.put(diceValues[0], allDiceValues.get(diceValues[0])+1);
            allDiceValues.put(diceValues[1], allDiceValues.get(diceValues[1])+1);
        }

        // We calculate the ratio between the equal throws and the amount of throws
        double ratio = (((double) equalThrows)/((double) throwAmount));

        // We ensure that the calculated ratio is within a respectable limit
        assertEquals(0.1666, ratio, 0.005);

        // We loop through the different die values and ensure that the
        // calculated ratio is within a respectable limit.
        for (int value : allDiceValues.values()) {
            ratio = (((double) value)/((double) (throwAmount*2)));
            assertEquals(0.1666, ratio, 0.005);
        }
    }
}
