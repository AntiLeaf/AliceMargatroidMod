package me.antileaf.alice.events.medicine;

import basemod.abstracts.CustomReward;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import me.antileaf.alice.cards.colorless.CreateSusan;
import me.antileaf.alice.cards.colorless.CreateSusanReplica;

import java.util.ArrayList;

public class MedicineCardReward extends CustomReward {
	private ArrayList<AbstractCard> cards;
	
	public MedicineCardReward(boolean win) {
		super(
				ImageMaster.REWARD_CARD_NORMAL,
				LittleSweetPoison.eventStrings.DESCRIPTIONS[6],
				Enums.MEDICINE_CARD_REWARD
		);
		
		this.cards = new ArrayList<>();
		
		if (win)
			this.cards.add(new CreateSusan());
		
		this.cards.add(new CreateSusanReplica());
		
		this.text = this.cards.get(0).name;
	}
	
	@Override
	public boolean claimReward() {
		AbstractDungeon.cardRewardScreen.open(this.cards, this, LittleSweetPoison.eventStrings.DESCRIPTIONS[6]);
		AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
		return false;
	}
	
	public static class Enums {
		@SpireEnum
		public static RewardType MEDICINE_CARD_REWARD;
	}
}
