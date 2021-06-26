package theShadeThatFades.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShadeThatFades.util.TextureLoader;

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
    private int playerCorruption;

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
        this.description = DESCRIPTIONS[0];//+ playerCorruption + DESCRIPTIONS[1];
    }

    //    public float atDamageGive(float damage, DamageInfo.DamageType type) {
//        return type == DamageInfo.DamageType.NORMAL ? damage + this.amount : damage;
//    }
    public void atEndOfRound() {
        int playerCorruption = 0;
        this.flash();
        if (AbstractDungeon.player.hasPower("theShadeThatFades:Corruption")) {
            playerCorruption = AbstractDungeon.player.getPower("theShadeThatFades:Corruption").amount;
        }
        if (playerCorruption > 0) {
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, playerCorruption, DamageInfo.DamageType.HP_LOSS)));
        }
    }

//    @Override
//    public void update(int slot) {
//        if (AbstractDungeon.player.hasPower("theShadeThatFades:Corruption")) {
//            playerCorruption = AbstractDungeon.player.getPower("theShadeThatFades:Corruption").amount;
//        }
//        super.update(slot);
//    }
//
//    @Override
//    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//        if (power.ID == "theShadeThatFades:Corruption" && target.isPlayer ) {
//            updateDescription();
//        }
//    }
//
//    @Override
//    public void onAfterCardPlayed(AbstractCard usedCard) {
//        if (AbstractDungeon.player.hasPower("theShadeThatFades:Corruption")) {
//            playerCorruption = AbstractDungeon.player.getPower("theShadeThatFades:Corruption").amount;
//        }
//    }

    @Override
    public void reducePower(int reduceAmount) { // Deteriorate cannot be removed so easily
        return;
    }

    @Override
    public int getHealthBarAmount() {
        int playerCorruption = 0;
        if (AbstractDungeon.player.hasPower("theShadeThatFades:Corruption")) {
            playerCorruption = AbstractDungeon.player.getPower("theShadeThatFades:Corruption").amount;
        }
        int amount = playerCorruption;
        return Math.max(amount, 0);
    }

    @Override
    public Color getColor() {
        return new Color(0.13f, 0.0f, 0.51f, 1.0f); // Dark Indigo
    }
}
