package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class PropertyTax extends PayCC{
    private double housetax = 500;
    private double hoteltax = 2300;

    public PropertyTax() {
        super("houseTax", 500.0);
    }
    public PropertyTax(double housetax) {
        super("houseTax", housetax);
        this.housetax = housetax;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).withdraw(housetax * PlayerManager.getInstance().getPlayer(playerID).getPlayerHotelOwnNumber());
        PlayerManager.getInstance().getPlayer(playerID).withdraw(hoteltax * PlayerManager.getInstance().getPlayer(playerID).getPlayerHouseOwnNumber());
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("property_tax_chancecard_message"));
    }
}
