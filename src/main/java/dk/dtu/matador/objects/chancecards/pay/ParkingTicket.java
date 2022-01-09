package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class ParkingTicket extends PayCC{
    public ParkingTicket() {
        super("ParkingTicket", 200.0);
    }
}
