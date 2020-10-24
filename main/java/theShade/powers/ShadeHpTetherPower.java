package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import theShade.util.TextureLoader;

import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType.THORNS;
import static theShade.DefaultMod.makePowerPath;

public class ShadeHpTetherPower extends AbstractPower {
    public static final String POWER_ID = "theShade:ShadeHpTetherPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AbstractCreature victim;
    private static int hptetherIdOffset; // To keep each instance seperate

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeHpTetherPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeHpTetherPower32.png"));

    public ShadeHpTetherPower(AbstractCreature owner, AbstractCreature victim) {
        this.name = NAME;
        this.ID = POWER_ID + hptetherIdOffset;
        ++hptetherIdOffset;
        this.owner = owner;
        this.victim = victim;
        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
        this.canGoNegative = false;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_STRENGTH", 0.05F);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) { // Copy the damage to the victim
        if (damageAmount > 0) {
            this.flash();
            this.addToTop(new DamageAction(this.victim, info));
        }
        return damageAmount;
    }

    @Override
    public int onHeal(int healAmount) { // Replace healing with damage during enemy turn
        if (healAmount > 0) {
            this.flash();
            this.addToTop(new HealAction(this.victim, this.owner, healAmount));
        }
        return healAmount;
    }

//    @Override
//    public void onVictory() {
//        this.owner = null;
//    }
//
//    @Override
//    public void  atEndOfRound() {
//        if (victim.isDeadOrEscaped() && this.owner != null && !this.owner.isDeadOrEscaped()) {
//            AbstractDungeon.actionManager.addToBottom(new SuicideAction((AbstractMonster) this.owner));
//        }
//    }
//
//    @Override
//    public void  atStartOfTurn() {
//        if (victim.isDeadOrEscaped() && this.owner != null && !this.owner.isDeadOrEscaped()) {
//            AbstractDungeon.actionManager.addToBottom(new SuicideAction((AbstractMonster) this.owner));
//        }
//    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.victim.name + DESCRIPTIONS[1];
        this.type = PowerType.BUFF;
    }

}
