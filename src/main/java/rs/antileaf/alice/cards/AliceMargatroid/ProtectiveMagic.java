package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.targeting.DollOrNoneTargeting;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ProtectiveMagic extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ProtectiveMagic.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int BLOCK = 10;
	private static final int UPGRADE_PLUS_BLOCK = 4;
	
	public ProtectiveMagic() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_NONE
		);
		
		this.block = this.baseBlock = BLOCK;
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		final AbstractDoll target = DollOrNoneTargeting.getTarget(this);
		
		if (target == null)
			this.addToBot(new AnonymousAction(() -> {
				if (p.currentBlock < this.block)
					this.addToTop(new GainBlockAction(p, p, this.block - p.currentBlock));
			}));
		else
			this.addToBot(new AnonymousAction(() -> {
				AliceSpireKit.log(SIMPLE_NAME, "ProtectiveMagic: current block = " + target.block);
				
				if (!DollManager.get().contains(target))
					AliceSpireKit.log(this.getClass(), "ProtectiveMagic: target not in DollManager");
				else if (target.block < this.block)
					this.addToTop(new DollGainBlockAction(target, this.block - target.block));
			}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ProtectiveMagic();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.initializeDescription();
		}
	}
}
