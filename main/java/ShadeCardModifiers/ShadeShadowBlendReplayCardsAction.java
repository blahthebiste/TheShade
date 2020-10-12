package ShadeCardModifiers;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShadeShadowBlendReplayCardsAction extends AbstractGameAction {

    private LinkedHashMap<AbstractCard, AbstractMonster> dupedCards;

    public ShadeShadowBlendReplayCardsAction(LinkedHashMap<AbstractCard, AbstractMonster> dupedCards)
    {
        duration = Settings.ACTION_DUR_MED;
        this.dupedCards = dupedCards;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            for (Map.Entry<AbstractCard, AbstractMonster> cardTargetPair : dupedCards.entrySet()) {
                AbstractMonster targetMonster = cardTargetPair.getValue();
                AbstractCard tmp = cardTargetPair.getKey();
                tmp.current_x = tmp.target_x = Settings.WIDTH / 2.0f - 300.0f * Settings.scale;
                tmp.current_y = tmp.target_y = Settings.HEIGHT / 2.0f;
                if (targetMonster != null) {
                    tmp.calculateCardDamage(targetMonster);
                }
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, targetMonster, tmp.energyOnUse, true, true));
                System.out.println("Played " + tmp.cardID);
            }
        }
        tickDuration();
    }
}
