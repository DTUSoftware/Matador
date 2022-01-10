package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class Raise extends ReceiveCC {
    private double raiseReceiveAmount = 1000.0;

    public Raise() {super("raise", 1000.0);}
    public Raise(double raiseReceiveAmount) {
        super("raise", raiseReceiveAmount);
        this.raiseReceiveAmount = raiseReceiveAmount;
    }
}
