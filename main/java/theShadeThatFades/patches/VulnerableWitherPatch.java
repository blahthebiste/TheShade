package theShadeThatFades.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theShadeThatFades.powers.ShadeWitherPower;


@SpirePatch(    // "Use the @SpirePatch annotation on the patch class."
        clz = VulnerablePower.class, // This is the class where the method we will be patching is
        method = "atEndOfRound" // This is the name of the method we will be patching.
)
public class VulnerableWitherPatch {// Don't worry about the "never used" warning - *You* usually don't use/call them anywhere. Mod The Spire does.
    
    private static final Logger logger = LogManager.getLogger(VulnerableWitherPatch.class.getName());
    // It's like a very fancy System.out.println();
    
    @SpirePrefixPatch
    //"A patch method must be a public static method."
    public static SpireReturn<Void> thisIsOurActualPatchMethod(VulnerablePower __instance, boolean ___justApplied) {
        // Exit the function early if the target has wither
        logger.info("Attempting to patch VulnerablePower to not tick down when the target has Wither...");
        if(__instance.owner.hasPower(ShadeWitherPower.POWER_ID) && !___justApplied) {
            return SpireReturn.Return();
        }
        else return SpireReturn.Continue();
    }

}