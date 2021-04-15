package theShadeThatFades.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShadeThatFades.util.TextureLoader;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makePowerPath;

public class ShadeDistortionPower extends AbstractPower {
    public static final String POWER_ID = "theShadeThatFades:ShadeDistortionPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeDistortion84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeDistortion32.png"));

    public ShadeDistortionPower(AbstractCreature owner) {
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
        this.updateExistingCurses();
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_STRENGTH", 0.05F);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (stackAmount < 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        else {
            this.updateExistingCurses();
        }
    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        if (reduceAmount > 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }

    }

//    @Override
//    public void onCardDraw(AbstractCard card) {
//        if (card.type == AbstractCard.CardType.CURSE || card.color == AbstractCard.CardColor.CURSE) {
////            this.flash();
//            if (card.costForTurn < -1) {
//                card.cost = 0;
//            }
//        }
//    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.CURSE || card.color == AbstractCard.CardColor.CURSE) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        }
    }

    @Override
    public void onDrawOrDiscard() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if (c.type == AbstractCard.CardType.CURSE || c.color == AbstractCard.CardColor.CURSE) {
                if (c.costForTurn < -1) {
                    c.costForTurn = 0;
                }
            }
        }

    }

    private void updateExistingCurses() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.type == AbstractCard.CardType.CURSE || c.color == AbstractCard.CardColor.CURSE) {
                if (c.costForTurn < -1) {
                    c.costForTurn = 0;
                }
            }
        }

        var1 = AbstractDungeon.player.drawPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.type == AbstractCard.CardType.CURSE || c.color == AbstractCard.CardColor.CURSE) {
                if (c.costForTurn < -1) {
                    c.costForTurn = 0;
                }
            }
        }

        var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.type == AbstractCard.CardType.CURSE || c.color == AbstractCard.CardColor.CURSE) {
                if (c.costForTurn < -1) {
                    c.costForTurn = 0;
                }
            }
        }

        var1 = AbstractDungeon.player.exhaustPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.type == AbstractCard.CardType.CURSE || c.color == AbstractCard.CardColor.CURSE) {
                if (c.costForTurn < -1) {
                    c.costForTurn = 0;
                }
            }
        }

    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        this.type = PowerType.BUFF;
    }

}
