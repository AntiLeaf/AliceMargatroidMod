package rs.antileaf.alice.cards.AliceMargatroidDerivation.dolls;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.dolls.KyotoDoll;
import rs.antileaf.alice.doll.dolls.LondonDoll;

public class CreateLondonDoll extends AbstractCreateDoll {
	public static final String SIMPLE_NAME = CreateLondonDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = -2;
	
	public int index;
	
	public CreateLondonDoll() {
		super(
				ID,
				cardStrings.NAME,
				null,
				COST,
				cardStrings.DESCRIPTION
		);
	}
	
	@Override
	public AbstractDoll getDoll() {
		return new LondonDoll();
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CreateLondonDoll();
	}
}
