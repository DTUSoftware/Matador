package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class BuyBeer extends PayCC{
    public BuyBeer() {
        super("buy_beer", 200.0);
    }
}
