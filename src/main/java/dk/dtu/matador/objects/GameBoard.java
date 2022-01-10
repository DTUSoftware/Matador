package dk.dtu.matador.objects;

import com.google.common.io.Resources;
import dk.dtu.matador.Game;
import dk.dtu.matador.managers.DeedManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.objects.chancecards.*;
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
    private final ChanceCard[] chanceCards = new ChanceCard[] {
    /* Bail */          new BailCC(),
    /* Give & Take */   new BirthdayCC(), new DidHomeWorkCC(), new EatCandyCC(),
//    /* Move to field */ new BoardWalkCC(), new SkateparkCC(), new StartCC(),
//    /* Move to color */ new BrownRedCC(), new LightBlueCC(), new LightblueYellowCC(), new OrangeBlueCC(), new OrangeCC(), new RedCC(), new SalmonGreenCC(),
//    /* Move to free */  // new CarCC(), new ShipCC(),
    /* Special */       new MoveFieldsCC(), new MoveOrDrawCC(),
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
        JSONObject subtypeConfiguration = gameBoardJSON.getJSONObject("subtype_configuration");
        fields = new Field[jsonFields.length()];
        HashMap<Color, ArrayList<UUID>> fieldGroupsMap = new HashMap<>();

        // Load fields from Game Board config
        for (int i = 0; i < jsonFields.length(); i++) {
            JSONObject jsonField = jsonFields.getJSONObject(i);
            String fieldType = jsonField.getString("field_type");
            String fieldColorString = jsonField.getString("field_color");
            Color fieldColor = getColor(fieldColorString);

            Color textColor = null;
            if (jsonField.has("field_color_text")) {
                textColor = getColor(jsonField.getString("field_color_text"));
            }

            switch (fieldType) {
                case "StartField":
                    fields[i] = new StartField(fieldColor, textColor);
                    Game.setStartPassReward(jsonField.getDouble("pass_reward"));
                    fields[i].reloadLanguage();
                    break;
                case "PropertyField":
                    String fieldName = jsonField.getString("field_name");
                    JSONObject prices = jsonField.getJSONObject("prices");

                    double price = prices.getDouble("deed");
                    double prawnPrice = prices.getDouble("prawn");

                    Deed fieldDeed = null;

                    String fieldSubtype = jsonField.getString("field_subtype");
                    double[] rent;
                    switch (fieldSubtype.toLowerCase()) {
                        case "street":
                            fields[i] = new StreetField(fieldSubtype, fieldColor, textColor, fieldName);
                            double housePrice = prices.getDouble("house");
                            double hotelPrice = prices.getDouble("hotel");
                            JSONArray rentJSON = (JSONArray) prices.get("rent");
                            rent = new double[rentJSON.length()];
                            for (int j = 0; j < rentJSON.length(); j++) {
                                rent[j] = rentJSON.getDouble(j);
                            }
                            fieldDeed = DeedManager.getInstance().createDeed(fields[i].getID(), price, prawnPrice, rent, housePrice, hotelPrice);

                            // Add the group color
                            if (!fieldGroupsMap.containsKey(fieldColor)) {
                                fieldGroupsMap.put(fieldColor, new ArrayList<>());
                            }

                            // Add the field deed to the group array
                            fieldGroupsMap.get(fieldColor).add(fieldDeed.getID());
                            break;
                        case "brewery":
                            JSONObject breweryConfiguration = subtypeConfiguration.getJSONObject("brewery");
                            fields[i] = new BreweryField(fieldSubtype, fieldColor, textColor, fieldName);
                            fieldDeed = DeedManager.getInstance().createDeed(fields[i].getID(), price, prawnPrice, new double[] {breweryConfiguration.getDouble("rent_multiplier"), breweryConfiguration.getDouble("monopoly_multiplier")});
                            break;
                        case "ferry":
                            JSONObject ferryConfiguration = subtypeConfiguration.getJSONObject("ferry");
                            JSONArray ferryJSONRent = ferryConfiguration.getJSONArray("rent");
                            rent = new double[ferryJSONRent.length()];
                            for (int j = 0; j < ferryJSONRent.length(); j++) {
                                rent[j] = ferryJSONRent.getDouble(j);
                            }
                            fields[i] = new FerryField(fieldSubtype, fieldColor, textColor, fieldName);
                            fieldDeed = DeedManager.getInstance().createDeed(fields[i].getID(), price, prawnPrice, rent);
                            break;
                        default:
                            Game.logDebug("PropertyField has no subtype!");
                            break;
                    }

                    if (fieldDeed == null) {
                        break;
                    }

                    // Update prices
                    ((PropertyField) fields[i]).updatePrices(fieldDeed.getID());
                    break;
                case "ChanceField":
                    fields[i] = new ChanceField(fieldColor, textColor);
                    break;
                case "JailField":
                    fields[i] = new JailField(fieldColor, textColor);
                    break;
                case "BreakField":
                    fields[i] = new BreakField(fieldColor, textColor);
                    break;
                case "GoToJailField":
                    fields[i] = new GoToJailField(fieldColor, textColor);
                    break;
                case "TaxField":
                    switch (jsonField.getString("field_subtype")) {
                        case "income-tax":
                            fields[i] = new TaxField(fieldColor, textColor, jsonField.getString("field_subtype"), jsonField.getDouble("tax_amount"), jsonField.getDouble("tax_percentage"));
                            break;
                        case "extra-ordinary":
                            fields[i] = new TaxField(fieldColor, textColor, jsonField.getString("field_subtype"), jsonField.getDouble("tax_amount"));
                            break;
                    }
                    break;
                default:
                    System.out.println("Ohno, field type doesn't exist...");
                    fields[i] = new ChanceField(fieldColor, textColor);
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
//                System.out.println("FieldColor: " + field.getFieldColor());
//                System.out.println("CheckColor: " + color);
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
                if (Class.forName("dk.dtu.matador.objects.fields."+fieldName).isInstance(fieldMap.get(uuid))) {
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
