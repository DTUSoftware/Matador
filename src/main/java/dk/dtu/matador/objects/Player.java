package dk.dtu.matador.objects;

import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.DeedManager;

import java.util.UUID;

/**
 * The Player object.
 */
public class Player {
    private final String name;
    private final UUID playerID;
    private final Account account;
    private int bailCards = 0;
    private boolean jailed = false;
    private int jailedTime = 0;

    public Player(String name) {
        this.name = name;
        this.playerID = UUID.randomUUID();
        this.account = new Account();
    }

    public Player(String name, double startingBalance) {
        this.name = name;
        this.playerID = UUID.randomUUID();
        this.account = new Account(startingBalance);
    }

    private boolean guiInitialized() {
        return GUIManager.getInstance().guiInitialized();
    }

    public String getName() {
        return name;
    }

    /**
     * Withdraws money from the player's account. Do not use negative numbers to withdraw, otherwise it will add to
     * the balance.
     *
     * @param amount    The amount to subtract from the balance.
     * @return          Whether the transaction succeeded.
     */
    public boolean withdraw(double amount) {
        boolean success = account.withdraw(amount);
        // update the GUI
        if (guiInitialized()) {
            GUIManager.getInstance().setPlayerBalance(playerID, getBalance());
        }
        return success;
    }

    /**
     * Deposits money onto the player's account.
     *
     * @param amount    The amount of money to add to the balance.
     */
    public void deposit(double amount) {
        account.deposit(amount);
        // update the GUI
        if (guiInitialized()) {
            GUIManager.getInstance().setPlayerBalance(playerID, getBalance());
        }
    }

    /**
     * Sets the balance of the player's account.
     *
     * @param balance   The new balance.
     */
    public void setBalance(double balance) {
        account.setBalance(balance);
        // update the GUI
        if (guiInitialized()) {
            GUIManager.getInstance().setPlayerBalance(playerID, getBalance());
        }
    }

    private void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    /**
     * Jails the player (doesn't move the player to the jail).
     */
    public void jail() {
        setJailed(true);
    }

    /**
     * Un-jails the player (doesn't move the player out of the jail).
     */
    public void unJail() {
        setJailed(false);
        setJailedTime(0);
    }

    public boolean isJailed() {
        return jailed;
    }

    /**
     * Gives the player a so-called "get out of jail free" card.
     */
    public void giveBailCard() {
        this.bailCards++;
    }

    /**
     * Counts for how many rounds the player has been in jail.
     */
    public void jailedTimeUp () {this.jailedTime++;}

    /**
     * gives how long the player has been jailed
     * @return jailedTime as an integer
     */
    public int getJailedTime () {return this.jailedTime;}

    /**
     * Sets the jaiLedTime to the new jailed time
     * @param newJailedTime integer for what the new JailedTime should be
     */
    public void setJailedTime (int newJailedTime){this.jailedTime=newJailedTime;}

    /**
     * Takes one of the player's bail cards, if the player has one.
     *
     * @return  <code>true</code> if the player has a bail card, and one was subtracted successfully,
     * else <code>false</code>.
     */
    public boolean takeBailCard() {
        if (this.bailCards > 0) {
            this.bailCards--;
            return true;
        }
        return false;
    }

    public double getBalance() {
        return account.getBalance();
    }

    public double getDeedBalance() {
        DeedManager dm = DeedManager.getInstance();

        UUID[] playerDeeds = dm.getPlayerDeeds(playerID);
        double unitedDeedBalance = 0;
        for (UUID deedID : playerDeeds)
            //TODO missing networth from houses mortgage and hotel(trivago)
            unitedDeedBalance += dm.getDeed(deedID).getPrice();
        return unitedDeedBalance;
    }

    public int getPlayerHouseOwnNumber() {
        int houses = 0;
        for (UUID deedID : DeedManager.getInstance().getPlayerDeeds(playerID)) {
            Deed deed = DeedManager.getInstance().getDeed(deedID);
            houses += deed.getHouses();
            }
        return houses;
    }

    public int getPlayerHotelOwnNumber() {
        int hotels = 0;
        for (UUID deedID : DeedManager.getInstance().getPlayerDeeds(playerID)) {
            Deed deed = DeedManager.getInstance().getDeed(deedID);
            hotels += deed.getHotels();
        }
        return hotels;
    }
  
    public double getNetWorth() {
        double netWorth = 0.0;
        netWorth += getBalance();

        // Calculate the worth of the player's deeds (even if it's prawned)
        for (UUID deedID : DeedManager.getInstance().getPlayerDeeds(playerID)) {
            Deed deed = DeedManager.getInstance().getDeed(deedID);
            netWorth += deed.getPrice();
            for (int i = 0; i < deed.getHouses(); i++) {
                netWorth += deed.getHousePrice();
            }
            for (int i = 0; i < deed.getHotels(); i++) {
                netWorth += deed.getHotelPrice();
            }
        }

        return netWorth;
    }

    public UUID getID() {
        return this.playerID;
    }
}