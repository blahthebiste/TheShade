package ShadeCardModifiers;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShadeShatterModifier extends AbstractCardModifier {
    private int multi;
//    private boolean usedUp;

    public ShadeShatterModifier(int multi) {
        this.multi = multi;
//        this.usedUp = false;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return (card.type == AbstractCard.CardType.ATTACK);
    }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (target != null) {
//            System.out.println("Target has " + target.currentBlock + " block.");
            return damage + (target.currentBlock * multi);
        }
        else {
//            System.out.println("Target is null");
            return damage;
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ShadeShatterModifier(this.multi);
    }
}
