package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
import rs.antileaf.alice.utils.AliceSpireKit;

public class SuspiciousCard extends CustomRelic {
	public static final String SIMPLE_NAME = SuspiciousCard.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = AliceSpireKit.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceSpireKit.getRelicOutlineImgFilePath(SIMPLE_NAME);

	public SuspiciousCard() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.UNCOMMON,
				LandingSound.FLAT
		);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atBattleStart() {
		this.flash();
		this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		
//		for (int i = 0; i < 2; i++)
		this.addToBot(new SpawnDollAction(new ShanghaiDoll(), -1));
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new SuspiciousCard();
	}
}
