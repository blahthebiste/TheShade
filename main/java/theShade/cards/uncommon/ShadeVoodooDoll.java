package theShade.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theShade.DefaultMod;
import theShade.cards.AbstractDynamicCard;
import theShade.characters.TheShade;
import theShade.monsters.ShadeDoll;
import theShade.powers.ShadeHpTetherPower;

import static theShade.DefaultMod.makeCardPath;

public class ShadeVoodooDoll extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(ShadeVoodooDoll.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ShadeSoulBind.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheShade.Enums.COLOR_GRAY;

    private static final int COST = 1;

    private static final int HP_PERCENTAGE = 50;
    private static final int UPGRADE_HP_PERCENTAGE = 25;

    // /STAT DECLARATION/


    public ShadeVoodooDoll() { // - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = HP_PERCENTAGE;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null && !m.isDying && !m.isDeadOrEscaped()) {
            // Figure out where to spawn it
            System.out.println("Target draw x/y: "+m.drawX+"/"+m.drawY);
            ShadeDoll doll = new ShadeDoll((m.drawX - (float)Settings.WIDTH * 0.75F)/ Settings.scale, (m.drawY -AbstractDungeon.floorY )/Settings.scale, m, magicNumber);
            this.addToBot(new ApplyPowerAction(doll, p, new ShadeHpTetherPower(doll, m)));
            this.addToBot(new SpawnMonsterAction(doll, true));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_HP_PERCENTAGE);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
