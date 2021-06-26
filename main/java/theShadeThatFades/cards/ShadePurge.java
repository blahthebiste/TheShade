package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeCorruptionPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadePurge extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadePurge.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadePurge.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 1;
    private static final int HEALING = 6;
    private static final int UPGRADED_HEALING = 2;

    // /STAT DECLARATION/


    public ShadePurge() {
        super(ID,IMG,COST,TYPE,COLOR,RARITY,TARGET);
        baseMagicNumber = magicNumber = HEALING;
        this.tags.add(CardTags.HEALING);
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { // Heal the player and set Corruption to 0
        if (p.hasPower(ShadeCorruptionPower.POWER_ID)) {
            AbstractPower pwr = p.getPower(ShadeCorruptionPower.POWER_ID);
            int currCorruption = pwr.amount;
            pwr.reducePower(currCorruption);
        }
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription =  "Retain. NL " + DESCRIPTION;
            initializeDescription();
            this.selfRetain = true;
//            upgradeMagicNumber(UPGRADED_HEALING);
        }
    }
}
