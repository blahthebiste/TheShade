package theShadeThatFades.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PureStrikePatch
{
    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class addLostCorruptionField {
        public static SpireField<Boolean> LostCorruption = new SpireField<>(() -> false);
    }


    @SpirePatch(
            clz= AbstractPlayer.class,
            method="applyStartOfCombatLogic"
    )
    public static class resetCardsAddedToDrawOrDiscard {
        @SpirePrefixPatch
        public static void Prefix()
        {
            AbstractPlayer p = AbstractDungeon.player;
            // Set value back to "false" at the start of each turn
            addLostCorruptionField.LostCorruption.set(p, false);
        }
    }
}