package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollPlacement extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollPlacement.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 5;
	private static final int UPGRADED_COST = 0;
	
	public DollPlacement() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.BASIC,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.isMultiDamage = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.calculateCardDamage(null);
		this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		
		this.addToBot(new SpawnDollAction(new ShanghaiDoll(), -1));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollPlacement();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
