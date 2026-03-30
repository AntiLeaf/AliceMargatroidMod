package me.antileaf.alice.powers.medicine;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class SpellCardRulesPowerPlayer extends AbstractAlicePower {
	public static final String SIMPLE_NAME = SpellCardRulesPowerPlayer.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public SpellCardRulesPowerPlayer(AbstractCreature owner) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.priority = -999;
		
		this.type = NeutralPowertypePatch.NEUTRAL;
		this.updateDescription();
		this.initializeImage("SpellCardRulesPower");
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}
}
