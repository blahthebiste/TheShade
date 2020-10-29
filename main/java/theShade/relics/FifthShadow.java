package theShade.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShade.DefaultMod;
import theShade.powers.ShadeCorruptionPower;
import theShade.util.TextureLoader;

import static theShade.DefaultMod.makeRelicOutlinePath;
import static theShade.DefaultMod.makeRelicPath;

public class FifthShadow extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("FifthShadow");

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
