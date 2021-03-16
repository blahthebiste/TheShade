package theShade.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theShade.DefaultMod;
import theShade.powers.ShadeCorruptionPower;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makeRelicOutlinePath;
import static theShade.DefaultMod.makeRelicPath;

public class TwistedSorcery extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("TwistedSorcery");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("sorcery.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("sorcery.png"));

    public TwistedSorcery() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.FLAT);
    }


    // Gain 1 Strength on on equip.
//    @Override
//    public void atBattleStart() {
//        flash();
//        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
//        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
//    }

    // Gain 1 ShadeCorruption at the start of every turn.
    @Override
    public void atTurnStart() {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ShadeCorruptionPower(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
