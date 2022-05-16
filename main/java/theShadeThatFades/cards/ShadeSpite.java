package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.characters.TheShade;

import static com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT;
import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeSpite extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeSpite.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeSpite.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 1;

    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/


    public ShadeSpite() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public void update() {
        if ( CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == COMBAT && this.color != CardColor.CURSE) {
            changeColorAndTypeToCurse();
        } else if (CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase != COMBAT  && this.color == CardColor.CURSE) {
            resetColorAndType();
        }
        super.update();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    private void changeColorAndTypeToCurse() {
        this.color = CardColor.CURSE;
        this.type = CardType.CURSE;
    }
    private void resetColorAndType() {
        this.color = COLOR; // defaultGray
        this.type = TYPE; // Attack
    }

    @Override
    public boolean canUpgrade() {
        if (this.color == CardColor.CURSE || this.type == CardType.CURSE){
            return !this.upgraded; // This is a curse, but also an attack that can be upgraded.
        }
        else {
            return super.canUpgrade();
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
