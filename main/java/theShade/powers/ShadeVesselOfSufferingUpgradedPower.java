package theShade.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makePowerPath;

public class ShadeVesselOfSufferingUpgradedPower extends AbstractPower {
    public static final String POWER_ID = "theShade:VesselOfSufferingUpgraded";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShadeVesselOfSuffering84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShadeVesselOfSuffering32.png"));

    private boolean isPlayerTurn;

    public ShadeVesselOfSufferingUpgradedPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        this.isPlayerTurn = true;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
//        System.out.println("DEBUGFORELI: end of turn now!");
        if (isPlayer) {
            isPlayerTurn = false;
        }
    }

    @Override
    public void onEnergyRecharge() {
//        System.out.println("DEBUGFORELI: energy recharge now!");
        super.onEnergyRecharge();
        if (owner.isPlayer) {
            isPlayerTurn = true;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    public void wasHPLost(DamageInfo info, int damageAmount) {
//        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0) {
        if (info.owner != null && damageAmount > 0) {
            this.flash();
            if (isPlayerTurn) { // Give raw energy during the players turn
//                System.out.println("DEBUGFORELI: took damage with vessel of suffering, during player turn!");
                this.addToBot(new GainEnergyAction(this.amount));
            }
            else { // Otherwise, give energy at the start of the player's next turn
//                System.out.println("DEBUGFORELI: took damage with vessel of suffering, outside player turn!");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new EnergizedPower(owner, this.amount)));
            }
        }

    }
}
