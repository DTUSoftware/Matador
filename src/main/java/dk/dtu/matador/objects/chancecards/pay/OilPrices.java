package dk.dtu.matador.objects.chancecards.pay;

import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

public class OilPrices extends PayCC{
    private double oilpricehouse = 500;
    private double oilpricehotel = 2000;

    public OilPrices() {
        super("oil_prices", 500.0);
    }
    public OilPrices(double oilpricehouse) {
        super("oil_prices", oilpricehouse);
        this.oilpricehouse = oilpricehouse;
    }

    @Override
    public void doCardAction(UUID playerID) {
        PlayerManager.getInstance().getPlayer(playerID).withdraw(oilpricehouse * PlayerManager.getInstance().getPlayer(playerID).getPlayerHotelOwnNumber());
        PlayerManager.getInstance().getPlayer(playerID).withdraw(oilpricehotel * PlayerManager.getInstance().getPlayer(playerID).getPlayerHouseOwnNumber());
    }
}
