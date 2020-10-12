package theShade.cards;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShade.DefaultMod;
import theShade.characters.TheDefault;

import java.util.Iterator;

import static theShade.DefaultMod.makeCardPath;

public class ShadeFatigue extends AbstractDynamicCard
{
    public static final String ID = DefaultMod.makeID(ShadeFatigue.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeFatigue.png");
    private static final CardRarity RARITY = CardRarity.CURSE; //  Up to you, I like auto-complete on these
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
    public void use(AbstractPlayer p, AbstractMonster m) {
//        if (this.dontTriggerOnUseCard) {
//            this.addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 2, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
//        }
    }

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