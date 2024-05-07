package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.powers.interfaces.OnDollOperatePower;

public class DollOrchestraPower extends AbstractAlicePower implements OnDollOperatePower {
	public static final String POWER_ID = DollOrchestraPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DollOrchestraPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.priority = 99;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
	@Override
	public void postSpawnDoll(AbstractDoll doll) {
		this.flash();
		this.addToBot(new DrawCardAction(this.amount));
	}
}
