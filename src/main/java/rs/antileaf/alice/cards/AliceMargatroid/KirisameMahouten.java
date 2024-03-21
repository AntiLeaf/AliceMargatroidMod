package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rs.antileaf.alice.action.common.AliceDiscoverAction;
import rs.antileaf.alice.action.unique.KirisameMahoutenAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.cards.AliceMargatroidDerivation.MarisasPotion;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class KirisameMahouten extends AbstractAliceCard {
	public static final String SIMPLE_NAME = KirisameMahouten.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int MAGIC = 3;
	
	public KirisameMahouten() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;
		this.tags.add(CardTags.HEALING);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractPotion> potions = new ArrayList<>();
		for (int i = 0; i < this.magicNumber; i++) {
			AbstractPotion potion = AbstractDungeon.returnRandomPotion();
			if (potions.stream().anyMatch(po -> potion.ID.equals(po.ID))) {
				i--;
				continue;
			}
			
			potions.add(potion);
		}
		
		AliceSpireKit.log("qwq");
		
		ArrayList<AbstractCard> choices = new ArrayList<>();
		for (AbstractPotion potion : potions)
			choices.add(new MarisasPotion(potion));
		
//		this.addToBot(new ChooseOneAction(choices));
		this.addToBot(new AliceDiscoverAction(
				choices,
				(c) -> {
					if (c instanceof MarisasPotion) {
						MarisasPotion marisasPotion = (MarisasPotion) c;
						
						if (marisasPotion.potion != null) {
							AbstractDungeon.getCurrRoom().addPotionToRewards(marisasPotion.potion.makeCopy());
							this.addToTop(new KirisameMahoutenAction(marisasPotion.potion.name));
						}
						else
							AliceSpireKit.log("KirisameMahouten: Potion is null.");
					}
					else
						AliceSpireKit.log("KirisameMahouten: Potion is not MarisasPotion.");
				},
				cardStrings.EXTENDED_DESCRIPTION[0],
				false
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new KirisameMahouten();
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
