package dk.dtu.matador.objects;

import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.PlayerManager;

import java.util.UUID;

/**
 * A deed for a field.
 * Keeps track of the price and the rent.
 */
public class Deed {
    private final UUID deedID;
    private double price = 0.0;
    private double rent = 0.0;
    private double groupRent = 0.0;

    public Deed() {
        deedID = UUID.randomUUID();
    }

    public UUID getID() {
        return this.deedID;
    }

    public void setPrices(double price, double rent, double groupRent) {
        this.price = price;
        this.rent = rent;
        this.groupRent = groupRent;
    }

    public double getPrice() {
        return this.price;
    }

    public double getCurrentRent() {
        UUID deedOwner = DeedManager.getInstance().getDeedOwnership(deedID);
        if (deedOwner == null) {
            return 0.0;
        }
        else {
            // get the deed group and if the owner of current deed is owner of both deeds, raise rent to group rent
            UUID[] deedIDs = DeedManager.getInstance().getDeedGroupDeeds(GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID)).getFieldColor());
            boolean sameOwner = true;
            for (UUID groupDeedID : deedIDs) {
                if (!deedOwner.equals(DeedManager.getInstance().getDeedOwnership(groupDeedID))) {
                    sameOwner = false;
                    break;
                }
            }
            if (sameOwner) {
                return getGroupRent();
            }
            else {
                return getRent();
            }
        }
    }

    public double getRent() {
        return this.rent;
    }

    public double getGroupRent() {
        return this.groupRent;
    }

    public boolean payRent(UUID playerID) {
        return PlayerManager.getInstance().getPlayer(playerID).withdraw(getCurrentRent());
    }

    @Override
    public String toString() {
        return "Price: " + getPrice() + ", Rent: " + getRent() + ", GroupRent: " + getGroupRent();
    }
}
