package theShadeThatFades.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theShadeThatFades.powers.ShadeCorruptionPower;
import theShadeThatFades.powers.ShadeOverdrivePower;
import theShadeThatFades.powers.ShadeWitherPower;


@SpirePatch(    // "Use the @SpirePatch annotation on the patch class."
        clz = PoisonLoseHpAction.class, // This is the class where the method we will be patching is. In our case - Abstract Dungeon
        method = "update" // This is the name of the method we will be patching.
)
public class PoisonWitherPatch {// Don't worry about the "never used" warning - *You* usually don't use/call them anywhere. Mod The Spire does.
    
    private static final Logger logger = LogManager.getLogger(PoisonWitherPatch.class.getName()); // This is our logger! It prints stuff out in the console.
    // It's like a very fancy System.out.println();
    
    @SpireInsertPatch( // This annotation of our patch method specifies the type of patch we will be using. In our case - a Spire Insert Patch
            
            locator = Locator.class
    )
    //"A patch method must be a public static method."
    public static SpireReturn<Void> thisIsOurActualPatchMethod(PoisonLoseHpAction __instance) {

        logger.info("Attempting to patch PoisonLoseHpAction to not tick down poison when the target has wither...");
        if(__instance.target.hasPower(ShadeWitherPower.POWER_ID)) {
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            return SpireReturn.Return();
        }
        else return SpireReturn.Continue();
    }
    
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {// All the locator has and needs is an override of the Locate method
            // Put our hard-coded, power-specific behavior right above all the base game hard-coded, power-specific behavior
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "getPower");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}