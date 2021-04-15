package theShadeThatFades.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.powers.ShadeBurnPower;
import theShadeThatFades.util.TextureLoader;

import static theShadeThatFades.TheShadeMod.makeRelicOutlinePath;
import static theShadeThatFades.TheShadeMod.makeRelicPath;

public class BurningTome extends CustomRelic {

    // ID, images, text.
    public static final String ID = TheShadeMod.makeID("BurningTome");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BurningTome.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BurningTome.png"));

    public BurningTome() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
    }

    public void atBattleStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ShadeBurnPower(AbstractDungeon.player, AbstractDungeon.player, 3), 3));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    public void onEquip() {
        ++AbstractDungeon.player.masterHandSize;
    }

    public void onUnequip() {
        --AbstractDungeon.player.masterHandSize;
    }

    public void atTurnStart() {
        this.flash();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
