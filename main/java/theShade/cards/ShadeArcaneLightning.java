package theShade.cards;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ElectroPower;
import theShade.DefaultMod;
import theShade.actions.LightningAction;
import theShade.characters.TheShade;

import static theShade.DefaultMod.makeCardPath;

public class ShadeArcaneLightning extends AbstractDynamicCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with ShadeArcaneLightning
     * And then you can do custom ones like 8 and ALL_ENEMY if you want.
     * I'll leave some comments on things you might consider replacing with what.
     *
     * Of course, delete all the comments and add anything you want (For example, if you're making a skill card template you'll
     * likely want to replace that new DamageAction with a gain Block one, and add baseBlock instead, or maybe you want a
     * universal template where you delete everything unnecessary - up to you)
     *
     * You can create templates for anything you ever want to. Cards, relics, powers, orbs, etc. etc. etc.
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(ShadeArcaneLightning.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeArcaneLightning.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_GRAY;

    private static final int COST = 2;

    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 2;

    private static final int NUM_LIGHTNING = 2;
//    private static final int UPGRADE_NUM_LIGHTNING = 1;

    // /STAT DECLARATION/
    private boolean target_all;

    public ShadeArcaneLightning() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = NUM_LIGHTNING;
        target_all = false;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasPower(ElectroPower.POWER_ID)) {
            target_all = true;
        }
        else {
            target_all = false;
        }
        for(int i = 0; i < magicNumber; ++i) {
            this.addToBot(new LightningAction(this.damage, DamageInfo.DamageType.NORMAL, p, null, !target_all, target_all));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
//            upgradeMagicNumber(UPGRADE_NUM_LIGHTNING);
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }
}
