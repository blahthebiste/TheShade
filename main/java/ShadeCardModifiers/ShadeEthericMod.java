package ShadeCardModifiers;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ShadeEthericMod extends AbstractCardModifier  {
    public static String MOD_ID = "ShadeEthericModID";

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL Ethereal.";
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.isEthereal = true;
    }

//    @Override
//    public boolean removeAtEndOfTurn(AbstractCard card) {
//        System.out.println("Removing Etheric modifier from card.");
//        return true; // Etheric Shift always lasts only until the end of the turn
//    }
    @Override
    public String identifier(AbstractCard card) {
        return MOD_ID;
    }

//    @Override
//    public void atEndOfTurn(AbstractCard card, CardGroup group) {
//        CardModifierManager.removeSpecificModifier(card, ShadeEthericMod, );
//    }

    @Override
    public void onRemove(AbstractCard card) {
        card.isEthereal = false;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ShadeEthericMod();
    }
}
