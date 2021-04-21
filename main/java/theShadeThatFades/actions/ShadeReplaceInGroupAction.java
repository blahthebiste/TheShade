package theShadeThatFades.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ShadeReplaceInGroupAction extends AbstractGameAction {
    private AbstractCard cardToReplace;
    private AbstractCard replacement;
    private CardGroup group;

    public ShadeReplaceInGroupAction(AbstractCard cardToReplace, AbstractCard replacement, CardGroup group) {
       this.cardToReplace = cardToReplace;
       this.replacement = replacement;
       this.group = group;
       this.actionType = ActionType.CARD_MANIPULATION;
       this.duration = Settings.ACTION_DUR_FASTER;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FASTER && this.cardToReplace != null && this.replacement != null && this.group != null) {
            replacement.current_x = replacement.target_x = cardToReplace.current_x;
            replacement.current_y = replacement.target_y = cardToReplace.current_y;
            group.removeCard(cardToReplace);
            group.addToBottom(replacement);
            if(group.equals(AbstractDungeon.player.hand)) {
                group.refreshHandLayout();
                replacement.superFlash();
                group.update();
            }
        }
        else {
            System.out.println("Got some nulls");
        }

        this.tickDuration();
    }
}
