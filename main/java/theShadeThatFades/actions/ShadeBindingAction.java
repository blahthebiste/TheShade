package theShadeThatFades.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.powers.ShadeHpTetherPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShadeBindingAction extends AbstractGameAction {
    private AbstractCreature target;

    public ShadeBindingAction(final AbstractCreature target) {
        this.target = target;
        actionType = ActionType.DEBUFF;
    }
    
    @Override
    public void update() {
        // TODO: Chain animation
        AbstractPlayer p = AbstractDungeon.player;
        if(!target.equals(p)) {
            this.addToBot(new ApplyPowerAction(p, p, new ShadeHpTetherPower(p, target)));
        }
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();
        while(var1.hasNext()) {
            AbstractMonster monster = (AbstractMonster) var1.next();
            // For each monster that is not the target, tether them to the target
            if (!monster.isDead && !monster.isDying && monster.currentHealth > 0 && !monster.equals(target)) {
                this.addToBot(new ApplyPowerAction(monster, p, new ShadeHpTetherPower(monster, target)));
                System.out.println(monster.currentBlock); // For debug
                if (!Settings.FAST_MODE) {
                    this.addToTop(new WaitAction(0.1F));
                }
            }
        }
        isDone = true;
    }

}
