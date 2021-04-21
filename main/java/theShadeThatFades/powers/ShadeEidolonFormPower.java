package theShadeThatFades.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import theShadeThatFades.actions.TransformIntoBlackCardAction;
import theShadeThatFades.util.TextureLoader;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makePowerPath;

public class ShadeEidolonFormPower extends AbstractPower {
    public static final String POWER_ID = "theShadeThatFades:ShadeEidolonFormPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeAmalgamFormPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeAmalgamFormPower32.png"));

    private static int THRESHOLD = 5;

    public ShadeEidolonFormPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
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
        if (stackAmount < 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        if (reduceAmount > 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }

    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            Iterator var2 = AbstractDungeon.player.hand.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                this.addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
                this.amount++;
                if (this.amount >= THRESHOLD) {
                    this.amount = 0;
                    this.addToBot(new ApplyPowerAction(this.owner, this.owner, new IntangiblePlayerPower(this.owner, 1), 1));
                }
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + THRESHOLD + DESCRIPTIONS[1];
        this.type = PowerType.BUFF;
    }

}
