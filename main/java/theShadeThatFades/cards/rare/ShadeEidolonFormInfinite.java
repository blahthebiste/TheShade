package theShadeThatFades.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeEidolonFormInfinitePower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeEidolonFormInfinite extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeEidolonFormInfinite.class.getSimpleName());
    public static final String IMG = makeCardPath("ShadeEidolonForm.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheShade.Enums.COLOR_GRAY;

    private static final int COST = 3;
    // /STAT DECLARATION/

    public ShadeEidolonFormInfinite() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        GraveField.grave.set(this, true);
    }

    // Actions the card should do.
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadeEidolonFormInfinitePower(p)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            GraveField.grave.set(this, false);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

}