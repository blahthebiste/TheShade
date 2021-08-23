package theShadeThatFades.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;
import theShadeThatFades.powers.ShadeSubversionPower;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeSubversion extends AbstractDynamicCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with ShadeSubversion
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

    public static final String ID = TheShadeMod.makeID(ShadeSubversion.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeSubversion.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 1;
    private static final int DURATION = 1;
    private static final int UPGRADE_DURATION = 1;

    // /STAT DECLARATION/
    public boolean isAnyTarget = true;
    private boolean targetingEnemy = false;



    public ShadeSubversion() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        baseMagicNumber = magicNumber = DURATION;
        this.tags.add(CardTags.HEALING);
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
        this.addToBot(new ApplyPowerAction(target, p, new ShadeSubversionPower(target, magicNumber), magicNumber));
    }

    // Code for choosing any target, player or monster
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
            upgradeMagicNumber(UPGRADE_DURATION);
            initializeDescription();
        }
    }
}
