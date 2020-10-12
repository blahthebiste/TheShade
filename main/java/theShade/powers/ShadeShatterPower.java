package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import theShade.util.TextureLoader;

import java.util.Iterator;

import static theShade.DefaultMod.makePowerPath;

public class ShadeShatterPower extends AbstractPower {
    public static final String POWER_ID = "theShade:Shatter";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeShatter84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeShatter32.png"));


    public ShadeShatterPower(AbstractCreature owner, int amount) {
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
//        this.playingAttack = false;
//        this.bonusDamage = 0;
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

//    @Override
//    public void onUseCard(AbstractCard card, UseCardAction action) {
//        if (card.type == AbstractCard.CardType.ATTACK){
//            System.out.println("DEBUGFORELI: using attack...");
//            AbstractMonster m;
//            if (action.target != null) {
//                m = (AbstractMonster)action.target;
//                targetBlock = m.currentBlock;
//                if (targetBlock > 0) { // Only use shatter if the target has block
//                    System.out.println("DEBUGFORELI: target has block...");
//                    this.flash();
//                    playingAttack = true;
//                    // Play some animation here?
//                }
//            }
//        }
//    }
//
//
//    public float atDamageGive(float damage, DamageInfo.DamageType type) {
//        int bonusDamage = 0;
//        if (type == DamageInfo.DamageType.NORMAL) {
//            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();
//            while(var1.hasNext()) {
//                AbstractMonster monster = (AbstractMonster) var1.next();
//                if (!monster.isDead && !monster.isDying) {
//                    bonusDamage += monster.currentBlock;
//                    System.out.println("DEBUGFORELI: got bonus damage from monster, they had "+monster.currentBlock+" block.");
//                }
//            }
//            this.flash();
//            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 1));
//            System.out.println("DEBUGFORELI: total bonusDamage = "+bonusDamage);
//            return damage + bonusDamage;
//        } else {
//            return damage;
//        }
//    }
//
//
//    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
//        int bonusDamage = 0;
//        if (info.owner == this.owner && info.type == DamageInfo.DamageType.NORMAL) {
//            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();
//            while(var1.hasNext()) {
//                AbstractMonster monster = (AbstractMonster) var1.next();
//                if (!monster.isDead && !monster.isDying) {
//                    bonusDamage += monster.currentBlock;
//                    System.out.println("DEBUGFORELI: got bonus damage from monster, they had "+monster.currentBlock+" block.");
//                }
//            }
//            this.flash();
//            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 1));
//            System.out.println("DEBUGFORELI: total bonusDamage = "+bonusDamage);
//            return damageAmount + bonusDamage;
//        } else {
//            return damageAmount;
//        }
//    }

//    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
//        int targetBlock;
//        System.out.println("DEBUGFORELI: 1 onAttack. damageAmount = "+damageAmount);
//        if (target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
//            AbstractMonster m;
//            if (target != null) {
//                m = (AbstractMonster) target;
//                targetBlock = m.currentBlock;
//                this.flash();
//                bonusDamage = (targetBlock * 2);
////                info.base += bonusDamage;
//                System.out.println("DEBUGFORELI: 2 onAttack. bonusDamage = "+bonusDamage);
//            }
//        }
//    }


    public void updateDescription() {
            if (this.amount == 1) {
                this.description = DESCRIPTIONS[0];
            }
            else {
                this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
            }
            this.type = PowerType.BUFF;
    }

}
