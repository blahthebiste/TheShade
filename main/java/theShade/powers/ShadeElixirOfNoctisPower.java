package theShade.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makePowerPath;

public class ShadeElixirOfNoctisPower extends AbstractPower {
    public static final String POWER_ID = "theShade:ShadeElixirOfNoctisPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeElixirOfNoctisPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeElixirOfNoctisPower32.png"));

    private static int noctisIdOffset; // To keep each instance seperate
    private int healing;
    public AbstractMonster monster;
    public boolean deepSleep;
    public boolean targetIsMonster;

    // Mostly copied from the Disciple's Sleep power https://github.com/Tempus/The-Disciple/blob/c3b05ef16d3168b112a9829fb27a64f00ab88dc9/src/main/java/powers/SleepPower.java
    // Differs in a few ways.
    public ShadeElixirOfNoctisPower(AbstractCreature owner, int amount, int healing, boolean deepSleep, boolean targetIsMonster) {
        this.name = NAME;
        this.ID = POWER_ID + noctisIdOffset;
        ++noctisIdOffset;
        this.owner = owner;
        this.amount = amount;
        this.healing = healing;
        this.deepSleep = deepSleep; // Determines if taking damage will wake the target up
        this.targetIsMonster = targetIsMonster;
        if (this.targetIsMonster) { // Cast owner to monster, if they are, so we can handle intents
            this.monster = (AbstractMonster)this.owner;
        }
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
    public void onRemove() {
        this.wake();
    }

    @Override
    public void atStartOfTurn() {
        this.applySleep();
        // Apply healing
        this.addToBot(new HealAction(this.owner, this.owner, this.healing));
        if (this.targetIsMonster) {
            switch (this.monster.id) {
                case "GiantHead":
                    int count = (int) ReflectionHacks.getPrivate(this.monster, this.monster.getClass(), "count");
                    ReflectionHacks.setPrivate(this.monster, this.monster.getClass(), "count", count + 1);
                    break;
                case "BookOfStabbing":
                    int stabCount = (int) ReflectionHacks.getPrivate(this.monster, this.monster.getClass(), "stabCount");
                    ReflectionHacks.setPrivate(this.monster, this.monster.getClass(), "stabCount", stabCount - 1);
                    break;
                case "Maw":
                    int turnCount = (int) ReflectionHacks.getPrivate(this.monster, this.monster.getClass(), "turnCount");
                    ReflectionHacks.setPrivate(this.monster, this.monster.getClass(), "turnsTaken", turnCount - 1);
                    break;
            }
        }
    }

    // Decrement Sleep, and make sure the enemy is correctly set to sleep
    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if (!isPlayer) {
            this.applySleep();
        }

        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    public void applySleep() {
        if (this.targetIsMonster) {
            this.monster.setMove((byte) -2, AbstractMonster.Intent.SLEEP);
            this.monster.createIntent();
        }
        else {
            // Need to do something for the player
            this.addToBot(new PressEndTurnButtonAction());
        }
    }

    // Wake up!
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (damageAmount > 0 && !this.deepSleep) {
            this.wake();
        }
        return damageAmount;
    }

    public void wake() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));

        if (this.targetIsMonster) {
            // Special Cases:
            switch (this.monster.id) {

                case "SlimeBoss":
                    this.monster.setMove((byte) 4, AbstractMonster.Intent.STRONG_DEBUFF);
                    break;

                case "Looter":
                case "Mugger":
                    this.monster.setMove((byte) 3, AbstractMonster.Intent.ESCAPE);
                    break;

                case "BanditChild":
                    this.monster.setMove((byte) 1, AbstractMonster.Intent.ATTACK, ((DamageInfo) this.monster.damage.get(0)).base, 2, true);
                    ;
                    break;

                default:
                    this.monster.rollMove();
                    break;
            }

            this.monster.createIntent();
        }
    }

    @Override
    public void onInitialApplication() {
        this.applySleep();
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + this.healing + DESCRIPTIONS[3];
        }
        this.type = PowerType.BUFF;
    }

}
