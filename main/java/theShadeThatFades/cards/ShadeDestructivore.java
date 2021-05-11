package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeStealStrengthEndOfTurnPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeDestructivore extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeDestructivore.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeDestructivore.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_GRAY;

    private static final int COST = 0;

    // /STAT DECLARATION/


    public ShadeDestructivore() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int strengthAmount = 2;
        if (m != null) {
            if (m.hasPower(StrengthPower.POWER_ID)) {
                strengthAmount = Math.max(m.getPower(StrengthPower.POWER_ID).amount, 2);
            }

            // Monster loses strength
            this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -strengthAmount), -strengthAmount));
            if (!m.hasPower(ArtifactPower.POWER_ID)) {
                // Artifact should block the rest of the effects as well
                this.addToBot(new ApplyPowerAction(m, p, new ShadeStealStrengthEndOfTurnPower(m, strengthAmount), strengthAmount));

                // Player gets strength
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, strengthAmount), strengthAmount));
            }
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription =  "Retain. NL " + DESCRIPTION;
            initializeDescription();
            this.selfRetain = true;
        }
    }
}
