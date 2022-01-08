package dk.dtu.matador.objects.chancecards.pay;

public class Oilprices extends PayCC{
    private double oilpricehouse = 500;
    private double oilpricehotel = 2000;

    /**
     * Initiates a new ChanceCard.
     *
     * @param cardName The programmable card name,
     *                 also used in the language file.
     */
    Oilprices(String cardName) {
        super(cardName);
    }
}
