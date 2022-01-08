package dk.dtu.matador.objects.chancecards.pay;

public abstract class PayCC {
    
    private final String cardName;

    /**
     * Initiates a new ChanceCard.
     *
     * @param cardName  The programmable card name,
     *                  also used in the language file.
     */
    PayCC(String cardName) {this.cardName = cardName;
    }
}

/**
 * Oliepriserne er steget, og De skal betale kr 500 pr hus og kr 2000 pr hotel.
 * Ejendomsskatten er steget. Ekstraudgifterne er: 800 kr pr hus, 2300 kr pr hotel.
 * De har kørt frem for “fuldt stop”, Betal 1000 kroner i bøde
 * Betal for vognvask og smøring kr 300
 * Betal kr 200 for levering af 2 kasser øl
 * Betal 3000 for reparation af deres vogn (antal kort: 2)
 * De har købt 4 nye dæk til Deres vogn, betal kr 1000
 * De har fået en parkeringsbøde, betal kr 200 i bøde
 * Betal deres bilforsikring, kr 1000
 * De har været udenlands og købt for mange smøger, betal kr 200 i told.
 * Tandlægeregning, betal kr 2000.
 * De har vundet i klasselotteriet. Modtag 500 kr. (Antal kort: 2)
 */