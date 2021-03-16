package theShade.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShade.DefaultMod;
import theShade.powers.ShadeWitherPower;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makeRelicOutlinePath;
import static theShade.DefaultMod.makeRelicPath;

public class WiltingRose extends CustomRelic implements OnApplyPowerRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("WiltingRose");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("WiltingRose.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("sorcery.png"));

    public WiltingRose() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean onApplyPower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        // If the power is Wither and the source is the player, apply weak to the target
        if (abstractPower.ID.equals(ShadeWitherPower.POWER_ID) && source.equals(AbstractDungeon.player) && !target.hasPower(ShadeWitherPower.POWER_ID)) {
            this.addToBot(new ApplyPowerAction(target, source, new WeakPower(target, 1, false), 1));
            this.flash();
        }
        return true;
    }
}
