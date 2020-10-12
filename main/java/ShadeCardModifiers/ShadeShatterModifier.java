package ShadeCardModifiers;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShadeShatterModifier extends AbstractCardModifier {
    private int multi;
    public ShadeShatterModifier(int multi) {
        this.multi = multi;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return (card.type == AbstractCard.CardType.ATTACK);
    }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        System.out.println("Target has "+target.currentBlock+" block.");
        return damage + target.currentBlock;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ShadeShatterModifier(this.multi);
    }
}
