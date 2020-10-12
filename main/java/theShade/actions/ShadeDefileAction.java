package theShade.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

public class ShadeDefileAction extends AbstractGameAction {
    private AbstractPlayer p;
    private AbstractMonster m;
    private DamageInfo dmgInfo;

    public ShadeDefileAction(final AbstractPlayer p, final AbstractMonster m, final DamageInfo dmgInfo) {
        this.p = p;
        this.m = m;
        this.dmgInfo = dmgInfo;
        actionType = ActionType.DAMAGE;
    }
    
    @Override
    public void update() {
//        AbstractDungeon.actionManager.cleanCardQueue();
//        if (this.p.hand.group.isEmpty()) {
//            this.isDone = true;
//        } else {
//            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
//            tmp.group.addAll(this.p.hand.group);
//            this.p.hand.clear();
//            Iterator var2 = tmp.group.iterator();
//
//            while (var2.hasNext()) {
//                var2.next();
//                AbstractCard curseCard = AbstractDungeon.returnRandomCurse();
//                this.p.hand.addToTop(curseCard);
//                this.p.hand.refreshHandLayout();
//                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(this.p, this.dmgInfo.base, this.damageType, AttackEffect.FIRE));
//            }
//        }
        int count = AbstractDungeon.player.hand.size();

        int i;
        for(i = 0; i < count; ++i) {
            this.addToTop(new DamageAllEnemiesAction(this.p, this.dmgInfo.base, this.damageType, AttackEffect.FIRE));
        }

        for(i = 0; i < count; ++i) {
            if (Settings.FAST_MODE) {
                this.addToTop(new TransformIntoCurseAction(1, false, true, false, false, Settings.ACTION_DUR_XFAST));
            } else {
                this.addToTop(new TransformIntoCurseAction(1, false, true, false));
            }
        }
        isDone = true;
    }

}
