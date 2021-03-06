package dk.dtu.matador.objects.chancecards.move;

import dk.dtu.matador.managers.GUIManager;
import dk.dtu.matador.managers.GameManager;
import dk.dtu.matador.managers.LanguageManager;
import dk.dtu.matador.managers.PlayerManager;
import dk.dtu.matador.objects.chancecards.ChanceCard;

import java.util.UUID;

public abstract class MoveToFieldCC extends ChanceCard {
    private boolean giveStartReward;
    private String fieldName;

    MoveToFieldCC(String cardName, String fieldName, boolean giveStartReward){
        super(cardName);
        this.fieldName = fieldName;
        this.giveStartReward = giveStartReward;
    }

    MoveToFieldCC(String cardName, boolean giveStartReward){
        super(cardName);
        this.fieldName = cardName;
        this.giveStartReward = giveStartReward;
    }

    @Override
    public void doCardAction(UUID playerID) {
        UUID fieldID = GameManager.getInstance().getGameBoard().getFieldIDFromType(fieldName);
        if (fieldID == null) {
            GUIManager.getInstance().showMessage(LanguageManager.getInstance().getString("error_string"));
            System.out.println("Field ID is null!");
            return;
        }

        GameManager.getInstance().setPlayerBoardPosition(
                playerID,
                GameManager.getInstance().getGameBoard().getFieldPosition(fieldID),
                giveStartReward
        );
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isGivenStartReward() {
        return giveStartReward;
    }
}

/**
 * Ryk frem til START (antal 2)
 * Ryk tre felter frem.
 * Ryk tre felter tilbage (Antal kort: 2)
 * Ryk frem til Frederiksberg Allé. Hvis De passere START, indkasser da 4000 kr.
 * Ryk frem til det nærmeste rederi og betal ejeren to gange den leje han ellers er berettiget
 * til, hvis selskabet ikke ejes af nogen kan De købe det af banken.(Antal 2)
 * Tag med Mols-Linien, flyt brikken frem og hvis De passerer START indkassér da kr
 * 4000.
 * Ryk frem til Grønningen, hvis de passerer start indkasser da kr 4000
 * Ryk frem til Vimmelskaftet, hvis de passerer start indkasser da kr 4000
 * Tag med den nærmeste færge, hvis de passerer start indkasser da kr 4000
 * Ryk frem til Strandvejen. Hvis De passere START, indkasser da 4000 kr.
 * Tag til Rådhuspladsen
 * Gå i fængsel, De indkasserer ikke 4000 kr for at passere start. (antal kort: 2)
 */