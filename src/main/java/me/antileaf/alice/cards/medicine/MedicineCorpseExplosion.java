package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.cards.green.CorpseExplosion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineCorpseExplosion extends CorpseExplosion {
	public static final String SIMPLE_NAME = MedicineCorpseExplosion.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	public MedicineCorpseExplosion() {
		super();
		
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}
}
