package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.NewThunderStrikeAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShade.actions.LightningAction;
import theShade.cards.ShadeChaosStormTmpLightningCard;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makePowerPath;

public class ShadeChaosStormPower extends AbstractPower {
    public static final String POWER_ID = "theShade:ShadeChaosStormPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeChaosStorm84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeChaosStorm32.png"));

    private static int CORRUPTION_AMOUNT = 3;
    private static int WITHER_AMOUNT = 2;
    private static int BURN_AMOUNT = 4;
    private static int DAMAGE_AMOUNT = 6;

    public ShadeChaosStormPower(AbstractCreature owner, int amount) {
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
        this.canGoNegative = false;
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
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        for (int i = 0; i < this.amount; i++) {
            int selection = MathUtils.random(3);

            if (selection == 0) {
                this.addToBot(new LightningAction(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, this.owner, null, true, false));
            }
            if (selection == 1) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerToRandomEnemyAction(this.owner, new ShadeCorruptionPower(null, CORRUPTION_AMOUNT), CORRUPTION_AMOUNT));
            }
            if (selection == 2) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerToRandomEnemyAction(this.owner, new ShadeWitherPower(null, WITHER_AMOUNT), WITHER_AMOUNT));
            }
            if (selection == 3) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerToRandomEnemyAction(this.owner, new ShadeBurnPower(null, this.owner, BURN_AMOUNT), BURN_AMOUNT));
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        this.type = PowerType.BUFF;
    }

}
