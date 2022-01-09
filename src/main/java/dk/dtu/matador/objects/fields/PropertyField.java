package dk.dtu.matador.objects.fields;

import dk.dtu.matador.Game;
import dk.dtu.matador.managers.*;
import dk.dtu.matador.objects.Deed;
import dk.dtu.matador.objects.Player;
import gui_fields.GUI_Ownable;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public abstract class PropertyField extends Field {
    public PropertyField(String subType, Color color, Color textColor, String fieldName) {
        super(subType, color, textColor, fieldName, false);
//        ((GUI_Ownable) super.getGUIField()).setBorder(Color.BLACK);
    }

    private void checkDeedOwnership(UUID playerID, UUID deedID, UUID deedOwnership, String propertyName) {
        if (deedOwnership.equals(playerID)) {
            // same player owns it
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("landed_on_own_property"));
        } else {
            if (DeedManager.getInstance().getDeed(deedID).payRent(playerID)) {
                GUIManager.getInstance().showMessage(
                        LanguageManager.getInstance().getString("paid_rent")
                                .replace("{property_rent}", Float.toString(Math.round(DeedManager.getInstance().getDeed(deedID).getCurrentRent())))
                                .replace("{property_owner}", PlayerManager.getInstance().getPlayer(deedOwnership).getName())
                                .replace("{property_name}", propertyName)
                );
            } else {
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("could_not_pay_rent").replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName()));
                GameManager.getInstance().finishGame();
            }
        }
    }

    public void doLandingAction(UUID playerID, boolean buyForFree) {
        if (buyForFree) {
            UUID deedID = DeedManager.getInstance().getDeedID(super.getID());
            UUID deedOwnership = DeedManager.getInstance().getDeedOwnership(deedID);
            String propertyName = LanguageManager.getInstance().getString("field_" + super.getFieldName() + "_name");
            if (deedOwnership == null) {
                // give the property
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("free_property"));
                DeedManager.getInstance().setDeedOwnership(DeedManager.getInstance().getDeedID(super.getID()), playerID);
                DeedManager.getInstance().updatePlayerDeedPrices(playerID);
            } else {
                // someone owns it
                checkDeedOwnership(playerID, deedID, deedOwnership, propertyName);
            }
        } else {
            doLandingAction(playerID);
        }
    }

    private class Bid {
        private final double bid;
        private final UUID bidder;

        Bid(double bid, UUID bidder) {
            this.bid = bid;
            this.bidder = bidder;
        }

        public double getBid() {
            return bid;
        }

        public UUID getBidder() {
            return bidder;
        }
    }

    private Bid biddingRound(Bid bid, String propertyName) {
        UUID[] auctionList = PlayerManager.getInstance().getPlayerIDs();
        for (UUID playerID : auctionList) {
            if (bid.getBidder() != null) {
                Game.logDebug(playerID.toString());
                Game.logDebug(bid.getBidder().toString());
                if (playerID == bid.getBidder()) {
                    continue;
                }
            }

            String playerName = PlayerManager.getInstance().getPlayer(playerID).getName();
            double[] biddingoptions = {50.0 + bid.getBid(), 100.0 + bid.getBid(), 500.0 + bid.getBid(), 1000.0 + bid.getBid(), 2000.0 + bid.getBid(), 5000.0 + bid.getBid()};
            double playerBalance = PlayerManager.getInstance().getPlayer(playerID).getBalance();
            if (playerBalance >= biddingoptions[0]) {
                boolean want_to_bid = GUIManager.getInstance().askPrompt(
                        LanguageManager.getInstance().getString("want_to_bid_on_action")
                                .replace("{property_name}", propertyName)
                                .replace("{player_name}", playerName));
                if (want_to_bid) {
                    for (int j = 0; j < biddingoptions.length; j++) {
                        if (playerBalance < biddingoptions[j]) {
                            biddingoptions = ArrayUtils.remove(biddingoptions, j);
                        }
                    }

                    bid = biddingRound(new Bid(GUIManager.getInstance().askBid(biddingoptions), playerID), propertyName);
                    break;
                }
            }
        }
        return bid;
    }

    /**
     * Method that starts an auction on properties when needed
     */
    public void startAuction() {
        String propertyName = LanguageManager.getInstance().getString("field_" + super.getFieldName() + "_name");
        GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("auction_start")
                .replace("{property_name}", propertyName));

        Bid bid = new Bid(0.0, null);

        bid = biddingRound(bid, propertyName);

        if (bid.getBidder() != null) {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("auction_won")
                    .replace("{player_name}", PlayerManager.getInstance().getPlayer(bid.getBidder()).getName())
                    .replace("{property_name}", propertyName));
            PlayerManager.getInstance().getPlayer(bid.getBidder()).withdraw(bid.getBid());
            DeedManager.getInstance().setDeedOwnership(DeedManager.getInstance().getDeedID(super.getID()), bid.getBidder());
            DeedManager.getInstance().updatePlayerDeedPrices(bid.getBidder());
        } else {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("auction_no_bids"));
        }
    }

    @Override
    public void doLandingAction(UUID playerID) {
        UUID deedID = DeedManager.getInstance().getDeedID(super.getID());
        UUID deedOwnership = DeedManager.getInstance().getDeedOwnership(deedID);
        String propertyName = LanguageManager.getInstance().getString("field_" + super.getFieldName() + "_name");
        if (deedOwnership == null) {
            // want to buy and/or have enough money
            double deed_price = DeedManager.getInstance().getDeed(deedID).getPrice();
            if (PlayerManager.getInstance().getPlayer(playerID).getBalance() >= deed_price) {
                boolean want_to_buy = GUIManager.getInstance().askPrompt(
                        LanguageManager.getInstance().getString("want_to_buy")
                                .replace("{property_name}", propertyName)
                                .replace("{property_price}", Float.toString(Math.round(deed_price)))
                );
                if (want_to_buy) {
                    PlayerManager.getInstance().getPlayer(playerID).withdraw(deed_price);
                    DeedManager.getInstance().setDeedOwnership(DeedManager.getInstance().getDeedID(super.getID()), playerID);
                    DeedManager.getInstance().updatePlayerDeedPrices(playerID);
                } else {
                    startAuction();
                }
            } else {
                // End the game
                GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("could_not_buy").replace("{player_name}", PlayerManager.getInstance().getPlayer(playerID).getName()));
                GameManager.getInstance().finishGame();
            }
        } else {
            // someone owns it
            checkDeedOwnership(playerID, deedID, deedOwnership, propertyName);
        }
    }

    @Override
    public void doLeavingAction(UUID playerID) {

    }

    @Override
    public void reloadLanguage() {
        super.getGUIField().setTitle(LanguageManager.getInstance().getString("field_" + super.getFieldName() + "_name"));
        ((GUI_Ownable) super.getGUIField()).setRentLabel(LanguageManager.getInstance().getString("rent") + ": ");
        ((GUI_Ownable) super.getGUIField()).setOwnableLabel(LanguageManager.getInstance().getString("owner") + ": ");
    }

    public void updatePrices(UUID deedID) {
        Deed fieldDeed = DeedManager.getInstance().getDeed(deedID);
        ((GUI_Ownable) super.getGUIField()).setRent(Double.toString(fieldDeed.getCurrentRent()));
        super.getGUIField().setSubText(Double.toString(fieldDeed.getPrice()));
        super.getGUIField().setDescription(LanguageManager.getInstance().getString("price") + ": " + Double.toString(fieldDeed.getPrice()) + " | " + LanguageManager.getInstance().getString("rent") + ": " + Arrays.toString(fieldDeed.getRent()));
    }

    public void setPropertyOwner(UUID playerID) {
        ((GUI_Ownable) super.getGUIField()).setOwnerName(PlayerManager.getInstance().getPlayer(playerID).getName());
    }

    @Override
    public String toString() {
        Deed fieldDeed = DeedManager.getInstance().getDeed(DeedManager.getInstance().getDeedID(getID()));
        return "[" + getID() + "] - " +
                "(" + String.format("%02d", GameManager.getInstance().getGameBoard().getFieldPosition(getID())) + ") " +
                getFieldName() +
                " [" + "Color: " + getFieldColor().toString().replace("java.awt.Color", "") +
                ", " + fieldDeed.toString() +
                "]";
    }
}
