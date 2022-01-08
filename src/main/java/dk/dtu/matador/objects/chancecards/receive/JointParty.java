package dk.dtu.matador.objects.chancecards.receive;

public class JointParty extends ReceiveCC {
    private double aktieReceiveAmountFromEveryPlayer = 500.0;

    /**
     * Initiates a new ChanceCard.
     *
     * @param cardName The programmable card name,
     *                 also used in the language file.
     */
    JointParty(String cardName) {
        super(cardName);
    }
}
