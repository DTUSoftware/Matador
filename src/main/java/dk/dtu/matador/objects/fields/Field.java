package dk.dtu.matador.objects.fields;

import dk.dtu.matador.Game;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import gui_fields.GUI_Chance;
import gui_fields.GUI_Field;
import gui_fields.GUI_Start;
import gui_fields.GUI_Street;

import java.awt.*;
import java.util.UUID;

/**
 * The field class is an abstract class to provide a structure for creating more fields on the board.
 * <br><br>
 * Provides three abstract functions:<br>
 * - doLandingAction(), doLeavingAction() & reloadAction()
 */
public abstract class Field {
    private final UUID fieldID;
    private final String fieldName;
    private final Color fieldColor;
    private final Color textColor;
    private final GUI_Field guiField;

    /**
     * Creates a field, and sets up its respective field on the GUI.
     *
     * @param fieldColor    The color of the field.
     * @param textColor     The color of the text.
     * @param fieldName     The name of the field (this is the programmatically correct name,
     *                      not user understandable name).
     * @param description   Whether to show the fields' description.
     */
    Field(Color fieldColor, Color textColor, String fieldName, boolean description) {
        fieldID = UUID.randomUUID();
        this.fieldName = fieldName;
        this.fieldColor = fieldColor;
        this.textColor = textColor;

        switch (fieldName) {
            case "chance":
                this.guiField = new GUI_Chance();
                break;
            case "start":
                this.guiField = new GUI_Start();
                break;
            case "jail":
                this.guiField = new GUIJailField("GUI_Field.Image.Jail", "", LanguageManager.getInstance().getString("field_"+fieldName+"_name"), "", fieldColor, Color.BLACK);
                break;
            case "go_to_jail":
                this.guiField = new GUIJailField("GUI_Field.Image.GoToJail", LanguageManager.getInstance().getString("field_"+fieldName+"_name"), "", "", fieldColor, Color.BLACK);
                break;
            default:
                this.guiField = new GUI_Street();
                break;
        }
        this.guiField.setBackGroundColor(fieldColor);
        if (this.textColor != null) {
            this.guiField.setForeGroundColor(this.textColor);
        }
        this.guiField.setTitle(LanguageManager.getInstance().getString("field_"+fieldName+"_name"));
        this.guiField.setSubText("");
        if (description) {
            this.guiField.setDescription(LanguageManager.getInstance().getString("field_"+fieldName+"_description")
                    .replace("{start_pass_amount}", Double.toString(Game.getStartPassReward())));
        }
    }

    /**
     * An action that is performed whenever the player lands on the field.
     * @param playerID  The UUID of the player.
     */
    public abstract void doLandingAction(UUID playerID);

    /**
     * An action that is performed whenever the player is about to leave the field.
     * @param playerID  The UUID of the player.
     */
    public abstract void doLeavingAction(UUID playerID);

    /**
     * Reloads the language on the field with the current language chosen in the LanguageManager.
     */
    public abstract void reloadLanguage();

    public UUID getID() {
        return fieldID;
    }

    public GUI_Field getGUIField() {
        return this.guiField;
    }

    public Color getFieldColor() {
        return fieldColor;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "["+getID()+"] - " +
                "(" + String.format("%02d", GameManager.getInstance().getGameBoard().getFieldPosition(getID())) + ") " +
                getFieldName() +
                " [" + "Color: " + getFieldColor().toString().replace("java.awt.Color", "") +
                "]";
    }
}
