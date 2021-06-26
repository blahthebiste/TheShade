package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeBurnPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeArcaneFire extends AbstractDynamicCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with ShadeArcaneFire
     * And then you can do custom ones like  and  if you want.
     * I'll leave some comments on things you might consider replacing with what.
     *
     * Of course, delete all the comments and add anything you want (For example, if you're making a skill card template you'll
     * likely want to replace that new DamageAction with a gain Block one, and add baseBlock instead, or maybe you want a
     * universal template where you delete everything unnecessary - up to you)
     *
     * You can create templates for anything you ever want to. Cards, relics, powers, orbs, etc. etc. etc.
     */

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeArcaneFire.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeArcaneFire.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 0;
    public boolean isAnyTarget = true;
    private boolean targetingEnemy = false;
    private static final int BURN_AMOUNT = 5;
    // /STAT DECLARATION/


    public ShadeArcaneFire() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = BURN_AMOUNT;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target;
        if(m != null) {
            target = m;
        }else{
            target = p;
        }
        this.addToBot(new ApplyPowerAction(target, p, new ShadeBurnPower(target, p, 5), 5));
        if(upgraded) {
            this.addToBot(new DrawCardAction(p, 1));
        }
    }

//    // WIP Newer upgrade function with enemy as the default target and player as the hover target
//    @Override
//    public void update() {
//        super.update();
//        if(isAnyTarget && AbstractDungeon.player != null) {
////            System.out.println("DEBUGFORELI: Hurdle 1. Printing player hitbox info");
//            AbstractPlayer p = AbstractDungeon.player;
//            if ( p.isDraggingCard) {
//                if( p.hoveredCard.equals(this)) {
//                    this.playerUsingThis = true;
//                }
//                else{
//                    this.playerUsingThis = false;
//                }
//            }
////            System.out.printf("DEBUGFORELI: p.hb.cX: %.2f  p.hb.cY: %.2f  p.hb.width: %.2f  p.hb.height: %.2f\n",p.hb.cX,p.hb.cY,p.hb.width,p.hb.height);
////            System.out.printf("DEBUGFORELI: this.current_x: %.2f  this.current_y: %.2f\n",);
//            if (p.isHoveringDropZone && this.playerUsingThis) {//
//                System.out.println("DEBUGFORELI: mouse in drop zone");
//                if ((InputHelper.mX > p.hb.x && InputHelper.mX < p.hb.x + p.hb.width) && (InputHelper.mY > p.hb.y && InputHelper.mY < p.hb.y + p.hb.height)) {
//                    System.out.println("DEBUGFORELI: Hovering over player!");
//                    if(!this.targetingPlayer) {
//                        System.out.println("DEBUGFORELI: Change to self targeting");
//                        this.targetingPlayer = true;
//                        p.inSingleTargetMode = false;
//                        this.target = CardTarget.SELF;
////                        this.target_x = p.hb.cX - this.hb.width * 0.6F - p.hb_w * 0.6F;
////                        this.target_y = p.hb.cY;
//                        this.applyPowers();
//                    }
//                } else {
//                    System.out.println("DEBUGFORELI: mX: "+InputHelper.mX);
//                    System.out.println("DEBUGFORELI: mY: "+InputHelper.mY);
//                    System.out.printf("DEBUGFORELI: p.hb.cX: %.2f  p.hb.cY: %.2f  p.hb.width: %.2f  p.hb.height: %.2f\n",p.hb.cX,p.hb.cY,p.hb.width,p.hb.height);
//
//                    if(this.targetingPlayer) {
//                        System.out.println("DEBUGFORELI: NOT hovering over player, change to enemy targeting");
//                        this.targetingPlayer = false;
//                        p.inSingleTargetMode = true;
//                        this.target_x = p.hb.cX - this.hb.width * 0.6F - p.hb_w * 0.6F;
//                        this.target_y = p.hb.cY;
//                        this.target = CardTarget.ENEMY;
//                        this.applyPowers();
//                    }
//                }
//            }else if(this.targetingPlayer){
//                System.out.println("DEBUGFORELI: NOT hovering at all, change to enemy targeting");
//                this.targetingPlayer = false;
//                p.inSingleTargetMode = true;
//                this.target_x = p.hb.cX - this.hb.width * 0.6F - p.hb_w * 0.6F;
//                this.target_y = p.hb.cY;
//                this.target = CardTarget.ENEMY;
//            }
//        }
//    }

    // Original update func vvv

    @Override
    public void update() {
        super.update();
        if(isAnyTarget && AbstractDungeon.player != null) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.isDraggingCard && p.hoveredCard.equals(this)) {
                AbstractMonster hoveredEnemy = null;
                for (AbstractMonster enemy : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (enemy.hb.hovered && !enemy.isDead && !enemy.halfDead) {
                        hoveredEnemy = enemy;
                    }
                }
                if (hoveredEnemy != null) {
                    if(!this.targetingEnemy) {
                        this.targetingEnemy = true;
                        p.inSingleTargetMode = true;
                        this.target = CardTarget.ENEMY;
                        this.target_x = hoveredEnemy.hb.cX - this.hb.width * 0.6F - hoveredEnemy.hb_w * 0.6F;
                        this.target_y = hoveredEnemy.hb.cY;
                        this.applyPowers();
                    }
                } else {
                    if(this.targetingEnemy) {
                        this.targetingEnemy = false;
                        p.inSingleTargetMode = false;
                        this.target = CardTarget.SELF;
                        this.applyPowers();
                    }
                }
            }else if(this.targetingEnemy){
                this.targetingEnemy = false;
                this.target = CardTarget.SELF;
            }
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
