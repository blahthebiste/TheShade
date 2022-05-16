package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeDeception extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeDeception.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeDeception.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 1;

    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLK = 4;

    // /STAT DECLARATION/


    public ShadeDeception() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bonusFromPlayer = 0;
        if (p != null && p.hasPower("theShadeThatFades:Corruption")) {
            bonusFromPlayer = p.getPower("theShadeThatFades:Corruption").amount;
        }
        this.addToBot(new GainBlockAction(p, p, this.block + bonusFromPlayer));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLK);
            initializeDescription();
        }
    }
}
