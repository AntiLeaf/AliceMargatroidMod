package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Collector extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Collector.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int BLOCK = 0;
	private static final int UPGRADE_PLUS_BLOCK = 3;
	
	public Collector() {
		super(
				ID,
				cardStrings.NAME,
				null, //AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.SELF
		);
		
		this.block = this.baseBlock = BLOCK;
	}
	
	@Override
	public void applyPowers() {
		this.baseBlock = AbstractDungeon.player.relics.size();
		if (this.upgraded)
			this.baseBlock += UPGRADE_PLUS_BLOCK;
		
		super.applyPowers();
//		this.initializeDescription();
	}
	
	@Override
	public void initializeDescription() {
		this.rawDescription = (!this.upgraded ? cardStrings.DESCRIPTION : cardStrings.UPGRADE_DESCRIPTION);
		if (AliceSpireKit.isInBattle())
			this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
		
		super.initializeDescription();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainBlockAction(p, p, this.block));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Collector();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.initializeDescription();
		}
	}
}
