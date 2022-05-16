package theShadeThatFades.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.powers.ShadeWitherPower;

public class WitherPotion extends AbstractPotion {


    public static final String POTION_ID = TheShadeMod.makeID("WitherPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public WitherPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main TheShadeMod.java
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SPIKY, PotionColor.SMOKE);
        
        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();
        
        // Initialize the Description
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        
       // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
        targetRequired = true;

        // Initialize the on-hover name + description
        tips.add(new PowerTip(name, description));
        // Initialize the keyword for corruption
        tips.add(new PowerTip(
            BaseMod.getKeywordTitle(TheShadeMod.getModID().toLowerCase() + ":wither"),
            BaseMod.getKeywordDescription(TheShadeMod.getModID().toLowerCase() + ":wither")
        ));
        
    }

    @Override
    public void use(AbstractCreature target) {
        // If you are in combat, gain strength and the "lose strength at the end of your turn" power, equal to the potency of this potion.
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new ShadeWitherPower(target, potency), potency));
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new WitherPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 3;
    }

    public void upgradePotion()
    {
      potency += 3;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
