package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import theShade.cards.deprecated.ShadeApexCorruption;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makePowerPath;

public class ShadeCorruptionPower extends AbstractPower {
    public static final String POWER_ID = "theShade:Corruption";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeCorruption84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeCorruption32.png"));

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("theShade:ShadeApexCorruption");
    public static final String BUBBLE_DESCRIPTION = eventStrings.DESCRIPTIONS[0];

    public ShadeCorruptionPower(AbstractCreature owner, int amount) {
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
//        this.loadRegion("ShadeCorruption");
        this.canGoNegative = false;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_STRENGTH", 0.05F);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (!this.owner.hasPower(ShadeApexCorruptionPower.POWER_ID)) {
            int preamount = this.amount;
            this.amount += stackAmount;
            if (this.owner.hasPower(ShadeOverdrivePower.POWER_ID)) {
                this.amount += stackAmount; // Double the corruption if the player has Overdrive.
            }
            if (this.amount >= 999 && preamount < 999) { // When overcoming 999, enter Apex Corruption
                // BROKEN, WIP
                //                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, BUBBLE_DESCRIPTION, true));
            }
            if (this.amount >= 9999) {
                this.amount = 9999;
            }

            if (this.amount <= 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "theShade:Corruption"));
            }
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        if (!this.owner.hasPower(ShadeApexCorruptionPower.POWER_ID)) {
            this.amount -= reduceAmount;
            if (this.amount >= 9999) {
                this.amount = 9999;
            }

            if (this.amount <= 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "theShade:Corruption"));
            }
        }
    }

    @Override
    public void updateDescription() {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
            this.type = PowerType.BUFF;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage + this.amount : damage;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage + this.amount : damage;
    }


}
