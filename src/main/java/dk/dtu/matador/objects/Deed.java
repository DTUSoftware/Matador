package dk.dtu.matador.objects;

import dk.dtu.matador.Game;
import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.DiceCup;
import dk.dtu.matador.objects.GameBoard;

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
    private int sameOwnerCount;
    private String subtype = GameBoard

    private static DiceCup diceCup;


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
            switch (GameBoard.field_subtype) {
                case brewery {
                    if (this.sameOwnerCount >= 0){
                        currentRent = diceCup.getSumD*100;
                    }
                    if (this.sameOwnerCount >= 1){
                        currentRent = diceCup.getSumD*200;
                    }

                }
                case ferry {
                    if (this.sameOwnerCount >= 0){
                        currentRent = this.rent[0];
                    }
                    if (this.sameOwnerCount >= 1){
                        currentRent = this.rent[1];
                    }
                    if (this.sameOwnerCount >= 2){
                        currentRent = this.rent[2];
                    }
                    if (this.sameOwnerCount >= 3){
                        currentRent = this.rent[3];
                    }

                }


                case street {
                            // raise rent to max rent, if they have a hotel built
                    if (this.hotels >= 1) {
                        currentRent = this.rent[5]; // hotel price
                    }
                            //Calculates rent if one owner owns all the fields.
                    else if (this.sameOwnerCount == 2){
                        currentRent = this.rent[0]*2;
                    }
                    else {
                        currentRent = this.rent[this.houses];
                    }

                }
                default:
                    Game.logDebug("PropertyField has no subtype!");
                    break;
            }

            // get the deed group and if the owner of current deed is owner of both deeds, raise rent to group rent
            UUID[] deedIDs = DeedManager.getInstance().getDeedGroupDeeds(GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID)).getFieldColor());
             sameOwnerCount = 0;
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
