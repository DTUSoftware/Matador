package dk.dtu.matador.managers;

import dk.dtu.matador.Game;
import dk.dtu.matador.objects.Deed;
import dk.dtu.matador.objects.DiceCup;
import dk.dtu.matador.objects.GameBoard;
import dk.dtu.matador.objects.chancecards.ChanceCard;
import dk.dtu.matador.objects.fields.Field;
import dk.dtu.matador.objects.fields.PropertyField;
import dk.dtu.matador.objects.fields.StreetField;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

/**
 * The GameManager controls the flow of the game, and has an instance of a game board as well as a dice cup.
 */
public class GameManager {
    private static GameManager gameManager;
    private static GameBoard gameBoard;
    private static DiceCup diceCup;

    private Deque<UUID> playerQueue;
    private HashMap<UUID, Integer> playerPositions;
    private boolean gameFinished = false;

    private static boolean initialized = false;

    private GameManager() {
        gameBoard = new GameBoard();
        diceCup = new DiceCup();
        initialized = true;
    }

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }

        return gameManager;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public DiceCup getDiceCup() {
        return diceCup;
    }

    /**
     * Makes ready for a game with the given players.
     *
     * @param playerIDs The UUID's of the players that are going to be playing.
     */
    public void setupGame(UUID[] playerIDs) {
        playerQueue = new ArrayDeque<>();
        playerQueue.addAll(Arrays.asList(playerIDs));

        playerPositions = new HashMap<>();
        for (UUID playerID : playerIDs) {
            setPlayerPosition(playerID, 0, false);
            PlayerManager.getInstance().getPlayer(playerID).setBalance(Game.getStartBalance());
            for (UUID deedID : DeedManager.getInstance().getPlayerDeeds(playerID)) {
                DeedManager.getInstance().setDeedOwnership(deedID, null);
                Deed deed = DeedManager.getInstance().getDeed(deedID);
                deed.resetDeed();
                Field field = gameBoard.getFieldFromID(DeedManager.getInstance().getFieldID(deedID));
                if (field instanceof StreetField) {
                    ((StreetField) field).demolishHouse();
                    ((StreetField) field).demolishHotel();
                }
            }
        }

        gameFinished = false;
    }

    /**
     * Stops the game, in case someone reaches a losing condition.
     */
    public void finishGame() {
        gameFinished = true;
    }

    /**
     * Removes the given player from the game's queue, making their current turn their final.
     *
     * @param playerID The player to remove from the game.
     */
    public void removePlayerFromGame(UUID playerID) {
        playerQueue.remove(playerID);
    }

    public UUID[] getPlayersCurrentlyInGame() {
        if (playerQueue == null) {
            return new UUID[0];
        }
        return playerQueue.toArray(new UUID[0]);
    }

    /**
     * Starts the game.
     */
    public void play() {
        UUID currentPlayer;
        // Loop until the game finishes
        while (!gameFinished) {
            Game.logDebug(Arrays.toString(getPlayersCurrentlyInGame()));
            // Get next player in queue
            currentPlayer = playerQueue.getFirst();

            // Let the player play
            playerPlay(currentPlayer);

            // Check if the player is still first in queue
            if (playerQueue.getFirst() == currentPlayer) {
                // Remove player from first in queue and put to end
                playerQueue.removeFirst();
                playerQueue.addLast(currentPlayer);
            }
            Game.logDebug(Arrays.toString(getPlayersCurrentlyInGame()));
        }

        // find out which player won
        UUID maxPlayer = null;
        UUID otherPlayer = null;
        double maxValue = 0.0;
        for (UUID playerID : PlayerManager.getInstance().getPlayerIDs()) {
            double balance = PlayerManager.getInstance().getPlayer(playerID).getBalance();
            if (balance > maxValue) {
                maxPlayer = playerID;
                maxValue = balance;
                otherPlayer = null;
            } else if (balance == maxValue) {
                otherPlayer = playerID;
            }
        }

        if (otherPlayer == null) {
            GUIManager.getInstance().showMessage(
                    LanguageManager.getInstance().getString("player_won_balance")
                            .replace("{player_name}", PlayerManager.getInstance().getPlayer(maxPlayer).getName())
                            .replace("{balance}", Float.toString(Math.round(maxValue)))
            );
        } else {
            maxPlayer = null;
            otherPlayer = null;
            maxValue = 0.0;

            for (UUID playerID : PlayerManager.getInstance().getPlayerIDs()) {
                double wealth = PlayerManager.getInstance().getPlayer(playerID).getBalance();

                for (UUID deedID : DeedManager.getInstance().getPlayerDeeds(playerID)) {
                    wealth = wealth + DeedManager.getInstance().getDeed(deedID).getPrice();
                }

                if (wealth > maxValue) {
                    maxPlayer = playerID;
                    maxValue = wealth;
                    otherPlayer = null;
                } else if (wealth == maxValue) {
                    otherPlayer = playerID;
                }
            }

            if (otherPlayer == null) {
                GUIManager.getInstance().showMessage(
                        LanguageManager.getInstance().getString("player_won_wealth")
                                .replace("{player_name}", PlayerManager.getInstance().getPlayer(maxPlayer).getName())
                                .replace("{balance}", Float.toString(Math.round(maxValue)))
                );
            } else {
                GUIManager.getInstance().showMessage(
                        LanguageManager.getInstance().getString("game_tie")
                                .replace("{player1_name}", PlayerManager.getInstance().getPlayer(maxPlayer).getName())
                                .replace("{player2_name}", PlayerManager.getInstance().getPlayer(otherPlayer).getName())
                );
            }
        }
    }

    private void processCheatCodeInput(UUID playerID) {
        while (true) {
            String cheatCode = GUIManager.getInstance().getUserString("Enter debug command(s). Continue game with 'x'");
            if (cheatCode.equals("x")) {
                break;
            }

            String fieldName;
            UUID fieldID;
            Field field;
            switch (cheatCode.split(" ")[0].toLowerCase()) {
                case "give":
                    fieldName = cheatCode.split(" ")[1];
                    fieldID = gameBoard.getFieldIDFromType(fieldName);
                    UUID deedID = DeedManager.getInstance().getDeedID(fieldID);
                    DeedManager.getInstance().setDeedOwnership(deedID, playerID);
                    DeedManager.getInstance().updatePlayerDeedPrices(playerID);
                    break;
                case "move":
                    fieldName = cheatCode.split(" ")[1];
                    fieldID = gameBoard.getFieldIDFromType(fieldName);
                    int fieldPosition = gameBoard.getFieldPosition(fieldID);
                    setPlayerBoardPosition(playerID, fieldPosition, true);
                    break;
                case "build":
                    fieldName = cheatCode.split(" ")[1];
                    fieldID = gameBoard.getFieldIDFromType(fieldName);
                    field = gameBoard.getFieldFromID(fieldID);
                    Deed deed = DeedManager.getInstance().getDeed(DeedManager.getInstance().getDeedID(fieldID));
                    if (deed.canBuildHouse()) {
                        deed.addHouse();
                        ((StreetField) field).buildHouse();
                        ((StreetField) field).updatePrices(deed.getID());
                    }
                    else if (deed.canBuildHotel()) {
                        deed.addHotel();
                        ((StreetField) field).buildHotel();
                        ((StreetField) field).updatePrices(deed.getID());
                    }
                    break;
                case "chancecard":
                    int cardNumber = Integer.parseInt(cheatCode.split(" ")[1]);
                    ChanceCard cc = GameManager.getInstance().getGameBoard().getChanceCard(cardNumber);
                    cc.showCardMessage();
                    cc.doCardAction(playerID);
                    break;
                case "balance":
                    double amount = 0.0;
                    switch (cheatCode.split(" ")[1].toLowerCase()) {
                        case "give":
                            amount = Integer.parseInt(cheatCode.split(" ")[2]);
                            PlayerManager.getInstance().getPlayer(playerID).deposit(amount);
                            break;
                        case "set":
                            amount = Integer.parseInt(cheatCode.split(" ")[2]);
                            PlayerManager.getInstance().getPlayer(playerID).setBalance(amount);
                            break;
                    }
                    break;
                default:
                    GUIManager.getInstance().showMessage("Not a valid command!");
                    break;
            }
        }
    }

    public HashMap<String, Boolean> playerChooseAction(String[] actions, UUID playerID) {
        HashMap<String, Boolean> actionsPerformed = new HashMap<>();

        for (String action : actions) {
            actionsPerformed.put(action, false);
        }

        int playerPosition = playerPositions.get(playerID);
        UUID[] playerDeeds = DeedManager.getInstance().getPlayerDeeds(playerID);

        String action = null;
        // if more than just roll is available for the player
        if (actions.length == 1 && actions[0].equals("roll")) {
            action = "roll";
        } else {
            // player chooses an action
            action = GUIManager.getInstance().askAction(actions);
        }

        // if the player both is jailed and doesn't have anything to build, the action is null
        if (action != null) {
            UUID deedID, fieldID;
            UUID[] deedIDs, fieldIDs;
            String buildingType;
            Deed deed;
            boolean confirmed;
            int i;
            // Switch on the action cases
            switch (action) {
                // Roll the die
                case "roll":
                    if (!Game.debug) {
                        GUIManager.getInstance().waitUserRoll();
                    }
                    actionsPerformed.put(action, true);
                    diceCup.raffle();

                    int[] diceValues = diceCup.getValues();
                    GUIManager.getInstance().updateDice(diceValues[0], diceValues[1]);

                    // Positions
                    int newPlayerPosition = playerPosition + diceCup.getSum();
                    playerPositions.put(playerID, newPlayerPosition);

                    Field field = GUIManager.getInstance().movePlayerField(playerID, playerPositions.get(playerID) % gameBoard.getFieldAmount());

                    // Check for passing start
                    if (((int) (playerPosition / gameBoard.getFieldAmount())) < ((int) (newPlayerPosition / gameBoard.getFieldAmount()))) {
                        // passed start
                        PlayerManager.getInstance().getPlayer(playerID).deposit(Game.getStartPassReward());
                        GUIManager.getInstance().showMessage(
                                LanguageManager.getInstance().getString("passed_start")
                                        .replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName())
                                        .replace("{start_pass_amount}", Double.toString(Game.getStartPassReward()))
                        );
                    }

                    field.doLandingAction(playerID);
                    break;
                // Build buildings on deeds
                case "build":
                    HashMap<UUID, String> buildableDeeds = new HashMap<>(); // deedID, buildingType (house, hotel)

                    for (i = 0; i < playerDeeds.length; i++) {
                        deedID = playerDeeds[i];
                        deed = DeedManager.getInstance().getDeed(deedID);

                        // if they can build houses or hotels, add the deed to the map of buildable deeds
                        if (deed.canBuildHouse()) {
                            buildableDeeds.put(deedID, "house");
                        } else if (deed.canBuildHotel()) {
                            buildableDeeds.put(deedID, "hotel");
                        }
                    }

                    deedIDs = buildableDeeds.keySet().toArray(new UUID[0]);
                    fieldIDs = new UUID[deedIDs.length];
                    for (i = 0; i < deedIDs.length; i++) {
                        fieldIDs[i] = DeedManager.getInstance().getFieldID(deedIDs[i]);
                    }
                    // player chooses a field
                    fieldID = GUIManager.getInstance().askProperty(fieldIDs, action);

                    // confirm to build
                    deedID = DeedManager.getInstance().getDeedID(fieldID);
                    deed = DeedManager.getInstance().getDeed(deedID);
                    buildingType = buildableDeeds.get(deedID);
                    confirmed = GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("confirm_build")
                            .replace("{price}", ((buildingType.equals("house")) ? Double.toString(deed.getHousePrice()) : Double.toString(deed.getHotelPrice())))
                            .replace("{building}", LanguageManager.getInstance().getString(buildingType))
                            .replace("{propertyDisplayName}", LanguageManager.getInstance().getString("field_" + gameBoard.getFieldFromID(fieldID).getFieldName() + "_name")));

                    if (confirmed) {
                        Field buildField = gameBoard.getFieldFromID(fieldID);
                        switch (buildingType) {
                            case "house":
                                // take money
                                PlayerManager.getInstance().getPlayer(playerID).withdraw(deed.getHousePrice());
                                // build on the property
                                deed.addHouse();
                                ((StreetField) buildField).buildHouse();
                                ((StreetField) buildField).updatePrices(deedID);
                                break;
                            case "hotel":
                                // take money
                                PlayerManager.getInstance().getPlayer(playerID).withdraw(deed.getHotelPrice());
                                // build on the property
                                deed.addHotel();
                                ((StreetField) buildField).buildHotel();
                                ((StreetField) buildField).updatePrices(deedID);
                                break;
                            default:
                                Game.logDebug("Something's wrong with the buildingType...");
                        }
                        actionsPerformed.put(action, true);
                    }
                    break;
                // Sell buildings on deeds
                case "sell":
                    HashMap<UUID, String> deedsWithBuildings = new HashMap<>(); // deedID, buildingType (house, hotel)

                    for (i = 0; i < playerDeeds.length; i++) {
                        deedID = playerDeeds[i];
                        deed = DeedManager.getInstance().getDeed(deedID);

                        // if they have houses or a hotel
                        if (deed.getHouses() > 0) {
                            deedsWithBuildings.put(deedID, "house");
                        } else if (deed.getHotels() > 0) {
                            deedsWithBuildings.put(deedID, "hotel");
                        }
                    }

                    deedIDs = deedsWithBuildings.keySet().toArray(new UUID[0]);
                    fieldIDs = new UUID[deedIDs.length];
                    for (i = 0; i < deedIDs.length; i++) {
                        fieldIDs[i] = DeedManager.getInstance().getFieldID(deedIDs[i]);
                    }
                    // player chooses a field
                    fieldID = GUIManager.getInstance().askProperty(fieldIDs, action);

                    // confirm to build
                    deedID = DeedManager.getInstance().getDeedID(fieldID);
                    deed = DeedManager.getInstance().getDeed(deedID);
                    buildingType = deedsWithBuildings.get(deedID);
                    confirmed = GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("confirm_sell")
                            .replace("{payout}", ((buildingType.equals("house")) ? Double.toString((deed.getHousePrice() / 2)) : Double.toString(((deed.getHotelPrice() + (deed.getHousePrice() * 4)) / 2))))
                            .replace("{building}", LanguageManager.getInstance().getString(buildingType))
                            .replace("{propertyDisplayName}", LanguageManager.getInstance().getString("field_" + gameBoard.getFieldFromID(fieldID).getFieldName() + "_name")));

                    if (confirmed) {
                        Field buildField = gameBoard.getFieldFromID(fieldID);
                        switch (buildingType) {
                            case "house":
                                // give money
                                PlayerManager.getInstance().getPlayer(playerID).deposit(deed.getHousePrice() / 2);
                                // remove the property
                                deed.removeHouse();
                                ((StreetField) buildField).demolishHouse();
                                ((StreetField) buildField).updatePrices(deedID);
                                break;
                            case "hotel":
                                // give money
                                PlayerManager.getInstance().getPlayer(playerID).deposit((deed.getHotelPrice() + (deed.getHousePrice() * 4)) / 2);
                                // remove the property
                                deed.removeHotel();
                                ((StreetField) buildField).demolishHotel();
                                ((StreetField) buildField).updatePrices(deedID);
                                break;
                            default:
                                Game.logDebug("Something's wrong with the buildingType...");
                        }
                        actionsPerformed.put(action, true);
                    }
                    break;
                // Prawn/mortgage deeds without buildings on them
                case "prawn":
                    ArrayList<UUID> prawnableDeeds = new ArrayList<>(); // deedID

                    for (i = 0; i < playerDeeds.length; i++) {
                        deedID = playerDeeds[i];
                        deed = DeedManager.getInstance().getDeed(deedID);

                        // if they can build houses or hotels, add the deed to the list of prawnable deeds
                        if (deed.getHouses() == 0 && deed.getHotels() == 0 && !deed.isPrawned()) {
                            prawnableDeeds.add(deedID);
                        }
                    }

                    deedIDs = prawnableDeeds.toArray(new UUID[0]);
                    fieldIDs = new UUID[deedIDs.length];
                    for (i = 0; i < deedIDs.length; i++) {
                        fieldIDs[i] = DeedManager.getInstance().getFieldID(deedIDs[i]);
                    }
                    // player chooses a field
                    fieldID = GUIManager.getInstance().askProperty(fieldIDs, action);

                    // confirm to build
                    deedID = DeedManager.getInstance().getDeedID(fieldID);
                    deed = DeedManager.getInstance().getDeed(deedID);
                    confirmed = GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("confirm_prawn")
                            .replace("{payout}", (Double.toString((deed.getPrawnPrice()))))
                            .replace("{propertyDisplayName}", LanguageManager.getInstance().getString("field_" + gameBoard.getFieldFromID(fieldID).getFieldName() + "_name")));

                    if (confirmed) {
                        Field buildField = gameBoard.getFieldFromID(fieldID);

                        // give money
                        PlayerManager.getInstance().getPlayer(playerID).deposit(deed.getPrawnPrice());
                        // prawn the property
                        deed.prawn();
                        ((PropertyField) buildField).updatePrices(deedID);
                        actionsPerformed.put(action, true);
                    }
                    break;
                // Buy back prawned deeds
                case "buy_back":
                    ArrayList<UUID> buyBackDeeds = new ArrayList<>(); // deedID

                    for (i = 0; i < playerDeeds.length; i++) {
                        deedID = playerDeeds[i];
                        deed = DeedManager.getInstance().getDeed(deedID);

                        // if it's prawned
                        if (deed.isPrawned()) {
                            // if they have enough money
                            if (PlayerManager.getInstance().getPlayer(playerID).getBalance() >= deed.getBuyBackPrice()) {
                                buyBackDeeds.add(deedID);
                            }
                        }
                    }

                    deedIDs = buyBackDeeds.toArray(new UUID[0]);
                    fieldIDs = new UUID[deedIDs.length];
                    for (i = 0; i < deedIDs.length; i++) {
                        fieldIDs[i] = DeedManager.getInstance().getFieldID(deedIDs[i]);
                    }
                    // player chooses a field
                    fieldID = GUIManager.getInstance().askProperty(fieldIDs, action);

                    // confirm to build
                    deedID = DeedManager.getInstance().getDeedID(fieldID);
                    deed = DeedManager.getInstance().getDeed(deedID);
                    confirmed = GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("confirm_buy_back")
                            .replace("{price}", (Double.toString((deed.getBuyBackPrice()))))
                            .replace("{propertyDisplayName}", LanguageManager.getInstance().getString("field_" + gameBoard.getFieldFromID(fieldID).getFieldName() + "_name")));

                    if (confirmed) {
                        Field buildField = gameBoard.getFieldFromID(fieldID);

                        // take money
                        PlayerManager.getInstance().getPlayer(playerID).withdraw(deed.getBuyBackPrice());
                        // buy back the property
                        deed.buyBack();
                        ((PropertyField) buildField).updatePrices(deedID);
                        actionsPerformed.put(action, true);
                    }
                    break;
                // Trade/sell deeds without any buildings on them
                case "trade":
                    ArrayList<UUID> tradeableDeeds = new ArrayList<>(); // deedID

                    for (i = 0; i < playerDeeds.length; i++) {
                        deedID = playerDeeds[i];
                        deed = DeedManager.getInstance().getDeed(deedID);

                        // if they can build houses or hotels, add the deed to the list of tradeable deeds
                        if (deed.getHouses() == 0 && deed.getHotels() == 0) {
                            tradeableDeeds.add(deedID);
                        }
                    }

                    deedIDs = tradeableDeeds.toArray(new UUID[0]);
                    fieldIDs = new UUID[deedIDs.length];
                    for (i = 0; i < deedIDs.length; i++) {
                        fieldIDs[i] = DeedManager.getInstance().getFieldID(deedIDs[i]);
                    }
                    // player chooses a field
                    fieldID = GUIManager.getInstance().askProperty(fieldIDs, action);
                    deedID = DeedManager.getInstance().getDeedID(fieldID);
                    deed = DeedManager.getInstance().getDeed(deedID);

                    // choose a player to trade it to
                    UUID tradePlayerID = GUIManager.getInstance().askPlayer(playerID, action);

                    tryTrade(deed, fieldID, playerID, tradePlayerID);
                    break;
                default:
                    break;
            }
        }
        return actionsPerformed;
    }

    private boolean tryTrade(Deed deed, UUID fieldID, UUID playerID, UUID tradePlayerID) {
        String action = "trade";
        // choose amount to sell for
        double[] priceOptions = {
                50.0, 100.0, 500.0, 1000.0, 2000.0, 5000.0,
                deed.getPrice(), deed.getPrice() + 50.0, deed.getPrice() + 100.0,
                deed.getPrice() + 500.0, deed.getPrice() + 1000.0,
                deed.getPrice() + 2000.0, deed.getPrice() + 5000.0};

        // remove prices above player balance
        double tradePlayerBalance = PlayerManager.getInstance().getPlayer(tradePlayerID).getBalance();
        for (int j = 0; j < priceOptions.length; j++) {
            if (tradePlayerBalance < priceOptions[j]) {
                priceOptions = ArrayUtils.remove(priceOptions, j);
            }
        }

        // remove duplicates and sort the prices
        Set<Double> priceSet = new TreeSet<>();
        for (double price : priceOptions) {
            priceSet.add(price);
        }
        priceOptions = ArrayUtils.toPrimitive(priceSet.toArray(new Double[0]));

        double tradePrice = GUIManager.getInstance().askPrice(priceOptions, LanguageManager.getInstance().getString("choose_a_price_"+action)
                .replace("{tradePlayerName}", PlayerManager.getInstance().getPlayer(tradePlayerID).getName())
                .replace("{playerName}", PlayerManager.getInstance().getPlayer(playerID).getName())
                .replace("{propertyDisplayName}", LanguageManager.getInstance().getString("field_"+gameBoard.getFieldFromID(fieldID).getFieldName()+"_name")));

        // confirm to trade
        boolean confirmed = GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("confirm_"+action)
                .replace("{tradePlayerName}", PlayerManager.getInstance().getPlayer(tradePlayerID).getName())
                .replace("{playerName}", PlayerManager.getInstance().getPlayer(playerID).getName())
                .replace("{price}", (Double.toString(tradePrice)))
                .replace("{propertyDisplayName}", LanguageManager.getInstance().getString("field_"+gameBoard.getFieldFromID(fieldID).getFieldName()+"_name")));

        if (confirmed) {
            // take money
            PlayerManager.getInstance().getPlayer(tradePlayerID).withdraw(tradePrice);
            // give money
            PlayerManager.getInstance().getPlayer(playerID).deposit(tradePrice);
            // transfer the property
            DeedManager.getInstance().setDeedOwnership(deed.getID(), tradePlayerID);
            DeedManager.getInstance().updatePlayerDeedPrices(tradePlayerID);
            DeedManager.getInstance().updatePlayerDeedPrices(playerID);

            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("trade_confirmed")
                    .replace("{playerName}", PlayerManager.getInstance().getPlayer(tradePlayerID).getName())
                    .replace("{propertyDisplayName}", LanguageManager.getInstance().getString("field_"+gameBoard.getFieldFromID(fieldID).getFieldName()+"_name")));
            return true;
        }
        else {
            boolean tryAgain = GUIManager.getInstance().askPrompt(LanguageManager.getInstance().getString("trade_price_denied"));
            if (tryAgain) {
                return tryTrade(deed, fieldID, playerID, tradePlayerID);
            }
            else {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("trade_cancelled"));
                return false;
            }
        }
    }

    /**
     * Performs a player's turn.
     *
     * @param playerID The UUID of the player whose turn it is.
     */
    private void playerPlay(UUID playerID) {
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("player_turn").replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName()));
        Game.logDebug("Player turn: " + PlayerManager.getInstance().getPlayer(playerID).getName());

        if (Game.debug) {
            processCheatCodeInput(playerID);
        }

        int turnCounter = 0;
        do {
            turnCounter++;
            if (turnCounter > 3) {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("feature_speeding").replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName()));
                PlayerManager.getInstance().getPlayer(playerID).jail();
                GameManager.getInstance().setPlayerBoardPosition(playerID, GameManager.getInstance().getGameBoard().getFieldPosition(GameManager.getInstance().getGameBoard().getFieldIDFromType("JailField")), false);
                break;
            }
            if (turnCounter > 1) {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("feature_extraturn").replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName()));
            }

            int playerPosition = playerPositions.get(playerID);
            // Do leaving action
            gameBoard.getField(playerPosition%gameBoard.getFieldAmount()).doLeavingAction(playerID);

            // loop actions, until the player rolls a dice, or is removed from the queue
            // for example, if player builds houses on two deeds, the turn is still not over
            // but if they go banktrupt, then they shouldn't continue their turn
            boolean rolledDice = false;
            while (!rolledDice && playerQueue.contains(playerID) && !PlayerManager.getInstance().getPlayer(playerID).isJailed()) {
                // Which actions can the player currently take
                HashMap<String, Boolean> actionMap = new HashMap<String, Boolean>() {{
                    put("roll", false);
                    put("build", false);
                    put("sell", false);
                    put("prawn", false);
                    put("buy_back", false);
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
                            if (deed.getHouses() == 0 && deed.getHotels() == 0) {
                                // you can also trade deeds with no houses on them, even if it's prawned
                                actionMap.put("trade", true);
                                if (!deed.isPrawned()) {
                                    // if the deed does not have any houses or hotels on it, enable the option to prawn the deed
                                    actionMap.put("prawn", true);
                                }
                            }
                        }

                        if (!actionMap.get("buy_back")) {
                            if (deed.isPrawned()) {
                                if (PlayerManager.getInstance().getPlayer(playerID).getBalance() >= deed.getBuyBackPrice()) {
                                    actionMap.put("buy_back", true);
                                }
                            }
                        }

                        if (!actionMap.get("build")) {
                            // if they can build houses or hotels, set build action to true
                            if (deed.canBuildHouse()) {
                                actionMap.put("build", true);
                            }
                            else if (deed.canBuildHotel()) {
                                actionMap.put("build", true);
                            }
                        }

                        // break the loop as soon as all the possible actions for deeds are found, to keep processing
                        // low, and not have to loop through ALL the deeds, if possible.
                        if (actionMap.get("sell") && actionMap.get("build") && actionMap.get("prawn") && actionMap.get("buy_back") && actionMap.get("trade")) {
                            break;
                        }
                    }
                }
                if (!PlayerManager.getInstance().getPlayer(playerID).isJailed()) {
                    actionMap.put("roll", true);
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

                HashMap<String, Boolean> actionsPerformed = playerChooseAction(actions, playerID);
                if (actionsPerformed.containsKey("roll")) {
                    rolledDice = actionsPerformed.get("roll");
                }
            }
        } while (diceCup.getValues()[0] == diceCup.getValues()[1]);
    }

    /**
     * Gets the current position of the player.
     *
     * @param playerID The UUID of the player.
     * @return The position of the player (not on the board, but in amount of moves).
     */
    public int getPlayerPosition(UUID playerID) {
        return playerPositions.get(playerID);
    }

    /**
     * Sets the player's position on the board.
     *
     * @param playerID        The UUID of the player to move.
     * @param boardPosition   The desired board position.
     * @param giveStartReward Whether to give the player money when passing GO! (not wished during jailing).
     */
    public void setPlayerBoardPosition(UUID playerID, int boardPosition, boolean giveStartReward) {
        setPlayerBoardPosition(playerID, boardPosition, giveStartReward, false);
    }

    /**
     * Sets the player's position on the board.
     *
     * @param playerID        The UUID of the player to move.
     * @param boardPosition   The desired board position.
     * @param giveStartReward Whether to give the player money when passing GO! (not wished during jailing).
     * @param buyForFree      Whether to provide the player with the deed for free, if the field is vacant.
     */
    public void setPlayerBoardPosition(UUID playerID, int boardPosition, boolean giveStartReward, boolean buyForFree) {
        int oldPlayerPosition = playerPositions.get(playerID);
        int currentBoardPosition = oldPlayerPosition % gameBoard.getFieldAmount();

        if (boardPosition > currentBoardPosition) {
            setPlayerPosition(playerID, oldPlayerPosition + (boardPosition - currentBoardPosition), buyForFree);
        } else {
            setPlayerPosition(playerID, oldPlayerPosition + (gameBoard.getFieldAmount() - currentBoardPosition) + boardPosition, buyForFree);
            if (giveStartReward) {
                PlayerManager.getInstance().getPlayer(playerID).deposit(Game.getStartPassReward());
                GUIManager.getInstance().showMessage(
                        LanguageManager.getInstance().getString("passed_start")
                                .replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName())
                                .replace("{start_pass_amount}", Double.toString(Game.getStartPassReward()))
                );
            }
        }
    }

    /**
     * Sets the player's position, and executes the action of the field when the player is moved to it.
     * This is NOT the board position, but the amount of moves taken position.
     *
     * @param playerID       The UUID of the player to move.
     * @param playerPosition The desired position.
     * @param buyForFree     Whether to provide the player with the deed for free, if the field is vacant.
     */
    public void setPlayerPosition(UUID playerID, int playerPosition, boolean buyForFree) {
        playerPositions.put(playerID, playerPosition);
        if (GUIManager.getInstance().guiInitialized()) {
            Field field = GUIManager.getInstance().movePlayerField(playerID, playerPositions.get(playerID) % gameBoard.getFieldAmount());
            if (buyForFree) {
                if (field instanceof PropertyField) {
                    ((PropertyField) field).doLandingAction(playerID, true);
                } else {
                    field.doLandingAction(playerID);
                }
            } else {
                field.doLandingAction(playerID);
            }
        }
    }
}
