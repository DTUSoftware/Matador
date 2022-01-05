package dk.dtu.matador.managers;

import dk.dtu.matador.Game;
import dk.dtu.matador.GameInstance;
import dk.dtu.matador.objects.DiceCup;
import dk.dtu.matador.objects.GameBoard;
import dk.dtu.matador.objects.GameGUI;
import dk.dtu.matador.objects.fields.Field;
import dk.dtu.matador.objects.fields.PropertyField;

import java.util.*;

/**
 * The GameManager controls the flow of the game, and has an instance of a game board as well as a dice cup.
 */
public class GameManager {
    private final GameBoard gameBoard;
    private final DiceCup diceCup;
    private UUID gameID;

    private Deque<UUID> playerQueue;
    private HashMap<UUID, Integer> playerPositions;
    private boolean gameFinished = false;

    public GameManager(UUID gameID) {
        gameBoard = new GameBoard();
        diceCup = new DiceCup();
        this.gameID = gameID;
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
     * Starts the game.
     */
    public void play() {
        UUID currentPlayer;
        // Loop until the game finishes
        while (!gameFinished) {
            // Get next player in queue
            currentPlayer = playerQueue.getFirst();

            // Let the player play
            playerPlay(currentPlayer);

            // Remove player from first in queue and put to end
            playerQueue.removeFirst();
            playerQueue.addLast(currentPlayer);
        }

        // find out which player won
        UUID maxPlayer = null;
        UUID otherPlayer = null;
        double maxValue = 0.0;
        for (UUID playerID : PlayerManager.getInstance().getPlayerIDs(gameID)) {
            double balance = PlayerManager.getInstance().getPlayer(playerID).getBalance();
            if (balance > maxValue) {
                maxPlayer = playerID;
                maxValue = balance;
                otherPlayer = null;
            }
            else if (balance == maxValue) {
                otherPlayer = playerID;
            }
        }

        GameGUI gui = GUIManager.getInstance().getGUI(Game.getGameInstance(gameID).getGUIID());
        LanguageManager lm = Game.getGameInstance(gameID).getLanguageManager();
        if (otherPlayer == null) {
            gui.showMessage(
                    lm.getString("player_won_balance")
                            .replace("{player_name}", PlayerManager.getInstance().getPlayer(maxPlayer).getName())
                            .replace("{balance}", Float.toString(Math.round(maxValue)))
            );
        }
        else {
            maxPlayer = null;
            otherPlayer = null;
            maxValue = 0.0;

            for (UUID playerID : PlayerManager.getInstance().getPlayerIDs(gameID)) {
                double wealth = PlayerManager.getInstance().getPlayer(playerID).getBalance();

                for (UUID deedID : DeedManager.getInstance().getPlayerDeeds(playerID)) {
                    wealth = wealth + DeedManager.getInstance().getDeed(deedID).getPrice();
                }

                if (wealth > maxValue) {
                    maxPlayer = playerID;
                    maxValue = wealth;
                    otherPlayer = null;
                }
                else if (wealth == maxValue) {
                    otherPlayer = playerID;
                }
            }

            if (otherPlayer == null) {
                gui.showMessage(lm.getString("player_won_wealth")
                                    .replace("{player_name}", PlayerManager.getInstance().getPlayer(maxPlayer).getName())
                                    .replace("{balance}", Float.toString(Math.round(maxValue)))
                );
            }
            else {
                gui.showMessage(lm.getString("game_tie")
                                    .replace("{player1_name}", PlayerManager.getInstance().getPlayer(maxPlayer).getName())
                                    .replace("{player2_name}", PlayerManager.getInstance().getPlayer(otherPlayer).getName())
                );
            }
        }
    }

    /**
     * Performs a player's turn.
     *
     * @param playerID  The UUID of the player whose turn it is.
     */
    private void playerPlay(UUID playerID) {
        GameGUI gui = GUIManager.getInstance().getGUI(Game.getGameInstance(gameID).getGUIID());
        LanguageManager lm = Game.getGameInstance(gameID).getLanguageManager();
        gui.showMessage(lm.getString("player_turn").replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName()));

        int playerPosition = playerPositions.get(playerID);
        // Do leaving action
        gameBoard.getField(playerPosition%gameBoard.getFieldAmount()).doLeavingAction(playerID);

        if (!Game.debug) {
            gui.waitUserRoll();
        }
        diceCup.raffle();

        int[] diceValues = diceCup.getValues();
        gui.updateDice(diceValues[0], diceValues[1]);

        // Positions
        int newPlayerPosition = playerPosition+diceCup.getSum();
        playerPositions.put(playerID, newPlayerPosition);

        Field field = gui.movePlayerField(playerID, playerPositions.get(playerID)%gameBoard.getFieldAmount());

        // Check for passing start
        if (((int) (playerPosition/gameBoard.getFieldAmount())) < ((int) (newPlayerPosition/gameBoard.getFieldAmount()))) {
            // passed start
            PlayerManager.getInstance().getPlayer(playerID).deposit(Game.getStartPassReward());
            gui.showMessage(lm.getString("passed_start")
                            .replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName())
                            .replace("{start_pass_amount}", Double.toString(Game.getStartPassReward()))
            );
        }

