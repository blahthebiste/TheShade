package theShadeThatFades.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.cards.ShadeSpite;
import theShadeThatFades.characters.TheShade;

import java.util.Iterator;

import static com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT;
import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeForbiddenKnowledge extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeForbiddenKnowledge.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeForbiddenKnowledge.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 0;
    private boolean descriptionUpdated = false;

    // /STAT DECLARATION/


    public ShadeForbiddenKnowledge() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        rawDescription = DESCRIPTION + " NL Exhaust.";
        this.initializeDescription();
    }

    @Override
    public void update() {
        if (CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == COMBAT && !AbstractDungeon.actionManager.turnHasEnded) {
            int num_curses = 0;
            Iterator var1 = AbstractDungeon.player.drawPile.group.iterator();
            // Count curses in draw pile
            while(var1.hasNext()) {
                AbstractCard c = (AbstractCard) var1.next();
                if (c.cardID == ShadeSpite.ID) {
                    c.update();
                }
                if (c.type == CardType.CURSE || c.color == CardColor.CURSE) {
                    ++num_curses;
                }
            }
            Iterator var2 = AbstractDungeon.player.hand.group.iterator();
            // Count curses in hand
            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard) var2.next();
                if (c.cardID == ShadeSpite.ID) {
                    c.update();
                }
                if (c.type == CardType.CURSE || c.color == CardColor.CURSE) {
                    ++num_curses;
                }
            }
            Iterator var3 = AbstractDungeon.player.discardPile.group.iterator();
            // Count curses in discard pile
            while(var3.hasNext()) {
                AbstractCard c = (AbstractCard) var3.next();
                if (c.cardID == ShadeSpite.ID) {
                    c.update();
                }
                if (c.type == CardType.CURSE || c.color == CardColor.CURSE) {
                    ++num_curses;
                }
            }
            baseMagicNumber = magicNumber = num_curses;
            if (this.upgraded) {
                rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
            }
            else {
                rawDescription = DESCRIPTION + " NL Exhaust." + UPGRADE_DESCRIPTION;
            }
            descriptionUpdated = true;
            initializeDescription();
        } else if (descriptionUpdated && CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase != COMBAT) {
            if (this.upgraded) {
                rawDescription = DESCRIPTION;
            }
            else {
                rawDescription = DESCRIPTION + " NL Exhaust.";
            }
            descriptionUpdated = false;
            initializeDescription();
        }
        super.update();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int num_curses = 0;
        Iterator var1 = AbstractDungeon.player.drawPile.group.iterator();
        // Count curses in draw pile
        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard) var1.next();
            if (c.cardID == ShadeSpite.ID) {
                c.update();
            }
            if (c.type == CardType.CURSE || c.color == CardColor.CURSE) {
                ++num_curses;
            }
        }
        Iterator var2 = AbstractDungeon.player.hand.group.iterator();
        // Count curses in hand
        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard) var2.next();
            if (c.cardID == ShadeSpite.ID) {
                c.update();
            }
            if (c.type == CardType.CURSE || c.color == CardColor.CURSE) {
                ++num_curses;
            }
        }
        Iterator var3 = AbstractDungeon.player.discardPile.group.iterator();
        // Count curses in discard pile
        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard) var3.next();
            if (c.cardID == ShadeSpite.ID) {
                c.update();
            }
            if (c.type == CardType.CURSE || c.color == CardColor.CURSE) {
                ++num_curses;
            }
        }
        this.addToBot(new DrawCardAction(num_curses));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            rawDescription = DESCRIPTION;
            initializeDescription();
        }
    }
}
