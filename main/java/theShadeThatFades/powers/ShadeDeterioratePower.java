package theShadeThatFades.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theShadeThatFades.util.TextureLoader;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makePowerPath;

public class ShadeDeterioratePower extends AbstractPower implements HealthBarRenderPower {
    public static final String POWER_ID = "theShadeThatFades:Deteriorate";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeDeteriorate84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeDeteriorate32.png"));

    public ShadeDeterioratePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
        this.type = PowerType.DEBUFF;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void atEndOfRound() {
        int totalDebuffs = 0;
        this.flash();

        Iterator var1 = this.owner.powers.iterator();

        while(var1.hasNext()) {
            AbstractPower p = (AbstractPower)var1.next();
            if (p.type == PowerType.DEBUFF) {
                totalDebuffs += Math.max(p.amount, 1); // Debuffs with no amount count as 1
            }
        }
        this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, totalDebuffs, DamageInfo.DamageType.HP_LOSS)));
        // Monster loses strength
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -1), -1));
    }

    @Override
    public void reducePower(int reduceAmount) { // Deteriorate cannot be removed so easily
        return;
    }

    @Override
    public int getHealthBarAmount() {
        int totalDebuffs = 0;
        Iterator var1 = this.owner.powers.iterator();

        while(var1.hasNext()) {
            AbstractPower p = (AbstractPower)var1.next();
            if (p.type == PowerType.DEBUFF) {
                totalDebuffs += Math.max(p.amount, 1); // Debuffs with no amount count as 1
            }
        }
        return Math.max(totalDebuffs, 0);
    }

    @Override
    public Color getColor() {
        return new Color(0.13f, 0.0f, 0.51f, 1.0f); // Dark Indigo
    }
}
