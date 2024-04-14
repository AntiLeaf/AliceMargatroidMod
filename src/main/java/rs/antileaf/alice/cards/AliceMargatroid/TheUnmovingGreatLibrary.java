package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.common.AlicePlayTempCardAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class TheUnmovingGreatLibrary extends AbstractAliceCard {
	public static final String SIMPLE_NAME = TheUnmovingGreatLibrary.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	
	public TheUnmovingGreatLibrary() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int roll = AbstractDungeon.cardRandomRng.random(99);
		AbstractCard.CardRarity cardRarity;
		if (roll < 55)
			cardRarity = CardRarity.COMMON;
		else if (roll < 85)
			cardRarity = CardRarity.UNCOMMON;
		else
			cardRarity = CardRarity.RARE;
		
		AbstractCard card = CardLibrary.getAnyColorCard(cardRarity).makeCopy();
		this.addToBot(new AlicePlayTempCardAction(card, true));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new TheUnmovingGreatLibrary();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.exhaust = false;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
