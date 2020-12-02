package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theShade.actions.ShadeInfusionTickAction;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makePowerPath;

public class ShadeInfusionUpgradedPower extends AbstractPower {
    public static final String POWER_ID = "theShade:ShadeInfusionUpgradedPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeInfusionPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeInfusionPower32.png"));

    public boolean isEnemyTurn;

    public ShadeInfusionUpgradedPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.priority = 110; // higher priority so that it triggers last? Needed so that other endOfRound DOTs trigger before this
        this.isEnemyTurn = AbstractDungeon.actionManager.turnHasEnded;
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

    @Override
    public void atEndOfRound() {
//        System.out.println("end of round");
        this.addToBot(new ShadeInfusionTickAction(this.owner, true));
    }

    @Override
    public int onHeal(int healAmount) { // double healing during enemy turn
        if (isEnemyTurn) {
            this.flash();
            return healAmount*3;
        }
        else {
            return healAmount;
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) { // double damage during enemy turn
        if (isEnemyTurn) {
            this.flash();
            return damageAmount*3;
        }
        else {
            return damageAmount;
        }

    }

    @Override
    public void atStartOfTurn() {
        if (this.owner.equals(AbstractDungeon.player)) {
            isEnemyTurn = false;
//            System.out.println("start of turn for player");
        }
        else {
            isEnemyTurn = true;
//            System.out.println("start of turn for monster");
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
//            System.out.println("end of turn for player");
            isEnemyTurn = true;
        }
        else {
//            System.out.println("end of turn for monster");
        }
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

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        this.type = PowerType.BUFF;
    }

}
