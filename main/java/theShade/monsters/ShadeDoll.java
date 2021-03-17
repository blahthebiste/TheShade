package theShade.monsters;

import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.actions.utility.*;
import com.megacrit.cardcrawl.actions.*;
import com.badlogic.gdx.graphics.*;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.badlogic.gdx.math.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.powers.MinionPower;

public class ShadeDoll extends AbstractMonster
{
    public static final String ID = "Shade:ShadeDoll";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private int count;

    public AbstractCreature victim;

    public ShadeDoll(final float x, final float y, AbstractCreature victim, float hp_multi) {
        super(ShadeDoll.NAME, ID, 50, 0,0, 160.0f, 160.0f, "theShadeResources/images/monsters/ShadeDoll.png", x -160, y +160);
        System.out.println("Doll draw x/y: "+this.drawX+"/"+this.drawY);
        this.victim = victim;
        if(victim != null) {
            AbstractCreature master = victim;
            while (master.hasPower(MinionPower.POWER_ID)) {
                // Can't minion to a minion, so get its master instead
                master = master.getPower(MinionPower.POWER_ID).owner;
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MinionPower(master)));
            this.maxHealth = Math.max((int) (victim.maxHealth*hp_multi/100.0), 1);
            this.currentHealth = Math.max((int) (victim.currentHealth*hp_multi/100.0), 1);
        }
        else {
            this.maxHealth = 50;
            this.currentHealth = 50;
        }
        this.count = 1;
        this.damage.add(new DamageInfo(this, 8));
    }

    @Override
    public void takeTurn() {
        if (victim.isDeadOrEscaped() && !this.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
        }
    }

    @Override
    public void update() {
        if (victim.isDeadOrEscaped() && !this.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
        }
        super.update();
        if (this.hb_x != victim.hb_x || this.hb_y != victim.hb_y) {
            // Smart movement code here
        }
    }

    @Override
    protected void getMove(final int num) {
        this.setMove((byte)2, Intent.NONE);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("theShade:ShadeDoll");
        NAME = ShadeDoll.monsterStrings.NAME;
        MOVES = ShadeDoll.monsterStrings.MOVES;
        DIALOG = ShadeDoll.monsterStrings.DIALOG;
    }
}