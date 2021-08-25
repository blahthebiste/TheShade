package theShadeThatFades.cards.uncommon;

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
import theShadeThatFades.powers.ShadeBurnPower;
import theShadeThatFades.powers.ShadeWitherPower;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeWitchFire extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeWitchFire.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeWitchFire.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 0;

    private static final int BURN_AMOUNT = 3;
    private static final int WITHER_AMOUNT = 2;

    // /STAT DECLARATION/


    public ShadeWitchFire() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null)
            target = AbstractDungeon.player;

        if (upgraded) {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                Iterator var3 = AbstractDungeon.getMonsters().monsters.iterator();

                while(var3.hasNext()) {
                    AbstractMonster monster = (AbstractMonster)var3.next();
                    if (!monster.isDead && !monster.isDying) {
                        this.addToBot(new ApplyPowerAction(monster, p, new ShadeBurnPower(monster, p, this.BURN_AMOUNT), this.BURN_AMOUNT));
                        this.addToBot(new ApplyPowerAction(monster, p, new ShadeWitherPower(monster, WITHER_AMOUNT), WITHER_AMOUNT));
                    }
                }
            }
        }
        else
        {
                this.addToBot(new ApplyPowerAction(target, p, new ShadeBurnPower(target, p, this.BURN_AMOUNT), this.BURN_AMOUNT));
                this.addToBot(new ApplyPowerAction(target, p, new ShadeWitherPower(target, WITHER_AMOUNT), WITHER_AMOUNT));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            this.target = CardTarget.ALL_ENEMY;
            initializeDescription();
        }
    }
}
