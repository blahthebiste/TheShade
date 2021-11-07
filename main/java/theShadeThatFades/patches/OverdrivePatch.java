package theShadeThatFades.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theShadeThatFades.powers.ShadeCorruptionPower;
import theShadeThatFades.powers.ShadeOverdrivePower;


@SpirePatch(    // "Use the @SpirePatch annotation on the patch class."
        clz = ApplyPowerAction.class, // This is the class where the method we will be patching is. In our case - Abstract Dungeon
        method = "update" // This is the name of the method we will be patching.
)
public class OverdrivePatch {// Don't worry about the "never used" warning - *You* usually don't use/call them anywhere. Mod The Spire does.
    
    private static final Logger logger = LogManager.getLogger(OverdrivePatch.class.getName()); // This is our logger! It prints stuff out in the console.
    
    @SpireInsertPatch( // This annotation of our patch method specifies the type of patch we will be using. In our case - a Spire Insert Patch
            
            locator = Locator.class
    )
    //"A patch method must be a public static method."
    public static void thisIsOurActualPatchMethod(ApplyPowerAction __instance, @ByRef AbstractPower[]  ___powerToApply) {

        logger.info("Attempting to patch ApplyPowerAction to double Corruption gain...");
        if(__instance.target.equals(AbstractDungeon.player) && AbstractDungeon.player.hasPower(ShadeOverdrivePower.POWER_ID) && ___powerToApply[0].ID.equals(ShadeCorruptionPower.POWER_ID)) {
            __instance.amount *= 2;
        }
    }
    
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {// All the locator has and needs is an override of the Locate method
            // Put our hard-coded, power-specific behavior right above all the base game hard-coded, power-specific behavior
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}