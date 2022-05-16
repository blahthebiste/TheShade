package theShadeThatFades.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.actions.ShadeBindingAction;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeDeterioratePower;
import theShadeThatFades.powers.ShadeWitherPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeRitualOfBinding extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeRitualOfBinding.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeRitualOfBinding.png");

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;

    // /STAT DECLARATION/


    public ShadeRitualOfBinding() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = 50;
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target != null) {
            // Trigger the tether binding action
            this.addToBot(new ShadeBindingAction(target));
            // Then Shackle them for 100
            this.addToBot(new ApplyPowerAction(target, p, new StrengthPower(target, -this.magicNumber), -this.magicNumber));
            if (!target.hasPower("Artifact")) {
                this.addToBot(new ApplyPowerAction(target, p, new GainStrengthPower(target, this.magicNumber), this.magicNumber));
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
