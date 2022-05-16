package theShadeThatFades.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.SacredBark;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.powers.ShadeCorruptionPower;

public class PurgingDraught extends AbstractPotion {


    public static final String POTION_ID = TheShadeMod.makeID("PurgingDraught");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public PurgingDraught() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main TheShadeMod.java
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.S, PotionColor.SMOKE);
        
        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();

        // Check for Sacred Bark
        if (CardCrawlGame.isInARun() && AbstractDungeon.player.hasRelic(SacredBark.ID)) {
            // Initialize the Description
            description = DESCRIPTIONS[1];
        }
        else {
            // Initialize the Description
            description = DESCRIPTIONS[0];
        }

       // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
        
        // Initialize the on-hover name + description
        tips.add(new PowerTip(name, description));
        // Initialize the keyword for corruption
        tips.add(new PowerTip(
            BaseMod.getKeywordTitle(TheShadeMod.getModID().toLowerCase() + ":corruption"),
            BaseMod.getKeywordDescription(TheShadeMod.getModID().toLowerCase() + ":corruption")
        ));
        
    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        // If you are in combat, lose all Corruption
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && target.hasPower(ShadeCorruptionPower.POWER_ID)) {
                AbstractPower pwr = target.getPower(ShadeCorruptionPower.POWER_ID);
                int currCorruption = pwr.amount;
                pwr.reducePower(currCorruption);
            if (AbstractDungeon.player.hasRelic(SacredBark.ID)) {
                // Remove all debuffs
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(AbstractDungeon.player));
            }
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new PurgingDraught();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 0;
    }


    public void upgradePotion()
    {
        potency += 1;
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

}
