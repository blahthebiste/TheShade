package theShadeThatFades.powers;

import ShadeCardModifiers.ShadeEthericMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShadeThatFades.util.TextureLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static theShadeThatFades.TheShadeMod.makePowerPath;

public class ShadeEthericShiftPower extends AbstractPower {
    public static final String POWER_ID = "theShadeThatFades:ShadeEthericShiftPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeEthericShiftPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeEthericShiftPower32.png"));

    private List<AbstractCard> modifiedCards;

    public ShadeEthericShiftPower(AbstractCreature owner, int amount) {
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
        this.modifiedCards = new ArrayList<>();
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

        if (stackAmount > 0) { // Reduce card costs further, and add new cards to the list.
            Iterator var2 = AbstractDungeon.player.hand.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (!c.isEthereal) { // Keep track of cards we made ethereal to undo it later
                    CardModifierManager.addModifier(c, new ShadeEthericMod());
                    modifiedCards.add(c);
                }
//                c.isEthereal = true;
                c.setCostForTurn(c.costForTurn - stackAmount);
                if (c.costForTurn < 0) {
                    c.costForTurn = 0;
                }
            }
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

    public void atEndOfRound() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void onRemove() {
        Iterator var1 = modifiedCards.iterator();
        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            CardModifierManager.removeModifiersById(c, ShadeEthericMod.MOD_ID, false);// Remove ethereal
        }
        modifiedCards.clear();
    }

    @Override
    public void onInitialApplication () {
        Iterator var2 = AbstractDungeon.player.hand.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (!c.isEthereal) { // Keep track of cards we made ethereal to undo it later
                CardModifierManager.addModifier(c, new ShadeEthericMod());
                modifiedCards.add(c);
            }
//            c.isEthereal = true;
            if(c.costForTurn > 0) {
                c.setCostForTurn(c.costForTurn - this.amount);
                if (c.costForTurn < 0) {
                    c.costForTurn = 0;
                }
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        this.type = PowerType.BUFF;
    }

}
