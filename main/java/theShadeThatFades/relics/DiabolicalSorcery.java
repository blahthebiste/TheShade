package theShadeThatFades.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.powers.ShadeCorruptionPower;
import theShadeThatFades.util.TextureLoader;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makeRelicOutlinePath;
import static theShadeThatFades.TheShadeMod.makeRelicPath;

public class DiabolicalSorcery extends CustomRelic {

    // ID, images, text.
    public static final String ID = TheShadeMod.makeID("DiabolicalSorcery");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("diabolicalSorcery.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("diabolicalSorcery.png"));

    public DiabolicalSorcery() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    // Apply 1 ShadeCorruption to everyone at the start of every turn.
    @Override
    public void atTurnStart() {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ShadeCorruptionPower(AbstractDungeon.player, 1), 1));
        Iterator var3 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var3.hasNext()) {
            AbstractMonster monster = (AbstractMonster)var3.next();
            if (!monster.isDead && !monster.isDying) {
                this.addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new ShadeCorruptionPower(monster, 1), 1));
            }
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(TwistedSorcery.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(TwistedSorcery.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(TwistedSorcery.ID);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
