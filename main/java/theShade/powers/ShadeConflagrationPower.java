package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShade.util.TextureLoader;

import java.util.Iterator;

import static theShade.DefaultMod.makePowerPath;

public class ShadeConflagrationPower extends AbstractPower {
    public static final String POWER_ID = "theShade:ShadeConflagrationPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeConflagrationPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeConflagrationPower32.png"));

    public ShadeConflagrationPower(AbstractCreature owner, int amount) {
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        boolean playerHasCorruption = AbstractDungeon.player.hasPower(ShadeCorruptionPower.POWER_ID);
        if (card.type == AbstractCard.CardType.ATTACK && !playerHasCorruption) {
            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster)var1.next();
                if (!m.isDead && !m.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this.owner, new ShadeBurnPower(m, this.owner, this.amount), this.amount));
                }
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        this.type = PowerType.BUFF;
    }

}
