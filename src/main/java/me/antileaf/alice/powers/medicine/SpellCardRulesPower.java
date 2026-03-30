package me.antileaf.alice.powers.medicine;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class SpellCardRulesPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = SpellCardRulesPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public SpellCardRulesPower(AbstractCreature owner, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		if (this.amount < 1)
			this.amount = -1;
		
		this.type = NeutralPowertypePatch.NEUTRAL;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void updateDescription() {
		this.description = this.amount != -1 ? String.format(powerStrings.DESCRIPTIONS[0],
				this.amount) : powerStrings.DESCRIPTIONS[1];
		
		if (this.amount != -1)
			this.description += powerStrings.DESCRIPTIONS[2];
	}
}
