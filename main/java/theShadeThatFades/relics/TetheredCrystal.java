package theShadeThatFades.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.util.TextureLoader;

import static theShadeThatFades.TheShadeMod.makeRelicOutlinePath;
import static theShadeThatFades.TheShadeMod.makeRelicPath;

public class TetheredCrystal extends CustomRelic {

    // ID, images, text.
    public static final String ID = TheShadeMod.makeID("TetheredCrystal");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("TetheredCrystal_smol.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("TetheredCrystal_smol.png"));
    private boolean isPlayerTurn;

    public TetheredCrystal() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
    }

    @Override
    public void onEnergyRecharge() {
        isPlayerTurn = true;
    }

    @Override
    public void wasHPLost(int damageAmount)
    { // Track HP lost during player turn
        if (damageAmount > 0) {
            if (isPlayerTurn) {
                this.flash();
                this.counter += damageAmount;
            }
        }
    }

    @Override
    public void onVictory() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0 && this.counter > 0) {
            p.heal(this.counter); // Heal it back at the end of combat
            this.counter = 0;
        }

    }
}
