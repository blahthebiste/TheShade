package theShade.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theShade.DefaultMod;
import theShade.powers.ShadeBurnPower;
import theShade.powers.ShadeWitherPower;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makeRelicOutlinePath;
import static theShade.DefaultMod.makeRelicPath;

public class HellfireTalisman extends CustomRelic implements OnApplyPowerRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("HellfireTalisman");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HellfireTalisman.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("sorcery.png"));

    public HellfireTalisman() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean onApplyPower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        // If the power is Burning and the source is the player, apply vulnerable to the target
        if (abstractPower.ID.equals(ShadeBurnPower.POWER_ID) && source.equals(AbstractDungeon.player)&& !target.hasPower(ShadeBurnPower.POWER_ID)) {
            this.addToBot(new ApplyPowerAction(target, source, new VulnerablePower(target, 1, false), 1));
            this.flash();
        }
        return true;
    }
}
