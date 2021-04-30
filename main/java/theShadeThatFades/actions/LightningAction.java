package theShadeThatFades.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.Iterator;

// Very similar to LightningOrbEvokeAction, but gives us more control over lightning aesthetics.
// Shade lightning still synergizes with Lock-on and Electrodynamics, though not focus.
public class LightningAction extends AbstractGameAction {
    public static final Color DEFAULT_LIGHTNING_COLOR = Color.WHITE.cpy();
    protected int damage;
    protected DamageInfo.DamageType type;
    protected AbstractCreature source;
    protected AbstractCreature target;
    protected boolean isRandom;
    protected boolean targetsAll;
    protected Color customColor;

    public LightningAction(int damage, DamageInfo.DamageType type, AbstractCreature source, AbstractCreature target, boolean isRandom, boolean targetsAll, Color customColor) {
        this.damage = damage;
        this.type = type;
        this.source = source;
        this.target = target; // Target has top priority.
        this.isRandom = isRandom; // If target is null, defer to isRandom and targetsAll
        this.targetsAll = targetsAll;
        this.customColor = customColor;
    }

    public LightningAction(int damage, DamageInfo.DamageType type, AbstractCreature source, AbstractCreature target, boolean isRandom, boolean targetsAll) {
        this(damage, type, source, target, isRandom, targetsAll, DEFAULT_LIGHTNING_COLOR);
    }

    public LightningAction(int damage, DamageInfo.DamageType type, AbstractCreature source, AbstractCreature target) {
        this(damage, type, source, target, false, source.hasPower("Electro"), DEFAULT_LIGHTNING_COLOR);
    }

    public LightningAction(int damage, DamageInfo.DamageType type, AbstractCreature source) {
        this(damage, type, source, null, true, source.hasPower("Electro"), DEFAULT_LIGHTNING_COLOR);
    }

    //====================================START CUSTOM LIGHTNING EFFECT=================================================
    public class CustomLightningEffect extends LightningEffect {
        protected TextureAtlas.AtlasRegion img;
        protected float x,y;

        public CustomLightningEffect(float x, float y, Color customColor) {
            super(x, y);
            this.img = ImageMaster.vfxAtlas.findRegion("combat/lightning");
            this.x = x - (float)this.img.packedWidth / 2.0F; // Need to keep our own copies of x and y because they are private in the super class
            this.y = y;
            this.color = customColor;
            this.duration = 0.7F;
//            this.scale =  1.50F * Settings.scale;
            this.startingDuration = 0.7F;
        }

        @Override
        public void render(SpriteBatch sb) {
            sb.setBlendFunction( GL30.GL_SRC_ALPHA,  GL30.GL_ONE);
            sb.setColor(Color.WHITE.cpy());
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
            if (!this.color.equals(Color.WHITE.cpy())) {
                sb.setColor(this.color); // Extra flash for different colors
                sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
                sb.draw(this.img, this.x, this.y, (float) this.img.packedWidth / 2.0F, 0.0F, (float) this.img.packedWidth, (float) this.img.packedHeight, this.scale, this.scale, this.rotation);
            }
        }
    }
    //======================================END CUSTOM LIGHTNING EFFECT=================================================



    protected void StrikeLightning(AbstractCreature target) {
        if (target.isDeadOrEscaped() || target.isDying || target.isEscaping || target.currentHealth <= 0) return;
        if (target.hasPower("Lockon")) {
            this.damage = (int)((float)this.damage * 1.5F);
        }
        this.addToTop(new DamageAction(this.target, new DamageInfo(this.source, this.damage, this.type)));
        this.addToTop(new VFXAction(new CustomLightningEffect(this.target.drawX, this.target.drawY, this.customColor)));
        this.addToTop(new VFXAction(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect)));
        this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
    }

    public void update() {
        if (!Settings.FAST_MODE) {
            this.addToTop(new WaitAction(0.1F));
        }

        if (targetsAll) {
            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();
            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                if (!m.isDead && !m.isDying) {
                    StrikeLightning(m);
                }
            }
        }
        else {
//            System.out.println("target: " + target + "  isRandom: " + isRandom);
            // Need to figure out who to hit if the target is null
            if (target == null && isRandom) {
                AbstractCreature m = AbstractDungeon.getRandomMonster();
                this.target = m;
            }
            else if (target == null) {
                // Weird case where targetsAll was false, isRandom is false, and target is null. Not a valid case.
                throw new NullPointerException("No valid target given! Use random target or all targets, or give valid target!");
            }
//            System.out.println("Striking target");
            StrikeLightning(target);
        }
        this.isDone = true;
    }
}
