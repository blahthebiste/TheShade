package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeArcaneBarrier extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeArcaneBarrier.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeArcaneBarrier.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_GRAY;

    private static final int COST = 1;

    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLK = 2;
    private static final int WEAK_AMOUNT = 1;
    private static final int UPGRADE_WEAK_AMOUNT = 1;
    // /STAT DECLARATION/


    public ShadeArcaneBarrier() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = WEAK_AMOUNT;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
        this.addToBot(new GainBlockAction(p, p, this.block));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLK);
            upgradeMagicNumber(UPGRADE_WEAK_AMOUNT);
            initializeDescription();
        }
    }
}
