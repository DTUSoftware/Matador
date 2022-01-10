package dk.dtu.matador.objects;

import com.google.common.io.Resources;
import dk.dtu.matador.Game;
import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.objects.chancecards.*;
import dk.dtu.matador.objects.chancecards.receive.*;
import dk.dtu.matador.objects.fields.*;
import gui_fields.GUI_Field;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The GameBoard keeps track of the fields, their positions on the board, as well as the chancecards on the board.
 */
public class GameBoard {
    private final Field[] fields;
    private final HashMap<UUID, Field> fieldMap = new HashMap<>();
    private final HashMap<UUID, Integer> fieldPositions = new HashMap<>();
    private final HashMap<UUID, GUI_Field> guiFields = new LinkedHashMap<>();

    //TODO Remove old ChanceCards
    private final ChanceCard[] chanceCards = new ChanceCard[] {
    /* Bail */          new BailCC(),
    /* Give & Take */   new BirthdayCC(), new DidHomeWorkCC(), new EatCandyCC(),
    /* Move to field */ new BoardWalkCC(), new SkateparkCC(), new StartCC(),
    /* Move to color */ new BrownRedCC(), new LightBlueCC(), new LightblueYellowCC(), new OrangeBlueCC(), new OrangeCC(), new RedCC(), new SalmonGreenCC(),
    /* Move to free */  // new CarCC(), new ShipCC(),
    /* Special */       new MoveFieldsCC(), new MoveOrDrawCC(),
            /* receive cards */ new Aktie(), new Birthday(), new EllevenRight(), new FamilyParty(), new Garden(), new JointParty(), new MatadorLegatet(),
            /* receive cards */ new OldFurniture(), new PremiumBond(), new Raise(), new TheClassLottery(), new TheLocalAuthority()
    };

    private JSONObject gameBoardJSON;
    private final Random rand = new Random();

    /**
     * Loads the board with the fields from the JSON-configuration file that is in the resources folder of the project.
     */
    private void loadGameBoardConfig() {
        try {
//            URL gameBoardConfigURL = Game.class.getClassLoader().getResource("GameBoard.json");
            URL gameBoardConfigURL = Resources.getResource("GameBoard.json");
            String gameBoardConfigString = Resources.toString(gameBoardConfigURL, StandardCharsets.UTF_8);

            gameBoardJSON = new JSONObject(gameBoardConfigString);
        }
        catch (Exception e) {
            System.out.println("Could not read Game Board JSON from resources... - " + e.toString());
        }
    }

