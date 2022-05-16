package theShadeThatFades.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShadeThatFades.util.TextureLoader;
import theShadeThatFades.vfx.ShadeTetherParticle;

import static theShadeThatFades.TheShadeMod.makePowerPath;

public class ShadeHpTetherPower extends AbstractPower {
    public static final String POWER_ID = "theShadeThatFades:ShadeHpTetherPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AbstractCreature victim;
    private static int hptetherIdOffset; // To keep each instance seperate
    private static int bufferedParticles;
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
        bufferedParticles = 0;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_STRENGTH", 0.05F);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        // Remove this specific instance if the victim is dead
        if (victim.isDead || victim.isDying || victim.currentHealth <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) { // Copy the damage to the victim as HP loss
        if (damageAmount > 0) {
            this.flash();
            this.addToTop(new DamageAction(this.victim, new DamageInfo(this.victim, damageAmount, DamageInfo.DamageType.HP_LOSS)));
            bufferedParticles += damageAmount;
        }
        return damageAmount;
    }

    @Override
    public int onHeal(int healAmount) {
        if (healAmount > 0) {
            this.flash();
            this.addToTop(new HealAction(this.victim, this.owner, healAmount));
            bufferedParticles += healAmount;
        }
        return healAmount;
    }

    @Override
    public void updateParticles() {
        if(this.owner.hb.hovered || bufferedParticles > 0) {
            float startX = this.owner.hb.cX + MathUtils.random(16.0F, -16.0F) * Settings.scale;
            float startY = this.owner.hb.cY + MathUtils.random(16.0F, -16.0F) * Settings.scale;
            float endX = this.victim.hb.cX;
            float endY = this.victim.hb.cY - (this.victim.hb.height)/2;

            AbstractDungeon.effectsQueue.add(new ShadeTetherParticle(startX, startY, endX, endY, false));
//            AbstractDungeon.effectsQueue.add(new ShadeTetherParticle(endX, endY, startX, startY, true));
            if(bufferedParticles > 0) bufferedParticles--;
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.victim.name + DESCRIPTIONS[1];
        this.type = PowerType.BUFF;
    }

}
