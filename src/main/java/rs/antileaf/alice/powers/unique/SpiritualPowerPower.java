package rs.antileaf.alice.powers.unique;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

public class SpiritualPowerPower extends AbstractPower {
	public static final String POWER_ID = SpiritualPowerPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public SpiritualPowerPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.img = new Texture(AliceSpireKit.getPowerImgFilePath("default"));
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = AliceMiscKit.join(
				powerStrings.DESCRIPTIONS[0],
				"#b" + this.amount,
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.tags.contains(CardTagEnum.ALICE_COMMAND))
			this.addToBot(new AnonymousAction(() -> {
				this.flash();
				this.addToTop(new DamageRandomEnemyAction(
						new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS),
						AbstractGameAction.AttackEffect.POISON
				));
			}));
	}
}
