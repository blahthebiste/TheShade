package theShade.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import theShade.DefaultMod;
import theShade.cards.ShadeFatigue;
import theShade.util.TextureLoader;

import java.util.Iterator;

import static theShade.DefaultMod.makeRelicOutlinePath;
import static theShade.DefaultMod.makeRelicPath;

public class TitanTome extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("TitanTome");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("TitanTome.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("TitanTome.png"));

    public TitanTome() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
    }

    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        ++this.counter;
        if (this.counter > 7) {
            this.beginLongPulse();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster mo = (AbstractMonster) var1.next();
                this.addToBot(new HealAction(mo, AbstractDungeon.player, 50));
            }
            this.stopPulse();
        }
        this.flash();
    }

    public void onEquip() {
        ++AbstractDungeon.player.masterHandSize;
    }

    public void onUnequip() {
        --AbstractDungeon.player.masterHandSize;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