    private Color getColor(String colorString) {
        Color color = Color.BLACK;
        if (colorString.startsWith("#")) {
            color = Color.decode(colorString);
        }
        else {
            try {
                color = (Color) Color.class.getField(colorString).get(null);
            }
            catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return color;
    }

    /**
     * Creates a new gameboard with fields, which is mainly just loading and parsing the fields from the configuration
     * file into proper game field objects, based on their types and given information.
     */
    public GameBoard() {
        loadGameBoardConfig();

        JSONArray jsonFields = gameBoardJSON.getJSONArray("fields");
        fields = new Field[jsonFields.length()];
        JSONObject jsonFieldGroups = gameBoardJSON.getJSONObject("property_field_groups");
        HashMap<Color, ArrayList<UUID>> fieldGroupsMap = new HashMap<>();
        // populate map
        for (String colorString : jsonFieldGroups.keySet()) {
            fieldGroupsMap.put(getColor(colorString), new ArrayList<>());
        }

        // Load fields from Game Board config
        for (int i = 0; i < jsonFields.length(); i++) {
            JSONObject jsonField = jsonFields.getJSONObject(i);
            String fieldType = jsonField.getString("field_type");

            switch (fieldType) {
                case "StartField":
                    fields[i] = new StartField();
                    Game.setStartPassReward(jsonField.getDouble("pass_reward"));
                    break;
                case "PropertyField":
                    String fieldColorString = jsonField.getString("field_color");
                    Color fieldColor = getColor(fieldColorString);
                    fields[i] = new PropertyField(fieldColor, jsonField.getString("field_name"));

                    // Register the rent
                    JSONObject groupRentJSON = jsonFieldGroups.getJSONObject(fieldColorString);
                    double price = groupRentJSON.getDouble("base_price");
                    double rent = groupRentJSON.getDouble("base_rent");
                    double groupRent = groupRentJSON.getDouble("group_rent");
                    Deed fieldDeed = DeedManager.getInstance().createDeed(fields[i].getID(), price, rent, groupRent);
                    ((PropertyField) fields[i]).updatePrices(fieldDeed.getID());

                    // Add the field deed to the group array
                    fieldGroupsMap.get(fieldColor).add(fieldDeed.getID());
                    break;
                case "ChanceField":
                    fields[i] = new ChanceField();
                    break;
                case "JailField":
                    fields[i] = new JailField();
                    break;
                case "BreakField":
                    fields[i] = new BreakField();
                    break;
                case "GoToJailField":
                    fields[i] = new GoToJailField();
                    break;
                default:
                    System.out.println("Ohno, field type doesn't exist...");
                    fields[i] = new ChanceField();
            }

            Field field = fields[i];
            fieldMap.put(field.getID(), fields[i]);
            guiFields.put(field.getID(), field.getGUIField());
            fieldPositions.put(field.getID(), i);
        }

        // Update the deedmanager with group deeds
        for (Color groupColor : fieldGroupsMap.keySet()) {
            DeedManager.getInstance().createDeedGroup(groupColor, fieldGroupsMap.get(groupColor).toArray(new UUID[0]));
        }
    }

    /**
     * Reloads the language on all fields.
     */
    public void reloadLanguage() {
        for (Field field : fields) {
            field.reloadLanguage();
        }
    }

    /**
     * Gets the amount of fields on the board.
     *
     * @return  The amount of fields.
     */
    public int getFieldAmount() {
        return fields.length;
    }

    /**
     * Gets a field based on its position on the board.
     * @param fieldPosition The position of the field on the board.
     * @return              The field.
     */
    public Field getField(int fieldPosition) {
        return fields[fieldPosition];
    }

    /**
     * Returns a string with all the fields and their information, including their UUID and their deed information.
     * @return  A string with the fields on the board.
     */
    public String fieldsToString() {
        StringBuilder fieldsString = new StringBuilder();

        for (Field field : fields) {
            fieldsString.append("\n    ").append(field.toString());
        }

        return fieldsString.toString();
    }

    public Field getFieldFromID(UUID fieldID) {
        return fieldMap.get(fieldID);
    }

    public GUI_Field[] getGUIFields() {
        return guiFields.values().toArray(new GUI_Field[0]);
    }

    public int getFieldPosition(UUID fieldID) {
        return fieldPositions.get(fieldID);
    }

    /**
     * Gets a random chance card.
     *
     * @return  The randomly picked chance card.
     */
    public ChanceCard getChanceCard() {
        return chanceCards[rand.nextInt(chanceCards.length)];
    }

    /**
     * Gets the next field with one of the given colors, from the position of given player.
     *
     * @param playerID  Player to find next field from.
     * @param colors    Colors to check for.
     * @return          The UUID of the found field.
     */
    public UUID getNextFieldIDWithColor(UUID playerID, Color[] colors) {
        int playerPosition = GameManager.getInstance().getPlayerPosition(playerID);
        Field foundField = null;
        for (int currentField = playerPosition+1; currentField < playerPosition+getFieldAmount(); currentField++) {
            Field field = getField(currentField % getFieldAmount());
            for (Color color : colors) {
                System.out.println("FieldColor: " + field.getFieldColor());
                System.out.println("CheckColor: " + color);
                if (field.getFieldColor().equals(color)) {
                    foundField = field;
                    break;
                }
            }
            if (foundField != null) { break; }
        }
        if (foundField != null) {
            return foundField.getID();
        }
        return null;
    }

    /**
     * Returns the UUID of a field that matches the given fieldName.
     *
     * @param fieldName A field type or name of the field (for example BreakField, JailField, StartField,
     *                  jail, swimming_pool, bowling_alley, etc.)
     * @return          The UUID of the field matching the criteria, if no field is found it returns null.
     */
    public UUID getFieldIDFromType(String fieldName) {
        for (UUID uuid : fieldMap.keySet()) {
            try {
                if (Class.forName("dk.dtu.cdio3.objects.fields."+fieldName).isInstance(fieldMap.get(uuid))) {
                    return uuid;
                }
            }
            catch (Exception e) {
                System.out.println("Could not find fieldName in Field Class names: " + e.toString());
            }
            if (fieldMap.get(uuid).getFieldName().equals(fieldName)) {
                return uuid;
            }
        }
        return null;
    }
}
