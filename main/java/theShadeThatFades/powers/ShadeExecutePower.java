package theShadeThatFades.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import theShadeThatFades.util.TextureLoader;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makePowerPath;

public class ShadeExecutePower extends AbstractPower {
    public static final String POWER_ID = "theShadeThatFades:ExecutePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeExecute84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeExecute32.png"));

    public ShadeExecutePower(AbstractCreature owner, int amount) {
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
        this.updateDescription();
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
        this.updateDescription();
    }


    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            AbstractMonster m = null;
            if (action.target != null) { // For single target attacks
                m = (AbstractMonster) action.target;
            }
            else {
                // Special case for AOE attacks: find the lowest HP enemy and treat that as the target
                Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();
                int lowestHP = Integer.MAX_VALUE; // just in case some madman wants to make enemies with Integer.MAX_VALUE HP
                while(var1.hasNext()) {
                    AbstractMonster monster = (AbstractMonster) var1.next();
                    if (!monster.isDead && !monster.isDying) {
                        if (monster.currentHealth <= lowestHP) {
                            m = monster;
                            lowestHP = monster.currentHealth;
//                            System.out.println("New lowest HP found: " + lowestHP);
                        }
                    }
                }
            }
            if (m == null) { // If there are no valid targets, just quit
                return;
            }
            if (!m.isDead && !m.isDying && m.currentHealth + m.currentBlock <= (card.damage * (this.amount + 1))) {
//                System.out.println("Card damage was enough: " + card.damage);
                // Only trigger if the target's remaining HP is low enough
                this.flash();
                while (true) {
                    AbstractCard tmp = card.makeSameInstanceOf();
                    AbstractDungeon.player.limbo.addToBottom(tmp);
                    tmp.current_x = card.current_x;
                    tmp.current_y = card.current_y;
                    tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                    tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                    if (m != null) {
                        tmp.calculateCardDamage(m);
                    }

                    tmp.purgeOnUse = true;
                    AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                    --this.amount;
                    if (this.amount == 0) {
                        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.POWER_ID));
                        break;
                    }
                }
            } //else System.out.println("Card damage was not enough: " + card.damage);
        }
    }

    public void updateDescription() {
        if (this.amount < 2) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        }
        else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2]+ DESCRIPTIONS[3];
        }
        this.type = PowerType.BUFF;
    }

}
