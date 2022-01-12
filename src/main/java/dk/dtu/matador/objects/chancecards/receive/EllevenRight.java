package dk.dtu.matador.objects.chancecards.receive;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class EllevenRight extends ReceiveCC {
    private double ellevenRightReceiveAmount = 1000.0;

    public EllevenRight() {
        super("elleven_right", 1000.0);
    }
    public EllevenRight(double ellevenRightReceiveAmount) {
        super("elleven_right", ellevenRightReceiveAmount);
        this.ellevenRightReceiveAmount = ellevenRightReceiveAmount;
    }
}