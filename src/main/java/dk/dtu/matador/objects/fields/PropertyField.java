package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.*;
import dk.dtu.matador.objects.Deed;
import gui_fields.GUI_Ownable;

import java.awt.*;
import java.util.Arrays;
import java.util.UUID;

public abstract class PropertyField extends Field {
    public PropertyField(Color color, String fieldName) {
        super(color, fieldName, false);
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
        }
        else {
            doLandingAction(playerID);
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
        ((GUI_Ownable) super.getGUIField()).setRentLabel(LanguageManager.getInstance().getString("rent")+": ");
        ((GUI_Ownable) super.getGUIField()).setOwnableLabel(LanguageManager.getInstance().getString("owner")+": ");
    }

    public void updatePrices(UUID deedID) {
        Deed fieldDeed = DeedManager.getInstance().getDeed(deedID);
        ((GUI_Ownable) super.getGUIField()).setRent(Double.toString(fieldDeed.getCurrentRent()));
        super.getGUIField().setSubText(Double.toString(fieldDeed.getPrice()));
        super.getGUIField().setDescription(LanguageManager.getInstance().getString("price")+": " + Double.toString(fieldDeed.getPrice()) + " | "+LanguageManager.getInstance().getString("rent")+": " + Arrays.toString(fieldDeed.getRent()));
    }

    public void setPropertyOwner(UUID playerID) {
        ((GUI_Ownable) super.getGUIField()).setOwnerName(PlayerManager.getInstance().getPlayer(playerID).getName());
    }

    @Override
    public String toString() {
        Deed fieldDeed = DeedManager.getInstance().getDeed(DeedManager.getInstance().getDeedID(getID()));
        return "["+getID()+"] - " +
                "(" + String.format("%02d", GameManager.getInstance().getGameBoard().getFieldPosition(getID())) + ") " +
                getFieldName() +
                " [" + "Color: " + getFieldColor().toString().replace("java.awt.Color", "") +
                ", " + fieldDeed.toString() +
                "]";
    }
}
