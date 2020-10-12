package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.sun.java.swing.action.ActionManager;
import theShade.util.TextureLoader;

import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType.THORNS;
import static theShade.DefaultMod.makePowerPath;

public class ShadeSubversionPower extends AbstractPower {
    public static final String POWER_ID = "theShade:SubversionPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeSubversionPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeSubversionPower32.png"));

    private AbstractCreature SubversionCreature;
    private boolean swapInProgress;

    public ShadeSubversionPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.swapInProgress = false;
//        SubversionCreature = new AbstractMonster() {
//            @Override
//            public void takeTurn() {
//
//            }
//
//            @Override
//            protected void getMove(int i) {
//
//            }
//        }
        this.amount = amount;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
        this.canGoNegative = false;
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_STRENGTH", 0.05F);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }

        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }

        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }

    }

    @Override
    public void atEndOfRound() {
        this.addToBot(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 1));
    }

    @Override
    public int onHeal(int healAmount) { // Replace healing with damage during enemy turn
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner, new DamageInfo(this.owner, healAmount, THORNS), AbstractGameAction.AttackEffect.NONE));
            return 0;
        }
        else {
            return healAmount;
        }
    }

//    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {// Replace damage with healing
//        this.addToTop(new HealAction(this.owner,this.owner, (int)damage));
//        return 0;
//    }

//    @Override
//    public void wasHPLost(DamageInfo info, int damageAmount) { // Covers player damage case only
//        super.wasHPLost(info, 0);
//        this.flash();
//        this.addToTop(new HealAction(this.owner,this.owner, damageAmount));
//    }

//    @Override
//    public int onLoseHp(int damageAmount) {// Covers player damage case only
//        this.flash();
//        this.addToTop(new HealAction(this.owner,this.owner, damageAmount));
//        return 0;
//    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) { // Replace damage with healing during player turn
        if (!AbstractDungeon.actionManager.turnHasEnded && info.owner != null && damageAmount > 0) {
            this.flash();
            this.addToTop(new HealAction(this.owner,this.owner, damageAmount));
            return 0;
        }
        else {
            return damageAmount;
        }

    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
        else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
        this.type = PowerType.BUFF;
    }

}
