package theShade.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShade.DefaultMod;
import theShade.powers.ShadeBurnPower;
import theShade.powers.ShadeCorruptionPower;
import theShade.util.TextureLoader;

import java.util.Iterator;

import static theShade.DefaultMod.makeRelicOutlinePath;
import static theShade.DefaultMod.makeRelicPath;

public class BurningTome extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("BurningTome");

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
