package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.DeedManager;
import gui_fields.GUI_Street;

import java.awt.*;

public class StreetField extends PropertyField {

    public StreetField(Color color, Color textColor, String fieldName) {
        super(color, textColor, fieldName);
    }

    public void buildHouse() {
        ((GUI_Street) super.getGUIField()).setHouses(DeedManager.getInstance().getDeed(DeedManager.getInstance().getDeedID(super.getID())).getHouses());
    }

    public void buildHotel() {
        ((GUI_Street) super.getGUIField()).setHotel(true);
    }
}
