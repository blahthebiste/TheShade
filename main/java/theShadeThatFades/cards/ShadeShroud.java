package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeWitherPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeShroud extends AbstractDynamicCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with ShadeShroud
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

    public static final String ID = TheShadeMod.makeID(ShadeShroud.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeShroud.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 2;

    private static final int BLOCK = 12;
    private static final int UPGRADE_PLUS_BLK = 2;
    private static final int WITHER_AMOUNT = 3;
    private static final int WITHER_AMOUNT_INCREASE = 1;
    // /STAT DECLARATION/

    public boolean isAnyTarget = true;
    private boolean targetingEnemy = false;

    public ShadeShroud() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = WITHER_AMOUNT;
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
        this.addToBot(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, p, new ShadeWitherPower(target, magicNumber), magicNumber));
    }

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
            upgradeBlock(UPGRADE_PLUS_BLK);
            upgradeMagicNumber(WITHER_AMOUNT_INCREASE);
            initializeDescription();
        }
    }
}
