package dk.dtu.matador.objects.fields;

import java.awt.*;
import java.util.UUID;

public class TaxField extends Field {
    /**
     * Creates a field, and sets up its respective field on the GUI.
     */
    public TaxField(String taxSubtype) {
        super(Color.PINK, taxSubtype, true);
    }

    @Override
    public void doLandingAction(UUID playerID) {

    }

    @Override
    public void doLeavingAction(UUID playerID) {

    }

    @Override
    public void reloadLanguage() {

    }
}
