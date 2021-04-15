package theShadeThatFades.cards;

import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShadeThatFades.TheShadeMod;

import java.util.Iterator;

import static theShadeThatFades.TheShadeMod.makeCardPath;

public class ShadeFatigue extends AbstractDynamicCard
{
    public static final String ID = TheShadeMod.makeID(ShadeFatigue.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeFatigue.png");
    private static final CardRarity RARITY = CardRarity.SPECIAL; //  Up to you, I like auto-complete on these
    private static final CardType TYPE = CardType.CURSE;       //
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = 1;

    public ShadeFatigue()
    {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, CardTarget.NONE);
        this.selfRetain = true;
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {    }

    @Override
    public void atTurnStart() {
        Iterator var2 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard c;
        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c == this) { // If we are the first Fatigue card, sap 1 energy
                this.addToBot(new LoseEnergyAction(1));
            }
            else if (c.cardID == this.cardID) {
                break; // If there is another Fatigue card before us, do nothing
            }
        }

    }

//    @Override
//    public void upgrade()
//    {
//
//    }
//
//    @Override
//    public AbstractCard makeCopy()
//    {
//        return new Hubris();
//    }
}