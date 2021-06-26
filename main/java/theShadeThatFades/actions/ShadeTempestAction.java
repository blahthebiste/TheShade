package theShadeThatFades.actions;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.ElectroPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class ShadeTempestAction extends AbstractGameAction {
    private boolean freeToPlayOnce = false;
    private AbstractPlayer p;
    private int energyOnUse = -1;
    private boolean upgraded;
    private int damage;
    private boolean target_all;

    public ShadeTempestAction(AbstractPlayer p, int damageAmount, int energyOnUse, boolean upgraded, boolean freeToPlayOnce) {
        this.p = p;
        this.damage = damageAmount;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        this.freeToPlayOnce = freeToPlayOnce;
        this.target_all = false;
    }

    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (this.upgraded) {
            ++effect;
        }

        if (effect > 0) {
//            AbstractCard tmpLightningCard = new ShadeChaosStormTmpLightningCard(this.damage);
            if(p.hasPower(ElectroPower.POWER_ID)) {
                target_all = true;
            }
            else {
                target_all = false;
            }
            for(int i = 0; i < effect; ++i) {
                    this.addToBot(new LightningAction(this.damage, DamageInfo.DamageType.NORMAL, p, null, !target_all, target_all));
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
