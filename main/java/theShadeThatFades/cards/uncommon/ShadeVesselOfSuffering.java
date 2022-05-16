package theShadeThatFades.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeVesselOfSufferingPower;
import theShadeThatFades.powers.ShadeVesselOfSufferingUpgradedPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeVesselOfSuffering extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeVesselOfSuffering.class.getSimpleName());
    public static final String IMG = makeCardPath("ShadeVesselOfSuffering.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 1;
    // /STAT DECLARATION/

    public ShadeVesselOfSuffering() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadeVesselOfSufferingUpgradedPower(p, 1)));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadeVesselOfSufferingPower(p, 1)));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

}