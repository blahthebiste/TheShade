package theShadeThatFades.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theShadeThatFades.powers.ShadeInfusionPower;
import theShadeThatFades.powers.ShadeInfusionUpgradedPower;

public class ShadeInfusionTickAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;
    private boolean isUpgraded;

    public ShadeInfusionTickAction(AbstractCreature owner, boolean isUpgraded) {
        this.target = owner;
        this.isUpgraded = isUpgraded;
        actionType = ActionType.REDUCE_POWER;
        this.duration = DURATION;
    }
    
    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
                AbstractPower p;
                if (!isUpgraded) {
                    p = this.target.getPower(ShadeInfusionPower.POWER_ID);
                    if (p != null) {
                        p.reducePower(1);
                        ((ShadeInfusionPower)p).isEnemyTurn = false;
                    }
                }
                else {
                    p = this.target.getPower(ShadeInfusionUpgradedPower.POWER_ID);
                    if (p != null) {
                        p.reducePower(1);
                        ((ShadeInfusionUpgradedPower)p).isEnemyTurn = false;
                    }
                }



                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
