package theShade.powers;

import ShadeCardModifiers.ShadeShadowBlendReplayCardsAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;
import theShade.util.TextureLoader;

import java.util.*;

import static theShade.DefaultMod.makePowerPath;

public class ShadeShadowBlendPower extends AbstractPower {
    public static final String POWER_ID = "theShade:ShadeShadowBlend";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeShadowBlend84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeShadowBlend32.png"));

    private static int shadowblendIdOffset; // To keep each instance seperate
    private LinkedHashMap<AbstractCard, AbstractMonster> dupedCards;

    public ShadeShadowBlendPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID + shadowblendIdOffset;
        ++shadowblendIdOffset;
        this.owner = owner;
        this.amount = amount;
        dupedCards = new LinkedHashMap<>();
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
        this.isTurnBased = true;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_STRENGTH", 0.05F);
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        // Put fancy particle effects here someday
//        if (!Settings.DISABLE_EFFECTS) {
//            AbstractDungeon.player.particleTimer -= Gdx.graphics.getDeltaTime();
//            if (this.particleTimer < 0.0F) {
//                this.particleTimer = 0.05F;
//                AbstractDungeon.effectsQueue.add(new WrathParticleEffect());
//            }
//        }
//
//        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
//        if (this.particleTimer2 < 0.0F) {
//            this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
//            AbstractDungeon.effectsQueue.add(new StanceAuraEffect("Wrath"));
//        }
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
        if (!card.purgeOnUse && this.amount > 0) {
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
//            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = (float)Settings.WIDTH;
            tmp.current_y = (float)Settings.HEIGHT;
            tmp.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            dupedCards.put(tmp, m);
            System.out.println("Added " + tmp.cardID);
        }
    }


    @Override
    public void onRemove() {
//        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new IntangiblePlayerPower(this.owner, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ShadeShadowBlendReplayCardsAction(dupedCards));
        super.onRemove();
    }


    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        this.type = PowerType.BUFF;
    }

}
