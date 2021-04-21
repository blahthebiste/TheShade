package ShadeCardModifiers;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theShadeThatFades.actions.ShadeReplaceInGroupAction;

public class ShadeTransformEndOfTurnModifier extends AbstractCardModifier  {
    public static String MOD_ID = "ShadeTransformEndOfTurnModifierID";
    private static AbstractCard originalCard;

    public ShadeTransformEndOfTurnModifier(AbstractCard ogCard){
        originalCard = ogCard;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL (At the end of your turn, transforms back.)";
    }

    @Override
    public String identifier(AbstractCard card) {
        return MOD_ID;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        AbstractDungeon.actionManager.addToBottom(new ShadeReplaceInGroupAction(card, originalCard, group));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ShadeTransformEndOfTurnModifier(originalCard);
    }
}
