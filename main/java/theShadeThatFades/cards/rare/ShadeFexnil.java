package theShadeThatFades.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.AbstractDynamicCard;
import theShadeThatFades.characters.TheShade;

import java.util.*;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeFexnil extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = TheShadeMod.makeID(ShadeFexnil.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeFexnil.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_SHADE_PURPLE;

    private static final int COST = 2;

    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 2;

    // /STAT DECLARATION/


    public ShadeFexnil() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    // Comparator for determining which monster is further to the left
    public static class xComparator implements Comparator<AbstractMonster> {
        @Override
        public int compare(AbstractMonster o1, AbstractMonster o2) {
            return (int) (o1.hb.cX - o2.hb.cX);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Order monsters from left to right
        int damageMultiplier = 1;
        List<AbstractMonster> orderedMonsters = new ArrayList<>();
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();
        while(var1.hasNext()) {
            AbstractMonster monster = (AbstractMonster) var1.next();
            if (!monster.isDead && !monster.isDying && monster.currentHealth > 0) {
                orderedMonsters.add(monster);
            }
        }
        orderedMonsters.sort(new xComparator());
        Iterator var2 = orderedMonsters.iterator();
        while(var2.hasNext()) {
            AbstractMonster target = (AbstractMonster) var2.next();
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(p, (damage * damageMultiplier), damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            damageMultiplier *= 2;
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
