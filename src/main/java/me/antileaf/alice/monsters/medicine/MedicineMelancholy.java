package me.antileaf.alice.monsters.medicine;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RunicDome;
import com.megacrit.cardcrawl.rooms.EventRoom;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.medicine.*;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.events.medicine.LittleSweetPoison;
import me.antileaf.alice.patches.enums.IntentEnum;
import me.antileaf.alice.powers.medicine.*;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MedicineMelancholy extends AbstractMonster {
	private static final Logger logger = LogManager.getLogger(MedicineMelancholy.class);
	
	public static final String SIMPLE_NAME = MedicineMelancholy.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	
	private static final byte GASSING_GARDEN = 1;
	private static final byte POISON_CLOUD1 = 2;
	private static final byte POISON_BEE1 = 3;
	private static final byte CHARGE1 = 4;
	private static final byte POISON_BREATH = 5;
	private static final byte POISON_BEE2 = 6;
	private static final byte NERVE_POISON2 = 7;
	private static final byte CHARGE2 = 8;
	private static final byte INTO_DELIRIUM = 9;
	private static final byte NERVE_POISON3 = 10;
	private static final byte POISONOUS_SWEET = 11;
	private static final byte POISON_BEE3 = 12;
	private static final byte A_POISON_FOR_MELANCHOLY = 13;
	
	public MedicineCardList cardList;
	
	int base_max_hp = 0;
	int lives = 0, cur_stage = 0;
	int moveCount = 0;
	
	public MedicineMelancholy(float x, float y) {
		super(
				monsterStrings.NAME,
				ID,
				100, // just temp value
				0.0F, // HB offset X
				-20.0F, // HB offset Y
				250.0F, // HB width
				300.0F, // HB height
				AliceHelper.getImgFilePath("monsters", SIMPLE_NAME),
				x,
				y
		);
		
		this.type = EnemyType.ELITE;
		
		this.lives = Math.min(AbstractDungeon.actNum, 3);
		this.cur_stage = 1;
		
		if (AbstractDungeon.ascensionLevel >= 8)
			this.base_max_hp = 90 + 25 * (this.lives - 1);
		else
			this.base_max_hp = 80 + 20 * (this.lives - 1);
		
		this.cardList = new MedicineCardList(this.hb.cX, this.hb.cY);
	}
	
	public MedicineMelancholy() {
		this(0.0F, 0.0F);
	}
	
	@Override
	public void usePreBattleAction() {
		CardCrawlGame.music.unsilenceBGM();
		AbstractDungeon.scene.fadeOutAmbiance();
		CardCrawlGame.music.silenceTempBgmInstantly();
		AbstractDungeon.getCurrRoom().playBgmInstantly("th09_12.mp3");
		
		this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
				new SpellCardRulesPowerPlayer(AbstractDungeon.player)));
		this.addToBot(new ApplyPowerAction(this, this,
				new SpellCardRulesPower(this, this.lives - 1)));
		
		if (this.lives > 1)
			AbstractDungeon.getCurrRoom().cannotLose = true;
	}
	
	@Override
	public void takeTurn() {
		if (this.halfDead) {
			this.addToBot(new HealAction(this, this, this.maxHealth));
			this.addToBot(new AnonymousAction(() -> this.halfDead = false));
			if (this.hasPower(SpellCardRulesPower.POWER_ID) &&
					this.getPower(SpellCardRulesPower.POWER_ID).amount > 1)
				this.addToBot(new ReducePowerAction(this, this,
						SpellCardRulesPower.POWER_ID, 1));
			else
				this.addToBot(new AnonymousAction(() -> {
					if (this.hasPower(SpellCardRulesPower.POWER_ID))
						this.getPower(SpellCardRulesPower.POWER_ID).amount = -1;
				}));
			
			for (AbstractRelic relic : AbstractDungeon.player.relics)
				relic.onSpawnMonster(this);
		}
		
		this.cardList.playAll(this);
		
		if (this.hasPower(MedicineExtraDrawPower.POWER_ID))
			this.addToTop(new RemoveSpecificPowerAction(this, this,
					MedicineExtraDrawPower.POWER_ID));
		if (this.hasPower(MedicineOutmaneuverPower.POWER_ID))
			this.addToTop(new RemoveSpecificPowerAction(this, this,
					MedicineOutmaneuverPower.POWER_ID));
		
		this.addToBot(new RollMoveAction(this));
	}
	
	private void addShivs() {
		if (this.hasPower(MedicineInfiniteBladesPower.POWER_ID)) {
			int amount = this.getPower(MedicineInfiniteBladesPower.POWER_ID).amount;
			
			for (int k = 0; k < amount; k++)
				this.cardList.add(new Shiv());
		}
	}
	
	@Override
	protected void getMove(int i) {
		this.cardList.clear();
		
		byte move = 0;
		
		if (this.cur_stage == 1) {
			if (this.moveCount == 0) { // 雾符「毒气花园」
				this.cardList.add(AliceHelper.upgraded(new MedicineEnvenom()));
				this.cardList.add(new MedicineNoxiousFumes());
				this.cardList.add(new Outmaneuver());
				
				move = GASSING_GARDEN;
			}
			else if (this.moveCount == 1) { // 毒云
				this.cardList.add(new DeadlyPoison());
				this.cardList.add(AliceHelper.upgraded(new MedicineCripplingPoison()));
				this.cardList.add(new MedicineCorpseExplosion());
				
				move = POISON_CLOUD1;
			}
			else if (this.moveCount == 2) { // 毒蜂
				this.cardList.add(new Bane());
				this.cardList.add(new PoisonedStab());
				this.cardList.add(new PoisonedStab());
				
				move = POISON_BEE1;
			}
			else if (this.moveCount == 3) { // 蓄力
				this.cardList.add(new BouncingFlask());
				this.cardList.add(new Outmaneuver());
				
				move = CHARGE1;
			}
		}
		else if (this.cur_stage == 2) {
			if (this.moveCount == 0) { // 毒符「猛毒气息」
				this.cardList.add(AliceHelper.upgraded(new MedicineEnvenom(), 2));
				this.cardList.add(AliceHelper.upgraded(new MedicineInfiniteBlades()));
				this.cardList.add(AliceHelper.upgraded(new Outmaneuver()));
				
				move = POISON_BREATH;
			}
			else if (this.moveCount == 1) { // 毒蜂
				this.cardList.add(AliceHelper.upgraded(new AfterImage()));
				this.cardList.add(AliceHelper.upgraded(new Bane()));
				this.cardList.add(AliceHelper.upgraded(new Acrobatics()));
				this.cardList.add(AliceHelper.upgraded(new PoisonedStab()));
				this.cardList.add(AliceHelper.upgraded(new PoisonedStab()));
				this.cardList.add(AbstractDungeon.ascensionLevel >= 18 ?
						new Catalyst() : AliceHelper.upgraded(new DeadlyPoison()));
				
				move = POISON_BEE2;
			}
			else if (this.moveCount == 2) { // 毒符「神经之毒」
				this.addShivs();
				
				this.cardList.add(AliceHelper.upgraded(new MedicineCripplingPoison(), 2));
				this.cardList.add(AliceHelper.upgraded(new PiercingWail()));
				
				move = NERVE_POISON2;
			}
			else if (this.moveCount == 3) { // 蓄力
				this.cardList.add(AliceHelper.upgraded(new Bane()));
				this.cardList.add(AliceHelper.upgraded(new PhantasmalKiller()));
				this.cardList.add(AliceHelper.upgraded(new Caltrops()));
				this.cardList.add(AliceHelper.upgraded(new Outmaneuver()));
				
				move = CHARGE2;
			}
		}
		else if (this.cur_stage == 3) {
			if (this.moveCount == 0) { // 谵妄「陷入疯狂」
				AbstractCard delirium = new MedicineDelirium();
				if (AbstractDungeon.ascensionLevel >= 18)
					delirium.upgrade();
				this.cardList.add(delirium);
				
				this.cardList.add(AliceHelper.upgraded(new MedicineEnvenom(), 2));
				this.cardList.add(AliceHelper.upgraded(new MedicineNoxiousFumes()));
				this.cardList.add(AliceHelper.upgraded(new MedicineInfiniteBlades()));
				
				move = INTO_DELIRIUM;
			}
			else if (this.moveCount == 1) { // 毒符「神经之毒」
				this.addShivs();
				
				this.cardList.add(AliceHelper.upgraded(new MedicineCripplingPoison(), 2));
				this.cardList.add(AliceHelper.upgraded(new PiercingWail()));
				if (AbstractDungeon.ascensionLevel >= 18)
					this.cardList.add(AliceHelper.upgraded(new DeadlyPoison()));
				this.cardList.add(AliceHelper.upgraded(new MedicineCorpseExplosion()));
				
				move = NERVE_POISON3;
			}
			else if (this.moveCount == 2) { // 甜蜜之毒
				this.cardList.add(AliceHelper.upgraded(new Bane()));
				this.cardList.add(AliceHelper.upgraded(new PhantasmalKiller()));
				this.cardList.add(AliceHelper.upgraded(new MedicineBouncingFlask()));
				if (AbstractDungeon.ascensionLevel >= 18)
					this.cardList.add(AliceHelper.upgraded(new DeadlyPoison()));
				this.cardList.add(AliceHelper.upgraded(new Outmaneuver()));
				
				move = POISONOUS_SWEET;
			}
			else if (this.moveCount == 3) { // 毒蜂
				this.cardList.add(AliceHelper.upgraded(new AfterImage()));
				this.cardList.add(AliceHelper.upgraded(new Bane()));
				this.cardList.add(AliceHelper.upgraded(new Expertise()));
				this.cardList.add(AliceHelper.upgraded(new PoisonedStab()));
				this.cardList.add(AliceHelper.upgraded(new PoisonedStab()));
				if (AbstractDungeon.ascensionLevel >= 18) {
					this.cardList.add(new PoisonedStab());
					this.cardList.add(AliceHelper.upgraded(new Catalyst()));
				}
				else
					this.cardList.add(new Catalyst());
				this.cardList.add(AliceHelper.upgraded(new Doppelganger()));
				
				move = POISON_BEE3;
			}
			else if (this.moveCount == 4) { // 毒符「忧郁之毒」
				if (AbstractDungeon.ascensionLevel >= 18)
					this.cardList.add(AliceHelper.upgraded(new Bane()));
				this.cardList.add(AliceHelper.upgraded(new DeadlyPoison()));
				this.cardList.add(AliceHelper.upgraded(new MedicineBouncingFlask()));
				this.cardList.add(AliceHelper.upgraded(new MedicineNoxiousFumes()));
				this.cardList.add(AliceHelper.upgraded(new Caltrops()));
				this.cardList.add(AliceHelper.upgraded(new MedicineInfiniteBlades()));
				
				move = A_POISON_FOR_MELANCHOLY;
			}
		}
		
		if (move != 0) {
			if (move != NERVE_POISON2 && move != NERVE_POISON3 &&
					move != GASSING_GARDEN && move != POISON_BREATH && move != INTO_DELIRIUM)
				this.addShivs();
			
			this.setMove(monsterStrings.MOVES[move + 1], move, IntentEnum.MEDICINE_PLAY_CARD);
		}
		else {
			logger.warn("Cannot determine next move for Medicine!");
			this.setMove((byte) 0, Intent.NONE);
		}
		
		this.createIntent();
		this.applyPowers();
		
		this.moveCount++;
		if ((this.cur_stage <= 2 && this.moveCount > 3) ||
				(this.cur_stage == 3 && this.moveCount > 4))
			this.moveCount = 1;
	}
	
	public int calculateCardDamage(int damage) {
		ReflectionHacks.privateMethod(AbstractMonster.class, "calculateDamage", int.class)
				.invoke(this, damage);
		int tmp = this.getIntentDmg();
		ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", -1);
		return tmp;
	}
	
	public boolean bane(int index) {
		if (AbstractDungeon.player.hasPower(MedicinePoisonPower.POWER_ID)) {
			AbstractPower power = AbstractDungeon.player.getPower(MedicinePoisonPower.POWER_ID);
			if (power.amount > 1)
				return true;
		}
		
		AbstractDoll doll = DollManager.get().getDolls().get(index);
		return doll != null && !(doll instanceof EmptyDollSlot) && doll.poison > 1;
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		
		this.cardList.applyPowers(this);
	}
	
	@Override
	public void damage(DamageInfo info) {
		super.damage(info);
		
		if (this.currentHealth <= 0 && !this.halfDead) {
			if (this.cur_stage < this.lives)
				this.cur_stage++;
			else {
				AbstractDungeon.getCurrRoom().cannotLose = false;
				this.die();
				return;
			}
			
			if (AbstractDungeon.getCurrRoom().cannotLose)
				this.halfDead = true;
			
			this.currentBlock = 0;
			
			for (AbstractPower p : this.powers)
				p.onDeath();
			
			for (AbstractRelic r : AbstractDungeon.player.relics)
				r.onMonsterDeath(this);
			
			this.addToTop(new AnonymousAction(() -> {
				for (AbstractDoll doll : DollManager.get().getDolls())
					doll.poison = doll.corpseExplosion = 0;
			}));
			this.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player,
					AbstractDungeon.player, MedicinePoisonPower.POWER_ID));
			
			this.addToTop(new ClearCardQueueAction());
			
			this.powers.removeIf(p -> p instanceof MedicineOutmaneuverPower);
			this.powers.removeIf(p -> p instanceof MedicineExtraDrawPower);
			this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF);
			
			this.moveCount = 0;
			this.getMove(0);
			if (this.nextMove != 0)
				this.addToBot(new SetMoveAction(this,
						monsterStrings.MOVES[this.nextMove + 1],
						this.nextMove,
						IntentEnum.MEDICINE_PLAY_CARD));
			else
				this.addToBot(new SetMoveAction(this, (byte) 0, Intent.NONE));
		}
	}
	
	@Override
	public void die() {
		if (!AbstractDungeon.getCurrRoom().cannotLose) {
			super.die();
			
			if (AbstractDungeon.getCurrRoom() instanceof EventRoom &&
					AbstractDungeon.getCurrRoom().event instanceof LittleSweetPoison)
				((LittleSweetPoison) AbstractDungeon.getCurrRoom().event).win = true;
		}
	}
	
	@SpireOverride
	public void updateIntentTip() {
		SpireSuper.call();
		
		ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentImg", null);
		
		PowerTip intentTip = ReflectionHacks.getPrivate(this,
				AbstractMonster.class, "intentTip");
		intentTip.header = monsterStrings.MOVES[0];
		intentTip.body = String.format(monsterStrings.MOVES[1], this.cardList.getNames());
		intentTip.img = null;
	}
	
	@Override
	public void update() {
		super.update();
		
		this.cardList.update(this.hb.cX, this.hb.cY + this.hb.height / 2.0F + 100.0F * Settings.scale);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		
		if (!AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic(RunicDome.ID))
			this.cardList.render(sb);
	}
	
	@SpireOverride
	public void renderIntent(SpriteBatch sb) {
		SpireSuper.call(sb);
		
//		if (!AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic(RunicDome.ID))
//			this.cardList.renderIntent(sb);
	}
}
