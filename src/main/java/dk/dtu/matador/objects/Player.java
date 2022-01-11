package dk.dtu.matador.objects;

import dk.dtu.matador.Game;
import dk.dtu.matador.managers.*;
import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.objects.fields.PropertyField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
     * Sells properties, to get more money.
     *
     * @return true, if they had something to sell or prawn.
     */
    private boolean tryAvoidBankruptcy() {
        Game.logDebug("Balance before try avoid bankryptcy: " + Double.toString(getBalance()));
        // ask the player to take an action
        // Which actions can the player currently take
        HashMap<String, Boolean> actionMap = new HashMap<String, Boolean>() {{
            put("sell", false);
            put("prawn", false);
            put("trade", false);
        }};

        // check if the player has any deeds
        UUID[] playerDeeds = DeedManager.getInstance().getPlayerDeeds(playerID);
        if (playerDeeds.length != 0) {
            // check the deeds for the possible actions the player can perform, and enable the actions
            // if the actions can be performed.
            for (UUID deedID : playerDeeds) {
                Deed deed = DeedManager.getInstance().getDeed(deedID);

                if (!actionMap.get("sell")) {
                    if (deed.getHouses() > 0 || deed.getHotels() > 0) {
                        // if the deed has houses or hotels on it, add the option to sell the buildings
                        actionMap.put("sell", true);
                    }
                }

                if (!actionMap.get("prawn") || !actionMap.get("trade")) {
                    if (deed.getHouses() == 0 && deed.getHotels() == 0 && !deed.isPrawned()) {
                        // if the deed does not have any houses or hotels on it, enable the option to prawn the deed
                        actionMap.put("prawn", true);
                        // you can also trade deeds with no houses on them, if it isn't pawned
                        actionMap.put("trade", true);
                    }
                }

                // break the loop as soon as all the possible actions for deeds are found, to keep processing
                // low, and not have to loop through ALL the deeds, if possible.
                if (actionMap.get("sell") && actionMap.get("prawn") && actionMap.get("trade")) {
                    break;
                }
            }
        }

        // put all the actions that are true into a list
        ArrayList<String> actionList = new ArrayList<>();
        for (String action : actionMap.keySet()) {
            if (actionMap.get(action)) {
                actionList.add(action);
            }
        }

        // Convert action list to an array, so we easily and effectively can loop through and switch on cases
        String[] actions = actionList.toArray(new String[0]);

        if (actions.length > 0) {
            HashMap<String, Boolean> actionsPerformed = GameManager.getInstance().playerChooseAction(actions, playerID);

            for (String action : actions) {
                if (actionsPerformed.get(action)) {
                    Game.logDebug("Player performed the " + action + " action, to avoid bankruptcy.");
                }
            }
            Game.logDebug("Balance after try avoid bankryptcy, and taking action: " + Double.toString(getBalance()));
            return true;
        }
        Game.logDebug("Balance after try avoid bankryptcy, no action taken: " + Double.toString(getBalance()));
        return false;
    }

    /**
     * Remove the player from the game, and set their properties, if any, on auction.
     */
    private void handleBankruptcy(UUID otherPlayerID) {
        if (otherPlayerID != null) {
            // give the rest of the balance to the other player
            PlayerManager.getInstance().getPlayer(otherPlayerID).deposit(this.getBalance());
            setBalance(0.0);
        }
        else {
            setBalance(0.0);
        }

        if (GameManager.getInstance().getPlayersCurrentlyInGame().length > 2) {
            Game.logDebug(Arrays.toString(GameManager.getInstance().getPlayersCurrentlyInGame()));
            Game.logDebug("Remove " + getName() + " from the game.");
            GameManager.getInstance().removePlayerFromGame(playerID);
            Game.logDebug(Arrays.toString(GameManager.getInstance().getPlayersCurrentlyInGame()));
        }
        else if (GameManager.getInstance().getPlayersCurrentlyInGame().length == 2) {
            Game.logDebug(Arrays.toString(GameManager.getInstance().getPlayersCurrentlyInGame()));
            Game.logDebug("Remove " + getName() + " from the game, and finish the game.");
            // bankrupt the player, and finish the game
            GameManager.getInstance().removePlayerFromGame(playerID);
            Game.logDebug(Arrays.toString(GameManager.getInstance().getPlayersCurrentlyInGame()));
            GameManager.getInstance().finishGame();
        }

        // put all deeds on auction
        for (UUID deedID : DeedManager.getInstance().getPlayerDeeds(playerID)) {
            PropertyField field = (PropertyField) GameManager.getInstance().getGameBoard().getFieldFromID(DeedManager.getInstance().getFieldID(deedID));
            boolean propertyBought = field.startAuction();
            if (!propertyBought) {
                DeedManager.getInstance().setDeedOwnership(deedID, null);
            }
        }
    }

    /**
     * Withdraws money from the player's account. Do not use negative numbers to withdraw, otherwise it will add to
     * the balance.
     *
     * @param amount The amount to subtract from the balance.
     * @param otherPlayerID The player that the money will be given to afterwards. THIS IS USED FOR HANDLING BANKRUPTCY, NOT TRANSFERRING AFTERWARDS.
     * @return Whether the transaction succeeded.
     */
    public boolean withdraw(double amount, UUID otherPlayerID) {
        boolean success = account.withdraw(amount);

        // If not success, handle bankruptcy
        if (!success) {
            if (tryAvoidBankruptcy()) {
                // try to withdraw again, and possibly have to handle bankruptcy again
                success = withdraw(amount, otherPlayerID);
            }
            else {
                // they didn't have enough properties to get money. Player is now bankrupt.
                handleBankruptcy(otherPlayerID);
            }
        }

        // update the GUI
        if (guiInitialized()) {
            GUIManager.getInstance().setPlayerBalance(playerID, getBalance());
        }

        return success;
    }

    /**
     * Withdraws money from the player's account. Do not use negative numbers to withdraw, otherwise it will add to
     * the balance.
     *
     * @param amount The amount to subtract from the balance.
     * @return Whether the transaction succeeded.
     */
    public boolean withdraw(double amount) {
        Game.logDebug("Before withdraw: " + Double.toString(account.getBalance()));
        boolean success = account.withdraw(amount);

        // If not success, handle bankruptcy
        if (!success) {
            Game.logDebug("Try to avoid bankruptcy for " + getName());
            if (tryAvoidBankruptcy()) {
                // try to withdraw again, and possibly have to handle bankruptcy again
                Game.logDebug("They tried an action. Try to withdraw again.");
                success = withdraw(amount);
            }
            else {
                // they didn't have enough properties to get money. Player is now bankrupt.
                Game.logDebug("Handle bankruptcy for " + getName());
                handleBankruptcy(null);
            }
            Game.logDebug("After withdraw: " + Double.toString(account.getBalance()));
        }

        // update the GUI
        if (guiInitialized()) {
            GUIManager.getInstance().setPlayerBalance(playerID, getBalance());
        }

        return success;
    }

    /**
     * Deposits money onto the player's account.
     *
     * @param amount The amount of money to add to the balance.
     */
    public void deposit(double amount) {
        Game.logDebug("Before deposit: " + Double.toString(account.getBalance()));
        account.deposit(amount);
        // update the GUI
        if (guiInitialized()) {
            GUIManager.getInstance().setPlayerBalance(playerID, getBalance());
        }
        Game.logDebug("After deposit: " + Double.toString(account.getBalance()));
    }

    /**
     * Sets the balance of the player's account.
     *
     * @param balance The new balance.
     */
    public void setBalance(double balance) {
        Game.logDebug("Before setBalance: " + Double.toString(account.getBalance()));
        account.setBalance(balance);
        // update the GUI
        if (guiInitialized()) {
            GUIManager.getInstance().setPlayerBalance(playerID, getBalance());
        }
        Game.logDebug("After setBalance: " + Double.toString(account.getBalance()));
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
    public void jailedTimeUp() {
        this.jailedTime++;
    }

    /**
     * gives how long the player has been jailed
     *
     * @return jailedTime as an integer
     */
    public int getJailedTime() {
        return this.jailedTime;
    }

    /**
     * Sets the jaiLedTime to the new jailed time
     *
     * @param newJailedTime integer for what the new JailedTime should be
     */
    public void setJailedTime(int newJailedTime) {
        this.jailedTime = newJailedTime;
    }

    /**
     * Takes one of the player's bail cards, if the player has one.
     *
     * @return <code>true</code> if the player has a bail card, and one was subtracted successfully,
     * else <code>false</code>.
     */
    public boolean takeBailCard() {
        if (this.bailCards > 0) {
            this.bailCards--;
            return true;
        }
        return false;
    }

    /**
     * Gives the balance of the player
     * @return balance as a double
     */
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