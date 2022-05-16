package theShadeThatFades.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ElectroPower;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.actions.LightningAction;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.cards.ShadeSpite;
import theShadeThatFades.characters.TheShade;

import java.util.Iterator;

import static com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT;
import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeBlackLightning extends AbstractDynamicCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with ShadeArcaneLightning
     * And then you can do custom ones like 8 and ALL_ENEMY if you want.
     * I'll leave some comments on things you might consider replacing with what.
     *
     * Of course, delete all the comments and add anything you want (For example, if you're making a skill card template you'll
     * likely want to replace that new DamageAction with a gain Block one, and add baseBlock instead, or maybe you want a
     * universal template where you delete everything unnecessary - up to you)
     *
     * You can create templates for anything you ever want to. Cards, relics, powers, orbs, etc. etc. etc.
     */

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeBlackLightning.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeBlackLightning.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 2;

    private static final int DAMAGE = 8;
    private static final int UPGRADED_BONUS_DAMAGE = 2;
    private boolean descriptionUpdated = false;

    // /STAT DECLARATION/
    public static final Color BLACK_LIGHTNING_COLOR = CardHelper.getColor(0.0f, 0.0f, 0.0f); // Black
    private boolean target_all;

    public ShadeBlackLightning() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = 0;
        target_all = false;
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
            rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
            descriptionUpdated = true;
            initializeDescription();
        } else if (descriptionUpdated && CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase != COMBAT) {
            rawDescription = DESCRIPTION;
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
        if(p.hasPower(ElectroPower.POWER_ID)) {
            target_all = true;
        }
        else {
            target_all = false;
        }
        for(int i = 0; i < num_curses; ++i) {
            this.addToBot(new LightningAction(this, DamageInfo.DamageType.NORMAL, p, null, !target_all, target_all, BLACK_LIGHTNING_COLOR));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADED_BONUS_DAMAGE);
            initializeDescription();
        }
    }
}
