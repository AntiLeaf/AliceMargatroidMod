package me.antileaf.alice.powers.medicine;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.action.medicine.MedicineApplyPoisonAction;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineEnvenomPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = MedicineEnvenomPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public MedicineEnvenomPower(AbstractCreature owner, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		
		this.updateDescription();
		this.loadRegion("envenom");
		this.type = PowerType.BUFF;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
	@Override
	public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
		if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
			this.flash();
			this.addToTop(new ApplyPowerAction(target, this.owner,
					new MedicinePoisonPower(target, this.owner, this.amount), this.amount, true));
		}
	}
	
	public void envenom(int index) {
		AliceHelper.addToTop(new MedicineApplyPoisonAction(index, this.owner, this.amount));
	}
}