        field.doLandingAction(playerID);
    }

    /**
     * Gets the current position of the player.
     *
     * @param playerID  The UUID of the player.
     * @return          The position of the player (not on the board, but in amount of moves).
     */
    public int getPlayerPosition(UUID playerID) {
        return playerPositions.get(playerID);
    }

    /**
     * Sets the player's position on the board.
     *
     * @param playerID          The UUID of the player to move.
     * @param boardPosition     The desired board position.
     * @param giveStartReward   Whether to give the player money when passing GO! (not wished during jailing).
     */
    public void setPlayerBoardPosition(UUID playerID, int boardPosition, boolean giveStartReward) {
        setPlayerBoardPosition(playerID, boardPosition, giveStartReward, false);
    }

    /**
     * Sets the player's position on the board.
     *
     * @param playerID          The UUID of the player to move.
     * @param boardPosition     The desired board position.
     * @param giveStartReward   Whether to give the player money when passing GO! (not wished during jailing).
     * @param buyForFree        Whether to provide the player with the deed for free, if the field is vacant.
     */
    public void setPlayerBoardPosition(UUID playerID, int boardPosition, boolean giveStartReward, boolean buyForFree) {
        int oldPlayerPosition = playerPositions.get(playerID);
        int currentBoardPosition = oldPlayerPosition % gameBoard.getFieldAmount();

        if (boardPosition > currentBoardPosition) {
            setPlayerPosition(playerID, oldPlayerPosition+(boardPosition-currentBoardPosition), buyForFree);
        }
        else {
            setPlayerPosition(playerID, oldPlayerPosition+(gameBoard.getFieldAmount()-currentBoardPosition)+boardPosition, buyForFree);
            if (giveStartReward) {
                PlayerManager.getInstance().getPlayer(playerID).deposit(Game.getStartPassReward());
                GUIManager.getInstance().getGUI(Game.getGameInstance(gameID).getGUIID()).showMessage(
                        Game.getGameInstance(gameID).getLanguageManager().getString("passed_start")
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
     * @param playerID          The UUID of the player to move.
     * @param playerPosition    The desired position.
     * @param buyForFree        Whether to provide the player with the deed for free, if the field is vacant.
     */
    public void setPlayerPosition(UUID playerID, int playerPosition, boolean buyForFree) {
        playerPositions.put(playerID, playerPosition);
        GameGUI gui = GUIManager.getInstance().getGUI(Game.getGameInstance(gameID).getGUIID());
        if (gui != null) {
            Field field = gui.movePlayerField(playerID,playerPositions.get(playerID)%gameBoard.getFieldAmount());
            if (buyForFree) {
                if (field instanceof PropertyField) {
                    ((PropertyField) field).doLandingAction(playerID, true);
                }
                else {
                    field.doLandingAction(playerID);
                }
            }
            else {
                field.doLandingAction(playerID);
            }
        }
    }
}
