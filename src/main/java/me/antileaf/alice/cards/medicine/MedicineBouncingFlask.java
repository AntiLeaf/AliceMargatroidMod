package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.cards.green.BouncingFlask;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineBouncingFlask extends BouncingFlask {
	public static final String SIMPLE_NAME = MedicineBouncingFlask.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	public MedicineBouncingFlask() {
		super();
		
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}
}
