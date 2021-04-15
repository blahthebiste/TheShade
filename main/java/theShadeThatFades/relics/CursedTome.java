package theShadeThatFades.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.ShadeFatigue;
import theShadeThatFades.util.TextureLoader;

import static theShadeThatFades.TheShadeMod.makeRelicOutlinePath;
import static theShadeThatFades.TheShadeMod.makeRelicPath;

public class CursedTome extends CustomRelic {

    // ID, images, text.
    public static final String ID = TheShadeMod.makeID("CursedTome");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CursedTome.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CursedTome.png"));

    public CursedTome() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
    }

    public void atBattleStart() {
        this.flash();
        this.addToBot(new MakeTempCardInHandAction(new ShadeFatigue()));
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
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
