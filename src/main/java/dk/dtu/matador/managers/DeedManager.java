package dk.dtu.matador.managers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dk.dtu.matador.objects.Deed;
import dk.dtu.matador.objects.fields.PropertyField;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * The deed manager holds all the deeds, their ownerships and their groups.
 */
public class DeedManager {
    private static DeedManager deedManager;

    private final HashMap<UUID, Deed> deedMap = new HashMap<>();  // deedID, Deed
    private final BiMap<UUID, UUID> deeds = HashBiMap.create();  // deedID, fieldID
    private final HashMap<UUID, UUID> deedOwnership = new HashMap<>();  // deedID, playerID
    private final HashMap<Color, UUID[]> deedGroups = new HashMap<>();  // fieldColor, deedID[]

    private DeedManager() {}

    /**
     * Gets the current instance of DeedManager, or creates one if one doesn't exist already.
     *
     * @return The current DeedManager instance.
     */
    public static DeedManager getInstance() {
        if (deedManager == null) {
            deedManager = new DeedManager();
        }

        return deedManager;
    }

    /**
     * Creates a new deed for a given field.
     *
     * @param fieldID       The UUID of the field to create a deed for.
     * @param price         The price to buy the deed.
     * @param prawnPrice    The mortgage/prawn value of the deed.
     * @param rent          The rent options that one would have to pay if they land on the field.
     * @param housePrice    The price to build a new house.
     * @param hotelPrice    The price to build a hotel.
     * @return              The created Deed.
     */
    public Deed createDeed(UUID fieldID, double price, double prawnPrice, double[] rent, double housePrice, double hotelPrice) {
        Deed deed = new Deed();
        deed.setPrices(price, prawnPrice, rent, housePrice, hotelPrice);

        UUID deedID = deed.getID();
        deedMap.put(deedID, deed);
        deeds.put(deedID, fieldID);
        deedOwnership.put(deedID, null);

        return deed;
    }

    /**
     * Creates a new deed for a given field.
     *
     * @param fieldID       The UUID of the field to create a deed for.
     * @param price         The price to buy the deed.
     * @param prawnPrice    The mortgage/prawn value of the deed.
     * @param rent          The rent options that one would have to pay if they land on the field.
     * @return              The created Deed.
     */
    public Deed createDeed(UUID fieldID, double price, double prawnPrice, double[] rent) {
        Deed deed = new Deed();
        deed.setPrices(price, prawnPrice, rent);

        UUID deedID = deed.getID();
        deedMap.put(deedID, deed);
        deeds.put(deedID, fieldID);
        deedOwnership.put(deedID, null);

        return deed;
    }

    /**
     * Creates a new deed for a given field.
     *
     * @param fieldID    The UUID of the field to create a deed for.
     * @param price      The price to buy the deed.
     * @param prawnPrice The mortgage/prawn value of the deed.
     * @return           The created Deed.
     */
    public Deed createDeed(UUID fieldID, double price, double prawnPrice) {
        Deed deed = new Deed();
        deed.setPrices(price, prawnPrice);

        UUID deedID = deed.getID();
        deedMap.put(deedID, deed);
        deeds.put(deedID, fieldID);
        deedOwnership.put(deedID, null);

        return deed;
    }

    /**
     * Gets a deed with the given UUID.
     *
     * @param deedID    The UUID of the deed.
     * @return          The deed, if it's found.
     * @throws NullPointerException If the given deed is not found using the provided deedID.
     */
    public Deed getDeed(UUID deedID) {
        return deedMap.get(deedID);
    }

    public Deed[] getDeeds(UUID[] deedIDs) {
        ArrayList<Deed> deeds = new ArrayList<>();
        for (UUID deedID : deedIDs) {
            deeds.add(getDeed(deedID));
        }
        return deeds.toArray(new Deed[0]);
    }

