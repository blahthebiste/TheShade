package theShadeThatFades.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.util.TextureLoader;

import static theShadeThatFades.TheShadeMod.makeRelicOutlinePath;
import static theShadeThatFades.TheShadeMod.makeRelicPath;

public class FifthShadow extends CustomRelic {

    // ID, images, text.
    public static final String ID = TheShadeMod.makeID("FifthShadow");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("FifthShadow.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("sorcery.png"));

    public FifthShadow() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.FLAT);
    }


    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        ++this.counter;
        if (this.counter == 5) {
            this.beginLongPulse();
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePlayerPower(AbstractDungeon.player, 1), 1));
            this.stopPulse();
            this.grayscale = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
