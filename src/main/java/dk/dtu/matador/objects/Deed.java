package dk.dtu.matador.objects;

import dk.dtu.matador.Game;
import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.DiceCup;
import dk.dtu.matador.objects.GameBoard;
import dk.dtu.matador.objects.fields.BreweryField;
import dk.dtu.matador.objects.fields.FerryField;
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
    private double prawnPrice = 0.0;
    private double housePrice = 0.0;
    private double hotelPrice = 0.0;
    private int houses = 0;
    private int hotels = 0;
    private boolean prawned = false;

    public Deed() {
        deedID = UUID.randomUUID();
    }

    public UUID getID() {
        return this.deedID;
    }

    public void setPrices(double price, double prawnPrice, double[] rent, double housePrice, double hotelPrice) {
        this.price = price;
        this.rent = rent;
        this.prawnPrice = prawnPrice;
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
    }

    public void setPrices(double price, double prawnPrice, double[] rent) {
        this.price = price;
        this.rent = rent;
        this.prawnPrice = prawnPrice;
    }

    public void setPrices(double price, double prawnPrice) {
        this.price = price;
        this.prawnPrice = prawnPrice;
    }

    public double getPrice() {
        return this.price;
    }

    public double getCurrentRent() {
        if (!GameManager.isInitialized()) {
            return 0.0;
        }

        DiceCup diceCup = GameManager.getInstance().getDiceCup();

        // if it's prawned, return 0.0
        if (isPrawned()) {
            return 0.0;
        }

        double currentRent = 0.0;
        UUID deedOwner = DeedManager.getInstance().getDeedOwnership(deedID);
        Field field = GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID));
        if (deedOwner == null) {
            return 0.0;
        }
        else {
            if (field instanceof BreweryField) {
                switch (DeedManager.getInstance().howManyFieldTypeDoesPlayerOwn("BreweryField", deedOwner)) {
                    case 1:
                        currentRent = diceCup.getSum()*this.rent[0];
                        break;
                    case 2:
                        currentRent = diceCup.getSum()*this.rent[1];
                        break;
                }
            }
            else if (field instanceof FerryField) {
                switch (DeedManager.getInstance().howManyFieldTypeDoesPlayerOwn("FerryField", deedOwner)) {
                    case 1:
                        currentRent = this.rent[0];
                        break;
                    case 2:
                        currentRent = this.rent[1];
                        break;
                    case 3:
                        currentRent = this.rent[2];
                        break;
                    case 4:
                        currentRent = this.rent[3];
                        break;
                }
            }
            else if (field instanceof StreetField) {
                // raise rent to max rent, if they have a hotel built
                if (this.hotels >= 1) {
                    currentRent = this.rent[5]; // hotel price
                }
                else if (this.houses > 0) {
                    currentRent = this.rent[this.houses];
                }
                //Calculates rent if one owner owns all the fields.
                else if (DeedManager.getInstance().playerOwnsAllDeedsInDeedGroup(field.getFieldColor(), deedOwner)){
                    currentRent = this.rent[0]*2.0;
                }
                else {
                    currentRent = this.rent[0];
                }
            }
        }
        return currentRent;
    }

    public double[] getRent() {
        return this.rent;
    }

    /**
     * Withdraws the rent from the player that landed on the field, and transfers the rent to the deed owner.
     * @param playerID the player that landed.
     * @return whether the player succeeded.
     */
    public boolean payRent(UUID playerID) {
        double currentRent = getCurrentRent();
        boolean success = PlayerManager.getInstance().getPlayer(playerID).withdraw(currentRent);
        if (success) {
            PlayerManager.getInstance().getPlayer(DeedManager.getInstance().getDeedOwnership(deedID)).deposit(currentRent);
        }
        return success;
    }

    public boolean canBuildHouse() {
        Field field = GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID));
        if (field instanceof StreetField) {
            if ((hotels != 1) && (houses != 4)) {
                UUID deedOwner = DeedManager.getInstance().getDeedOwnership(deedID);
                // if player has enough money
                if ((deedOwner != null) && (PlayerManager.getInstance().getPlayer(deedOwner).getBalance() >= housePrice)) {
                    // check the other deeds in the group, if the player owns the deeds
                    if (DeedManager.getInstance().playerOwnsAllDeedsInDeedGroup(field.getFieldColor(), deedOwner)) {
                        // does the other deeds in the group have fewer houses on them, than this deed (rule of even building)
                        UUID[] deedGroupIDs = DeedManager.getInstance().getDeedGroupDeeds(field.getFieldColor());
                        for (UUID deedID : deedGroupIDs) {
                            if (DeedManager.getInstance().getDeed(deedID).getHouses() < houses) {
                                return false;
                            }
                        }
                        return true;
                    }

                }
            }
        }
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
                    Deed deed = DeedManager.getInstance().getDeed(deedID);
                    if (deed.getHouses() != 4) {
                        if (!(deed.getHotels() > hotels)) {
                            return false;
                        }
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

    public double getPrawnPrice() {
        return prawnPrice;
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

    public void removeHouse() {
        houses--;
    }

    public void addHotel() {
        houses = 0;
        hotels++;
    }

    public void removeHotel() {
        hotels--;
    }

    public boolean isPrawned() {
        return prawned;
    }

    public void prawn() {
        prawned = true;
    }

    public void buyBack() {
        prawned = false;
    }

    public double getBuyBackPrice() {
        return prawnPrice + Math.ceil((prawnPrice * 0.1)/100.0)*100.0;
    }

    @Override
    public String toString() {
        return "Price: " + getPrice() + ", Rent: " + Arrays.toString(getRent());
    }
}
