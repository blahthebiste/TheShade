package theShade.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import infinitespire.helpers.CardHelper;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShade.actions.TransformIntoBlackCardAction;
import theShade.util.TextureLoader;

import java.util.Iterator;

import static theShade.DefaultMod.makePowerPath;

public class ShadeDistortionInfinitePower extends AbstractPower {
    public static final String POWER_ID = "theShade:ShadeDistortionInfinitePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeDistortion84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeDistortion32.png"));

    public ShadeDistortionInfinitePower(AbstractCreature owner) {
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
    public void onCardDraw(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE || card.color == AbstractCard.CardColor.CURSE) {
            this.flash();
            AbstractCard blackCard = CardHelper.getRandomBlackCard().makeStatEquivalentCopy();
            blackCard.current_x = blackCard.target_x = card.current_x;
            blackCard.current_y = blackCard.target_y = card.current_y;
            AbstractDungeon.player.hand.removeCard(card);
            blackCard.superFlash(new Color(124.0f, 0.0f, 60.0f, 185.0f));
            AbstractDungeon.player.hand.addToBottom(blackCard);
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.update();
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        this.type = PowerType.BUFF;
    }

}
