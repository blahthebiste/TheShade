package theShadeThatFades.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeFalteringBlade extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeFalteringBlade.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeFalteringBlade.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 0;

    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/


    public ShadeFalteringBlade() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = 0;
    }


    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        if (AbstractDungeon.player.hasPower("theShadeThatFades:Corruption")) {
            this.baseMagicNumber = AbstractDungeon.player.getPower("theShadeThatFades:Corruption").amount;
        }
        else {
            this.baseMagicNumber = 0;
        }
        this.baseDamage -= 2*this.baseMagicNumber;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    public void calculateCardDamage(AbstractMonster mo) {
        if (AbstractDungeon.player.hasPower("theShadeThatFades:Corruption")) {
            this.baseMagicNumber = AbstractDungeon.player.getPower("theShadeThatFades:Corruption").amount;
        }
        else {
            this.baseMagicNumber = 0;
        }
        int realBaseDamage = this.baseDamage;
        this.baseDamage -= 2*this.baseMagicNumber;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.damage -= 2*this.magicNumber;
        this.calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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
