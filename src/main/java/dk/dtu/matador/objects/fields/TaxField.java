package dk.dtu.matador.objects.fields;

import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.objects.Player;

import java.awt.*;
import java.util.UUID;

public class TaxField extends Field {
    private String taxSubType;
    private double taxAmount;
    private double taxPercentage;

    /**
     * Creates a field, and sets up its respective field on the GUI.
     */
    public TaxField(Color color, Color textColor, String taxSubType, double taxAmount) {
        super(color, textColor, taxSubType, true);
        this.taxSubType = taxSubType;
        this.taxAmount = taxAmount;
    }

    /**
     * Creates a field, and sets up its respective field on the GUI.
     */
    public TaxField(Color color, Color textColor, String taxSubType, double taxAmount, double taxPercentage) {
        this(color, textColor, taxSubType, taxAmount);
        this.taxPercentage = taxPercentage;
    }

    @Override
    public void doLandingAction(UUID playerID) {
        GUIManager gui = GUIManager.getInstance();
        Player player = PlayerManager.getInstance().getPlayer(playerID);
        switch (taxSubType) {
            case "income-tax":
                boolean answer = gui.askTaxField(taxAmount, taxPercentage);

                if (answer) {
                    player.withdraw(taxAmount);
                } else {
                    player.withdraw(player.getNetWorth() * (taxPercentage/100));
                }
                break;
            case "extra-ordinary":
                gui.showMessage(LanguageManager.getInstance().getString("tax_extra_ordinary").replace("{price}", Double.toString(taxAmount)));
                player.withdraw(taxAmount);
                break;
        }

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
