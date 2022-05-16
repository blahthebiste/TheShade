package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeRebuff extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeRebuff.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeRebuff.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 1;

    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLK = 2;

    private static final int CORRUPTION_LOSS = 4;
    private static final int UPGRADE_CORRUPTION_LOSS = 4;

    // /STAT DECLARATION/


    public ShadeRebuff() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = CORRUPTION_LOSS;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block for each enemy
        Iterator var4 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var4.hasNext()) {
            AbstractMonster m2 = (AbstractMonster)var4.next();
            if (!m2.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
            }
        }
        // Lose corruption
        if (p.hasPower("theShadeThatFades:Corruption")) {
            AbstractPower pwr = p.getPower("theShadeThatFades:Corruption");
            pwr.reducePower(magicNumber);
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLK);
            upgradeMagicNumber(UPGRADE_CORRUPTION_LOSS);
            initializeDescription();
        }
    }
}