    /**
     * Gets all deeds that a player owns.
     *
     * @param playerID  The UUID of the player.
     * @return  The UUID's of the given deeds.
     */
    public UUID[] getPlayerDeeds(UUID playerID) {
        ArrayList<UUID> playerDeeds = new ArrayList<>();
        for (UUID deedID : deedOwnership.keySet()) {
            if (deedOwnership.get(deedID) == playerID) {
                playerDeeds.add(deedID);
            }
        }
        return playerDeeds.toArray(new UUID[0]);
    }

    public void updatePlayerDeedPrices(UUID playerID) {
        for (UUID deedID : getPlayerDeeds(playerID)) {
            ((PropertyField) GameManager.getInstance().getGameBoard().getFieldFromID(getFieldID(deedID))).updatePrices(deedID);
        }
    }

    /**
     * Gets a deed using the fieldID of the deed.
     *
     * @param fieldID   The UUID of the field.
     * @return          The UUID of the deed.
     */
    public UUID getDeedID(UUID fieldID) {
        return deeds.inverse().get(fieldID);
    }

    /**
     * Gets a field using the deedID that matches that of the field.
     *
     * @param deedID    The UUID of the deed.
     * @return          The UUID of the field.
     */
    public UUID getFieldID(UUID deedID) {
        return deeds.get(deedID);
    }

    /**
     * Creates a deedGroup using a group color and a group of deed UUID's.
     *
     * @param groupColor The color of the fields.
     * @param deeds      The UUID's of the deeds in the group.
     */
    public void createDeedGroup(Color groupColor, UUID[] deeds) {
        deedGroups.put(groupColor, deeds);
    }

    /**
     * Gets the UUID's of the deeds that are in a deed group.
     *
     * @param groupColor    The color of the fields.
     * @return              The UUID's of the deeds.
     */
    public UUID[] getDeedGroupDeeds(Color groupColor) {
        return deedGroups.get(groupColor);
    }

    public boolean playerOwnsAllDeedsInDeedGroup(Color groupColor, UUID playerID) {
        UUID[] deedGroupIDs = getDeedGroupDeeds(groupColor);
        for (UUID deedID : deedGroupIDs) {
            if (getDeedOwnership(deedID) != playerID) {
                return false;
            }
        }
        return true;
    }

    public int howManyFieldTypeDoesPlayerOwn(String fieldClass, UUID playerID) {
        int i = 0;
        for (UUID uuid : getPlayerDeeds(playerID)) {
            UUID fieldID = getFieldID(uuid);
            try {
                if (Class.forName("dk.dtu.matador.objects.fields."+fieldClass).isInstance(GameManager.getInstance().getGameBoard().getFieldFromID(fieldID))) {
                    i++;
                }
            }
            catch (Exception e) {
                System.out.println("Could not find fieldName in Field Class names: " + e.toString());
            }
        }
        return i;
    }

    /**
     * Sets the ownership of a deed.
     *
     * @param deedID        The UUID of the deed.
     * @param playerID      The UUID of the player.
     */
    public void setDeedOwnership(UUID deedID, UUID playerID) {
        deedOwnership.put(deedID, playerID);
        PropertyField deedField = (PropertyField) GameManager.getInstance().getGameBoard().getFieldFromID(getFieldID(deedID));
        deedField.setPropertyOwner(playerID);
    }

    public UUID getDeedOwnership(UUID deedID) {
        return deedOwnership.get(deedID);
    }

    public void updateDeedPrices(UUID deedID, double price, double prawnPrice) {
        Deed deed = getDeed(deedID);
        deed.setPrices(price, prawnPrice);
        PropertyField deedField = (PropertyField) GameManager.getInstance().getGameBoard().getFieldFromID(getFieldID(deedID));
        deedField.updatePrices(deedID);
    }

    public void updateDeedPrices(UUID deedID, double price, double prawnPrice, double[] rent, double housePrice, double hotelPrice) {
        Deed deed = getDeed(deedID);
        deed.setPrices(price, prawnPrice, rent, housePrice, hotelPrice);
        PropertyField deedField = (PropertyField) GameManager.getInstance().getGameBoard().getFieldFromID(getFieldID(deedID));
        deedField.updatePrices(deedID);
    }
}
