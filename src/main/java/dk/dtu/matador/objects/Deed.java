package dk.dtu.matador.objects;

import dk.dtu.matador.Game;
import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.fields.Field;
import dk.dtu.matador.objects.fields.StreetField;

import java.util.Arrays;
import java.util.UUID;

/**
 * A deed for a field.
 * Keeps track of the price and the rent.
 */
public class Deed {
    private final UUID deedID;
    private double price = 0.0;
    private double[] rent = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private double mortgage = 0.0;
    private double housePrice = 0.0;
    private double hotelPrice = 0.0;
    private int houses = 0;
    private int hotels = 0;

    public Deed() {
        deedID = UUID.randomUUID();
    }

    public UUID getID() {
        return this.deedID;
    }

    public void setPrices(double price, double mortgage, double[] rent, double housePrice, double hotelPrice) {
        this.price = price;
        this.rent = rent;
        this.mortgage = mortgage;
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
    }

    public void setPrices(double price, double mortgage) {
        this.price = price;
        this.mortgage = mortgage;
    }

    public double getPrice() {
        return this.price;
    }

    public double getCurrentRent() {
        double currentRent = 0.0;
        UUID deedOwner = DeedManager.getInstance().getDeedOwnership(deedID);
        if (deedOwner == null) {
            return 0.0;
        }
        else {
            // raise rent to max rent, if they have a hotel built
            if (this.hotels >= 1) {
                currentRent = this.rent[5]; // hotel price
            }
            else {
                currentRent = this.rent[this.houses];
            }

            // get the deed group and if the owner of current deed is owner of both deeds, raise rent to group rent
            UUID[] deedIDs = DeedManager.getInstance().getDeedGroupDeeds(GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID)).getFieldColor());
            int sameOwnerCount = 0;
            for (UUID groupDeedID : deedIDs) {
                if (deedOwner.equals(DeedManager.getInstance().getDeedOwnership(groupDeedID))) {
                    sameOwnerCount++;
                }
            }

            // TODO: calculate rent, if they own all the group fields
        }
        return currentRent;
    }

    public double[] getRent() {
        return this.rent;
    }

    public boolean payRent(UUID playerID) {
        return PlayerManager.getInstance().getPlayer(playerID).withdraw(getCurrentRent());
    }

    public boolean canBuildHouse() {
        Field field = GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID));
        if (field instanceof StreetField) {
            Game.logDebug("is street field");
            if ((hotels != 1) && (houses != 4)) {
                Game.logDebug("no hotels and not 4 houses");
                UUID deedOwner = DeedManager.getInstance().getDeedOwnership(deedID);
                // if player has enough money
                if ((deedOwner != null) && (PlayerManager.getInstance().getPlayer(deedOwner).getBalance() >= housePrice)) {
                    Game.logDebug("player has enough money");
                    // check the other deeds in the group, if the player owns the deeds
                    if (DeedManager.getInstance().playerOwnsAllDeedsInDeedGroup(field.getFieldColor(), deedOwner)) {
                        Game.logDebug("player owns all the deeds in the deed group");
                        // does the other deeds in the group have fewer houses on them, than this deed (rule of even building)
                        UUID[] deedGroupIDs = DeedManager.getInstance().getDeedGroupDeeds(field.getFieldColor());
                        for (UUID deedID : deedGroupIDs) {
                            if (DeedManager.getInstance().getDeed(deedID).getHouses() < houses-1) {
                                Game.logDebug("cannot build, less houses");
                                return false;
                            }
                        }
                        return true;
                    }

                }
            }
        }
        Game.logDebug("cannot build");
        return false;
    }

    public boolean canBuildHotel() {
        // no need to check for streetField, since it would already have houses on it
        // to build a hotel in the first place
        if ((houses == 4) && (hotels != 1)) {
            UUID deedOwner = DeedManager.getInstance().getDeedOwnership(deedID);
            if ((deedOwner != null) && (PlayerManager.getInstance().getPlayer(deedOwner).getBalance() >= hotelPrice)) {
                // check if the other deeds in the group also have 4 houses
                UUID[] deedGroupIDs = DeedManager.getInstance().getDeedGroupDeeds(
                        GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID)).getFieldColor());
                for (UUID deedID : deedGroupIDs) {
                    if (DeedManager.getInstance().getDeed(deedID).getHouses() != 4) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public double getHousePrice() {
        return housePrice;
    }

    public double getHotelPrice() {
        return hotelPrice;
    }

    public int getHouses() {
        return houses;
    }

    public int getHotels() {
        return hotels;
    }

    public void addHouse() {
        houses++;
    }

    public void addHotel() {
        houses = 0;
        hotels++;
    }

    @Override
    public String toString() {
        return "Price: " + getPrice() + ", Rent: " + Arrays.toString(getRent());
    }
}
