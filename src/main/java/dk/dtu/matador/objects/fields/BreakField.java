package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.LanguageManager;

import java.awt.*;
import java.util.UUID;

public class BreakField extends Field {
    public BreakField(Color color, Color textColor) {
        super(color, textColor, "break", true);
    }

    @Override
    public void doLandingAction(UUID playerID) {

    }

    @Override
    public void doLeavingAction(UUID playerID) {

    }

    @Override
    public void reloadLanguage() {
        super.getGUIField().setTitle(LanguageManager.getInstance().getString("field_"+super.getFieldName()+"_name"));
        super.getGUIField().setDescription(LanguageManager.getInstance().getString("field_"+super.getFieldName()+"_description"));
    }
}
