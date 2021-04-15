package theShadeThatFades.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;

import static com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT;
import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeMutualAnnihilation extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeMutualAnnihilation.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeMutualAnnihilation.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_GRAY;

    private static final int COST = 1;

    private static final int DAMAGE = 80;
    private static final int UPGRADE_PLUS_DMG = 20;

    private static final int DMG_THRESHOLD = 13;

    private boolean descriptionUpdated = false;
    // /STAT DECLARATION/


    public ShadeMutualAnnihilation() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = DMG_THRESHOLD;
        this.selfRetain = true;
    }

    @Override
    public void update() {
        if (CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == COMBAT) {
//            System.out.println("damagedThisCombat: "+ AbstractDungeon.player.damagedThisCombat);
            baseMagicNumber = magicNumber = Math.max((DMG_THRESHOLD - AbstractDungeon.player.damagedThisCombat),0);
            rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
            descriptionUpdated = true;
            initializeDescription();
            this.selfRetain = true;
        } else if (descriptionUpdated && CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase != COMBAT) {
            rawDescription = DESCRIPTION;
            descriptionUpdated = false;
            initializeDescription();
            this.selfRetain = true;
        }
        super.update();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player.damagedThisCombat >= DMG_THRESHOLD) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(p, baseDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SMASH));
        }
    }

//    @Override
//    public void triggerOnEndOfPlayerTurn() {
//       super.triggerOnEndOfPlayerTurn();
//        if (GameActionManager.damageReceivedThisTurn > 0) {
//            this.addToBot(new ReduceCostAction(this));
//        }
//    }

//    @Override
//    public void tookDamage() {
//        this.flash();
//        this.addToBot(new ReduceCostAction(this));
//    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.player.damagedThisCombat >= DMG_THRESHOLD) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
//            upgradeMagicNumber(UPGRADE_PLUS_EXTRA_DMG);
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
            this.selfRetain = true;
        }
    }
}
