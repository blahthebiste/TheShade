package theShadeThatFades.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.actions.ShadeBlackHeartAction;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeBlackHeart extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeBlackHeart.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeBlackHeart.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = -1;

    private static final int CORRUPTION_MULTI = 4;
    private static final int UPGRADE_CORRUPTION_MULTI = 1;

    // /STAT DECLARATION/


    public ShadeBlackHeart() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = CORRUPTION_MULTI;
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ShadeBlackHeartAction(p, this.freeToPlayOnce, this.energyOnUse, this.magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CORRUPTION_MULTI);
            initializeDescription();
        }
    }
}
