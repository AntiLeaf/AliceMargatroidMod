package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.unique.DollActivationPower;

public class DollActivation extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollActivation.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public DollActivation() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(p, p, new DollActivationPower(this.magicNumber), this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollActivation();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}