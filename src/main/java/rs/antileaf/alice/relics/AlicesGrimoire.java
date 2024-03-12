package rs.antileaf.alice.relics;

import rs.antileaf.alice.AliceMagtroidMod;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.doll.dolls.KyotoDoll;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;

public class AlicesGrimoire extends CustomRelic {
	public static final String SIMPLE_NAME = AlicesGrimoire.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = "img/relics/AliceMagtroid/" + SIMPLE_NAME + ".png";
	private static final String IMG_OTL = "img/relics/AliceMagtroid/outline/" + SIMPLE_NAME + ".png";

	public AlicesGrimoire() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.STARTER,
				LandingSound.MAGICAL
		);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atBattleStart() {
		this.addToBot(new SpawnDollAction(new ShanghaiDoll(), -1));
		this.addToBot(new SpawnDollAction(new KyotoDoll(), -1));
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new AlicesGrimoire();
	}
}
