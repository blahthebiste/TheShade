package theShadeThatFades.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeElixirOfNoctisPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeElixirOfNoctis extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeElixirOfNoctis.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeElixirOfNoctis.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 4;

    private static final int DURATION = 2;
    private static final int UPGRADE_DURATION = 1;
    private static final int HEALING = 30;
    private static final int UPGRADE_HEALING = 20;


    public ShadeElixirOfNoctis() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = DURATION;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = HEALING;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target != null) {
            this.addToBot(new ApplyPowerAction(target, p, new ShadeElixirOfNoctisPower(target, magicNumber, defaultSecondMagicNumber, true, (!target.isPlayer)), 5));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DURATION);
            upgradeDefaultSecondMagicNumber(UPGRADE_HEALING);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
