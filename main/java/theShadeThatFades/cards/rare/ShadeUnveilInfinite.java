package theShadeThatFades.cards.rare;

import ShadeCardModifiers.ShadeTransformEndOfTurnModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.actions.TransformIntoBlackCardAction;
import theShadeThatFades.actions.TransformIntoCurseAction;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeUnveilInfinite extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeUnveilInfinite.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeUnveil.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_GRAY;

    private static final int COST = 1;

    // /STAT DECLARATION/


    public ShadeUnveilInfinite() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Pick card!
        this.addToBot(new TransformIntoBlackCardAction(1, false, false, false, true, true));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            returnToHand = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
