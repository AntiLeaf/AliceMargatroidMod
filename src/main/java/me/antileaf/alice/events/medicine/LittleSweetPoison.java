package me.antileaf.alice.events.medicine;

import basemod.ReflectionHacks;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import basemod.patches.com.megacrit.cardcrawl.core.CardCrawlGame.LoadPlayerSaves;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class LittleSweetPoison extends PhasedEvent {
	private static final Logger logger = LogManager.getLogger(LittleSweetPoison.class);
	
	public static final String SIMPLE_NAME = LittleSweetPoison.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

	private static final String INTRO = "INTRO", CHALLENGE = "CHALLENGE", COMBAT = "COMBAT",
			AFTER_COMBAT = "AFTER_COMBAT", REFUSED = "REFUSED", LEAVE = "LEAVE";
	
	public boolean win = false;

	public LittleSweetPoison() {
		super(ID, eventStrings.NAME, AliceHelper.getEventImgFilePath(SIMPLE_NAME));
		
		this.registerPhase(INTRO, new TextPhase(eventStrings.DESCRIPTIONS[0])
				.addOption(eventStrings.OPTIONS[0], (i) -> this.transitionKey(CHALLENGE)));
		
		this.registerPhase(CHALLENGE, new TextPhase(eventStrings.DESCRIPTIONS[1])
				.addOption(eventStrings.OPTIONS[1], (i) -> { // 接受挑战
					this.transitionKey(COMBAT);
				})
				.addOption(eventStrings.OPTIONS[2], (i) -> { // 拒绝
					this.transitionKey(REFUSED);
				}));
		
		this.registerPhase(COMBAT, new CombatPhase(AliceHelper.makeID("MedicineMelancholy")) {
			{ this.cardReward = false; }
			
			@Override
			public void transition(PhasedEvent event) {
				LittleSweetPoison.this.saveCurrentState();
				super.transition(event);
				
				AbstractDungeon.player.maxHealth = 30 + 15 * Math.min(3, AbstractDungeon.actNum);
				AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
				AbstractDungeon.player.healthBarUpdatedEvent();
			}
			
			@Override
			public void postCombatTransition(PhasedEvent event) {
				LittleSweetPoison.this.restoreState();
				
				super.postCombatTransition(event);
			}
		}
				.setType(AbstractMonster.EnemyType.ELITE)
				.setNextKey(AFTER_COMBAT));
		
		this.registerPhase(AFTER_COMBAT, new TextPhase(eventStrings.DESCRIPTIONS[2]) {
			@Override
			public String getBody() {
				if (LittleSweetPoison.this.win)
					ReflectionHacks.setPrivate(this, TextPhase.class, "body",
							eventStrings.DESCRIPTIONS[3]);
				
				return super.getBody();
			}
		}
				.addOption(eventStrings.OPTIONS[3], (i) -> {
					this.openRewardScreen();
					
					this.transitionKey(LEAVE);
				}));
		
		this.registerPhase(REFUSED, new TextPhase(eventStrings.DESCRIPTIONS[4])
				.addOption(eventStrings.OPTIONS[3], (i) -> {
					this.openRewardScreen();
					
					this.transitionKey(LEAVE);
				}));
		
		this.registerPhase(LEAVE, new TextPhase(eventStrings.DESCRIPTIONS[5])
				.addOption(eventStrings.OPTIONS[4], (i) -> this.openMap()));
		
//		this.noCardsInRewards = true;
		this.transitionKey(INTRO);
		
//		SaveAndContinue.save(CardCrawlGame.saveFile);
	}
	
	public void saveCurrentState() {
	
	}
	
	public void restoreState() {
//		if (this.saveFile == null) {
//			logger.warn("Save file is null, cannot restore state");
//			if (AbstractDungeon.player.currentHealth < 1)
//				AbstractDungeon.player.currentHealth = 1;
//			return;
//		}
		
		SaveFile saveFile = CardCrawlGame.saveFile;
		
		AbstractPlayer p = AbstractDungeon.player;
		p.currentHealth = saveFile.current_health;
		p.maxHealth = saveFile.max_health;
		p.gold = saveFile.gold;
		p.displayGold = p.gold;
		p.masterHandSize = saveFile.hand_size;
		p.potionSlots = saveFile.potion_slots;
		if (p.potionSlots == 0) {
			p.potionSlots = 3;
		}
		p.potions.clear();
		for(int i = 0; i < p.potionSlots; ++i)
			p.potions.add(new PotionSlot(i));
		
		p.masterMaxOrbs = saveFile.max_orbs;
		CardCrawlGame.perfect = saveFile.perfect;
		
		p.masterDeck.clear();
		for(CardSave s : saveFile.cards) {
//			logger.info(s.id + ", " + s.upgrades);
			p.masterDeck.addToTop(CardLibrary.getCopy(s.id, s.upgrades, s.misc));
		}
		
		p.relics.clear();
		int index = 0;
		for(String s : saveFile.relics) {
			AbstractRelic r = RelicLibrary.getRelic(s).makeCopy();
			r.instantObtain(p, index, false);
			if (index < saveFile.relic_counters.size()) {
				r.setCounter((Integer)saveFile.relic_counters.get(index));
			}
			
			r.updateDescription(p.chosenClass);
			++index;
		}
		
		
		index = 0;
		for(String s : saveFile.potions) {
			AbstractPotion potion = PotionHelper.getPotion(s);
			if (potion != null) {
				AbstractDungeon.player.obtainPotion(index, potion);
			}
			
			++index;
		}
		
		AbstractCard tmpCard = null;
		if (saveFile.bottled_flame != null) {
			for(AbstractCard i : AbstractDungeon.player.masterDeck.group) {
				if (i.cardID.equals(saveFile.bottled_flame)) {
					tmpCard = i;
					if (i.timesUpgraded == saveFile.bottled_flame_upgrade && i.misc == saveFile.bottled_flame_misc) {
						break;
					}
				}
			}
			
			if (tmpCard != null) {
				tmpCard.inBottleFlame = true;
				((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).card = tmpCard;
				((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).setDescriptionAfterLoading();
			}
		}
		
		tmpCard = null;
		if (saveFile.bottled_lightning != null) {
			for(AbstractCard i : AbstractDungeon.player.masterDeck.group) {
				if (i.cardID.equals(saveFile.bottled_lightning)) {
					tmpCard = i;
					if (i.timesUpgraded == saveFile.bottled_lightning_upgrade && i.misc == saveFile.bottled_lightning_misc) {
						break;
					}
				}
			}
			
			if (tmpCard != null) {
				tmpCard.inBottleLightning = true;
				((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).card = tmpCard;
				((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).setDescriptionAfterLoading();
			}
		}
		
		tmpCard = null;
		if (saveFile.bottled_tornado != null) {
			for(AbstractCard i : AbstractDungeon.player.masterDeck.group) {
				if (i.cardID.equals(saveFile.bottled_tornado)) {
					tmpCard = i;
					if (i.timesUpgraded == saveFile.bottled_tornado_upgrade && i.misc == saveFile.bottled_tornado_misc) {
						break;
					}
				}
			}
			
			if (tmpCard != null) {
				tmpCard.inBottleTornado = true;
				((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).card = tmpCard;
				((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).setDescriptionAfterLoading();
			}
		}
		
		LoadPlayerSaves.Postfix(null, p);
	}
	
	private void openRewardScreen() {
		AbstractDungeon.getCurrRoom().rewards.clear();
		AbstractDungeon.getCurrRoom().rewards.add(new MedicineCardReward(this.win));
		
		int act = Math.min(3, AbstractDungeon.actNum);
		
		if (this.win) {
			AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(
					AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON)));
			
			if (act >= 2)
				AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(
						AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON)));
			
			if (act >= 3)
				AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(
						AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE)));
			
			ArrayList<MedicineKeyReward> keys = new ArrayList<>();
			
			if (!Settings.hasRubyKey)
				keys.add(new MedicineKeyReward(ObtainKeyEffect.KeyColor.RED));
			if (!Settings.hasEmeraldKey)
				keys.add(new MedicineKeyReward(ObtainKeyEffect.KeyColor.GREEN));
			if (!Settings.hasSapphireKey)
				keys.add(new MedicineKeyReward(ObtainKeyEffect.KeyColor.BLUE));
			
			for (int i = 0; i < keys.size(); i++) {
				if (i < keys.size() - 1)
					keys.get(i).nextLink = keys.get(i + 1);
				
				if (i > 0)
					keys.get(i).prevLink = keys.get(i - 1);
				
				if (i == 0 && keys.size() > 2)
					keys.get(i).otherLink = keys.get(2);
				else if (i == 2)
					keys.get(i).otherLink = keys.get(0);
			}
			
			AbstractDungeon.getCurrRoom().rewards.addAll(keys);
		}
		else {
			AbstractDungeon.getCurrRoom().rewards.add(new RewardItem());
			
			if (act >= 2)
				AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(
						AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON)));
			
			if (act >= 3)
				AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(
						AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON)));
		}
		
		this.noCardsInRewards = true;
		
		AbstractDungeon.combatRewardScreen.clear();
		AbstractDungeon.combatRewardScreen.open();
	}
	
}
