package theShade.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShade.actions.ShadeBurnTickAction;
import theShade.util.TextureLoader;

import java.util.Iterator;

import static theShade.DefaultMod.makePowerPath;

// Shamelessly copied from Jorbs spire mod: https://github.com/dbjorge/jorbs-spire-mod/tree/0e55310748d5c0459b9c09cb32ec471f82b8e20c
public class ShadeBurnPower extends AbstractPower implements HealthBarRenderPower {
    public static final String POWER_ID = "theShade:Burn";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCreature source;
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeBurn84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeBurn32.png"));

    public ShadeBurnPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }

        if (this.amount <= 0) {
            this.amount = 0;
        }

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        this.isTurnBased = true;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }

        if (this.amount <= 0) {
            this.amount = 0;
        }
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        }
    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }

        if (this.amount <= 0) {
            this.amount = 0;
        }
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, NAME));
        }

    }

    public void updateDescription() {
        if (this.amount <= 0) {
            this.description = String.format(DESCRIPTIONS[0],DESCRIPTIONS[2]);
        } else {
            if (this.owner != null && !this.owner.isPlayer) {
                this.description = String.format(DESCRIPTIONS[0], this.amount, DESCRIPTIONS[2]);
            } else {
                this.description = String.format(DESCRIPTIONS[1], this.amount, DESCRIPTIONS[3]);
            }
        }
    }


//    public void atEndOfRound() {
//        if (this.amount <= 0) {
//            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
//        }
//    }

//    @Override
//    public void atEndOfTurn(boolean isPlayer) {
//        super.atEndOfTurn(isPlayer);
//        if (!owner.hasPower(InvinciblePower.POWER_ID)) {
//            performBurningTick();
//        }
//    }

//    @Override
//    public void onEnergyRecharge() {
//        // Can't be blocked this way, but oh well
//        System.out.println("DEBUGFORELI: onEnergyRecharge (burning) now!");
//        if (owner.isPlayer) performBurningTick();
//    }

    @Override
    public void atStartOfTurn() {
        // Can't be blocked this way, but oh well
//        if (!owner.isPlayer)
        System.out.println("DEBUGFORELI: atStartTurn (burning) now!");
            performBurningTick();
    }

    private void performBurningTick() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flashWithoutSound();
            CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
            AbstractDungeon.actionManager.addToTop(
                    new ShadeBurnTickAction(this.owner, this.source, this.amount, AbstractGameAction.AttackEffect.FIRE));
        }

    }

    @Override
    public int getHealthBarAmount() {
        int amount = this.amount;
        return Math.max(amount, 0);
    }

    @Override
    public Color getColor() {
        return Color.ORANGE.cpy();
    }

}
