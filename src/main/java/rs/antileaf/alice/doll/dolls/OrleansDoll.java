package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class OrleansDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = OrleansDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 1;
	
	public OrleansDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceSpireKit.getOrbImgFilePath("black"),
				RenderTextMode.NONE
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.MAGIC;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public void onAct() {
		this.addToBot(new DrawCardAction(1));
	}
	
	@Override
	public void onRecycle() {
		this.addToTop(new GainEnergyAction(2));
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = dollStrings.DESCRIPTION[0];
		this.actDescription = dollStrings.DESCRIPTION[1];
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new OrleansDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new OrleansDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
