package theShade.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import infinitespire.InfiniteSpire;
import infinitespire.helpers.CardHelper;
import java.util.Iterator;

public class TransformIntoBlackCardAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private boolean canPickZero;
    private boolean canChoose;
    public static int numTransformed;

    public TransformIntoBlackCardAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero, boolean canChoose) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.canPickZero = canPickZero;
        this.canChoose = canChoose;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        System.out.println("Transforming " + this.amount + " cards into Black cards.");
    }

    public TransformIntoBlackCardAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber) {
        this(amount, isRandom, anyNumber);
        this.target = target;
        this.source = source;
    }

    public TransformIntoBlackCardAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom) {
        this(amount, isRandom, false, false);
        this.target = target;
        this.source = source;
    }

    public TransformIntoBlackCardAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this(amount, isRandom, anyNumber, canPickZero);
        this.target = target;
        this.source = source;
    }

    public TransformIntoBlackCardAction(boolean isRandom, boolean anyNumber, boolean canPickZero, boolean canChoose) {
        this(99, isRandom, anyNumber, canPickZero, canChoose);
    }

    public TransformIntoBlackCardAction(int amount, boolean canPickZero, boolean canChoose) {
        this(amount, false, false, canPickZero, canChoose);
    }

    public TransformIntoBlackCardAction(int amount, boolean isRandom, boolean anyNumber, boolean canChoose) {
        this(amount, isRandom, anyNumber, false, canChoose);
    }

    public TransformIntoBlackCardAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero, boolean canChoose, float duration) {
        this(amount, isRandom, anyNumber, canPickZero, canChoose);
        this.duration = this.startDuration = duration;
    }


    private void transformEntireHand() {
        int s = this.p.hand.size();
        for(int i = 0; i < s; ++i) {
            AbstractCard c = this.p.hand.getTopCard();
            transformSpecificCard(c);
        }
    }

    private void transformSpecificCard(AbstractCard c) {
        AbstractCard blackCard = CardHelper.getRandomBlackCard().makeStatEquivalentCopy();
        blackCard.current_x = blackCard.target_x = c.current_x;
        blackCard.current_y = blackCard.target_y = c.current_y;
        this.p.hand.removeCard(c);
        blackCard.superFlash(new Color(124.0f, 0.0f, 60.0f, 185.0f));
        this.p.hand.addToBottom(blackCard);
        this.p.hand.refreshHandLayout();
        this.p.hand.update();
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }

            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                numTransformed = this.amount;
                transformEntireHand();
                this.isDone = true;
                return;
            }

            if (!this.isRandom) {
                if (this.canChoose) {
                    numTransformed = this.amount;
                    AbstractDungeon.handCardSelectScreen.open("transform into Black cards.", this.amount, this.anyNumber, this.canPickZero);
                    this.tickDuration();
                }
                else {
                    for(int i = 0; i < this.amount; ++i) {
                        AbstractCard c = this.p.hand.getTopCard();
                        transformSpecificCard(c);
                        this.isDone = true;
                    }
                }
                return;
            }
            else {
                for (int i = 0; i < this.amount; ++i) {
                    AbstractCard c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                    transformSpecificCard(c);
                    this.isDone = true;
                }
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator var4 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while(var4.hasNext()) {
                AbstractCard c = (AbstractCard)var4.next();
                transformSpecificCard(c);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }
}
