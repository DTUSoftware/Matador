package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class NewTyres extends PayCC{
    public NewTyres() {
        super("new_tyres", 1000.0);
    }
}
