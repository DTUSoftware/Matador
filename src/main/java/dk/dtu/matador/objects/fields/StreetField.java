package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.DeedManager;
import gui_fields.GUI_Street;

import java.awt.*;

public class StreetField extends PropertyField {

    public StreetField(String subType, Color color, Color textColor, String fieldName) {
        super(subType, color, textColor, fieldName);
    }

    public void buildHouse() {
        ((GUI_Street) super.getGUIField()).setHouses(DeedManager.getInstance().getDeed(DeedManager.getInstance().getDeedID(super.getID())).getHouses());
    }

    public void buildHotel() {
        ((GUI_Street) super.getGUIField()).setHotel(true);
    }

    public void demolishHouse() {
        ((GUI_Street) super.getGUIField()).setHouses(DeedManager.getInstance().getDeed(DeedManager.getInstance().getDeedID(super.getID())).getHouses());
    }

    public void demolishHotel() {
        ((GUI_Street) super.getGUIField()).setHotel(false);
    }
}
