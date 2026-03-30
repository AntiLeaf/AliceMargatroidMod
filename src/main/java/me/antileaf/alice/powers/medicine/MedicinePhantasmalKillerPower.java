package me.antileaf.alice.powers.medicine;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.DoubleDamagePower;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicinePhantasmalKillerPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = MedicinePhantasmalKillerPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	private boolean justApplied = true;
	
	public MedicinePhantasmalKillerPower(AbstractCreature owner, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		
		this.updateDescription();
		this.loadRegion("phantasmal");
		this.type = PowerType.BUFF;
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}
	
	@Override
	public float atDamageGive(float damage, DamageInfo.DamageType type) {
		if (this.justApplied || this.owner.hasPower(DoubleDamagePower.POWER_ID))
			return damage;
		
		return type == DamageInfo.DamageType.NORMAL ? damage * 2.0F : damage;
	}
	
	@Override
	public void atStartOfTurn() {
		this.flash();
		this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DoubleDamagePower(this.owner, 1, false), this.amount));
		this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
	}
	
	@Override
	public void atEndOfRound() {
		this.justApplied = false;
	}
}
