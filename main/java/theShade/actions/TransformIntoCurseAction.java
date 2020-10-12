package theShade.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class TransformIntoCurseAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private boolean canPickZero;
    private boolean canChoose;
    public static int numTransformed;

    public TransformIntoCurseAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero, boolean canChoose) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.canPickZero = canPickZero;
        this.canChoose = canChoose;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public TransformIntoCurseAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber) {
        this(amount, isRandom, anyNumber);
        this.target = target;
        this.source = source;
    }

    public TransformIntoCurseAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom) {
        this(amount, isRandom, false, false);
        this.target = target;
        this.source = source;
    }

    public TransformIntoCurseAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this(amount, isRandom, anyNumber, canPickZero);
        this.target = target;
        this.source = source;
    }

    public TransformIntoCurseAction(boolean isRandom, boolean anyNumber, boolean canPickZero, boolean canChoose) {
        this(99, isRandom, anyNumber, canPickZero, canChoose);
    }

    public TransformIntoCurseAction(int amount, boolean canPickZero, boolean canChoose) {
        this(amount, false, false, canPickZero, canChoose);
    }

    public TransformIntoCurseAction(int amount, boolean isRandom, boolean anyNumber, boolean canChoose) {
        this(amount, isRandom, anyNumber, false, canChoose);
    }

    public TransformIntoCurseAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero, boolean canChoose, float duration) {
        this(amount, isRandom, anyNumber, canPickZero, canChoose);
        this.duration = this.startDuration = duration;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }

            int s;
            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                numTransformed = this.amount;
                s = this.p.hand.size();

                for(int i = 0; i < s; ++i) {
                    AbstractCard c = this.p.hand.getTopCard();
                    AbstractCard curseCard = AbstractDungeon.returnRandomCurse();
                    curseCard.current_x = curseCard.target_x = c.current_x;
                    curseCard.current_y = curseCard.target_y = c.current_y;
                    this.p.hand.removeCard(c);
                    curseCard.superFlash(CardHelper.getColor(124.0f, 0.0f, 60.0f));
                    this.p.hand.addToBottom(curseCard);
                    this.p.hand.refreshHandLayout();
                    this.p.hand.update();
                }
                this.isDone = true;
                return;
            }

            if (!this.isRandom) {
                if (this.canChoose) {
                    numTransformed = this.amount;
                    AbstractDungeon.handCardSelectScreen.open("Transform cards into Curses.", this.amount, this.anyNumber, this.canPickZero);
                    this.tickDuration();
                }
                else {
                    for(int i = 0; i < this.amount; ++i) {
                        AbstractCard c = this.p.hand.getTopCard();
                        AbstractCard curseCard = AbstractDungeon.returnRandomCurse();
                        curseCard.current_x = curseCard.target_x = c.current_x;
                        curseCard.current_y = curseCard.target_y = c.current_y;
                        this.p.hand.removeCard(c);
                        curseCard.superFlash();
                        this.p.hand.addToBottom(curseCard);
                        this.p.hand.refreshHandLayout();
                        this.p.hand.update();
                    }
                }
                this.isDone = true;
                return;
            }

            for(int i = 0; i < this.amount; ++i) {
                AbstractCard c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                AbstractCard curseCard = AbstractDungeon.returnRandomCurse();
                curseCard.current_x = curseCard.target_x = c.current_x;
                curseCard.current_y = curseCard.target_y = c.current_y;
                this.p.hand.removeCard(c);
                curseCard.superFlash();
                this.p.hand.addToBottom(curseCard);
                this.p.hand.refreshHandLayout();
                this.p.hand.update();
            }

        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator var4 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while(var4.hasNext()) {
                AbstractCard c = (AbstractCard)var4.next();
                AbstractCard curseCard = AbstractDungeon.returnRandomCurse();
                curseCard.current_x = curseCard.target_x = c.current_x;
                curseCard.current_y = curseCard.target_y = c.current_y;
                this.p.hand.removeCard(c);
                curseCard.superFlash();
                this.p.hand.addToBottom(curseCard);
                this.p.hand.refreshHandLayout();
                this.p.hand.update();
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }
}
