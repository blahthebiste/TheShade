package theShadeThatFades.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShadeThatFades.util.TextureLoader;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makePowerPath;

public class ShadeWitherPower extends AbstractPower {
    public static final String POWER_ID = "theShadeThatFades:Wither";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeWither84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeWither32.png"));

    public ShadeWitherPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
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
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "theShadeThatFades:Wither"));
        }

        if (this.amount >= 9999) {
            this.amount = 9999;
        }

    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, NAME));
        }

        if (this.amount >= 9999) {
            this.amount = 9999;
        }

    }

    public void updateDescription() {
            this.description = DESCRIPTIONS[0];
    }

//    public float atDamageGive(float damage, DamageInfo.DamageType type) {
//        return type == DamageInfo.DamageType.NORMAL ? damage + this.amount : damage;
//    }
    public void atEndOfRound() {
//        if (this.amount == 0) {
//            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "theShadeThatFades:Wither"));
//        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, "theShadeThatFades:Wither", 1));
//        }
    }

    public void atStartOfTurn() { // Decay over time
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
//        this.flashWithoutSound();
            // Add to other turn based DOTs
            if (this.owner != null) {
                Iterator var2 = this.owner.powers.iterator();

                AbstractPower effect;
                while (var2.hasNext()) {
                    effect = (AbstractPower) var2.next();
                    // TODO: rework this so it doesn't trigger Sadistic Nature or Snecko Skull
                    if (effect.type == PowerType.DEBUFF && effect.ID == "Poison" || effect.ID == "Vulnerable"|| effect.ID == "Weakened"|| effect.ID == "Frail"|| effect.ID == "Lockon") {
//                        this.addToTop(new ApplyPowerAction(this.owner, this.owner, new PoisonPower(this.owner, this.owner, 1), 1, true));
                        effect.amount++;
                    }
                    // Other DOT effects should be added here. Burn is a special case, it is smart enough to check for Wither on its own.
                }
            }
        }
    }


//    @Override
//    public int onHeal(int healAmount) {
//        healAmount -= this.amount;
//        if (healAmount < 0) {
//            healAmount = 0;
//        }
//        return healAmount;
//    }

}
