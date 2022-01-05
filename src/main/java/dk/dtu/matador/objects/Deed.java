package dk.dtu.matador.objects;

import dk.dtu.matador.Game;
import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.PlayerManager;

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
        DeedManager dm = DeedManager.getInstance();
        UUID deedOwner = dm.getDeedOwnership(deedID);
        double currentRent = 0.0;
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
            UUID[] deedIDs = dm.getDeedGroupDeeds(dm.getGameBoard(deedID).getFieldFromID(DeedManager.getInstance().getFieldID(deedID)).getFieldColor());
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

    @Override
    public String toString() {
        return "Price: " + getPrice() + ", Rent: " + Arrays.toString(getRent());
    }
}
