package rs.antileaf.alice;

import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import rs.antileaf.alice.cards.AliceMagtroid.*;
import rs.antileaf.alice.cards.AliceMagtroid.Thread;
import rs.antileaf.alice.characters.AliceMagtroid;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.targeting.DollOrEmptySlotTargeting;
import rs.antileaf.alice.doll.targeting.DollOrEnemyTargeting;
import rs.antileaf.alice.doll.targeting.DollOrNoneTargeting;
import rs.antileaf.alice.doll.targeting.DollTargeting;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.relics.AlicesGrimoire;
import rs.antileaf.alice.utils.AliceSpireKit;
import rs.antileaf.alice.variable.TempHPVariable;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.AliceMagtroidModClassEnum;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static rs.antileaf.alice.patches.enums.AbstractCardEnum.ALICE_MAGTROID_COLOR;

@SuppressWarnings("Duplicates")
@SpireInitializer
public class AliceMagtroidMod implements PostExhaustSubscriber,
		PostBattleSubscriber,
		PostDungeonInitializeSubscriber,
		EditCharactersSubscriber,
		PostInitializeSubscriber,
		EditRelicsSubscriber,
		EditCardsSubscriber,
		EditStringsSubscriber,
		OnCardUseSubscriber,
		EditKeywordsSubscriber,
		OnPowersModifiedSubscriber,
		PostDrawSubscriber,
		PostEnergyRechargeSubscriber,
		OnPlayerLoseBlockSubscriber,
		OnPlayerDamagedSubscriber,
		OnStartBattleSubscriber,
		OnPlayerTurnStartSubscriber,
		PostPlayerUpdateSubscriber,
		RenderSubscriber {
	public static final String SIMPLE_NAME = AliceMagtroidMod.class.getSimpleName();
	
	public static final Logger logger = LogManager.getLogger(AliceMagtroidMod.class.getName());
	
//	private static final String MOD_BADGE = "img/UI/badge.png";
	
	private static String getCCPath(String s) {
		return "img/512/" + s + ".png";
	}
	
	private static String getCCPortraitPath(String s) {
		return "img/1024/" + s + ".png";
	}
	
	//card backgrounds
	private static final String ATTACK_CC = getCCPath("bg_attack_s");
	private static final String SKILL_CC = getCCPath("bg_skill_s");
	private static final String POWER_CC = getCCPath("bg_power_s");
	private static final String ENERGY_ORB_CC = getCCPath("cardOrb_s");
	
	private static final String ATTACK_CC_PORTRAIT = getCCPortraitPath("bg_attack");
	private static final String SKILL_CC_PORTRAIT = getCCPortraitPath("bg_skill");
	private static final String POWER_CC_PORTRAIT = getCCPortraitPath("bg_power");
	private static final String ENERGY_ORB_CC_PORTRAIT = getCCPortraitPath("cardOrb");
	
	public static final Color ALICE_PUPPETEER = CardHelper.getColor(0, 191, 255);
	public static final Color ALICE_PUPPETEER_FLAVOR = CardHelper.getColor(204, 255, 255);
	public static final String CARD_ENERGY_ORB = "img/UI/AliceMagtroid/energyOrb.png";
	
	private static final String CHARACTER_BUTTON = "img/charSelect/AliceMagtroid/Button.png";
	private static final String ALICE_PORTRAIT = "img/charSelect/AliceMagtroid/Portrait.jpg";
	
	private final ArrayList<AbstractCard> cardsToAdd = new ArrayList<>();
	//private ArrayList<AbstractRelic> relicsToAdd = new ArrayList<>();
	
	public AliceMagtroidMod() {
		BaseMod.subscribe(this);
		logger.info("creating the color : ALICE_COLOR and ALICE_DERIVATION_COLOR");
		BaseMod.addColor(
				ALICE_MAGTROID_COLOR,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ATTACK_CC,
				SKILL_CC,
				POWER_CC,
				ENERGY_ORB_CC,
				ATTACK_CC_PORTRAIT,
				SKILL_CC_PORTRAIT,
				POWER_CC_PORTRAIT,
				ENERGY_ORB_CC_PORTRAIT,
				CARD_ENERGY_ORB
		);
		BaseMod.addColor(
				AbstractCardEnum.ALICE_MAGTROID_DERIVATION_COLOR,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ATTACK_CC,
				SKILL_CC,
				POWER_CC,
				ENERGY_ORB_CC,
				ATTACK_CC_PORTRAIT,
				SKILL_CC_PORTRAIT,
				POWER_CC_PORTRAIT,
				ENERGY_ORB_CC_PORTRAIT,
				CARD_ENERGY_ORB
		);
	}
	
	public void receiveEditCharacters() {
		logger.info("begin editing characters");
		
		logger.info("add " + AliceMagtroidModClassEnum.ALICE_MAGTROID.toString());
		BaseMod.addCharacter(
				new AliceMagtroid("Alice Magtroid"),
				CHARACTER_BUTTON,
				ALICE_PORTRAIT,
				AliceMagtroidModClassEnum.ALICE_MAGTROID
		);
		logger.info("done editing characters");
	}
	
	public void receiveEditRelics() {
		logger.info("Begin editing relics.");
		BaseMod.addRelicToCustomPool(
				new AlicesGrimoire(),
				ALICE_MAGTROID_COLOR
		);
		
		logger.info("Relics editing finished.");
	}
	
	public void receiveEditCards() {
		this.loadVariables();
		
		logger.info("starting editing cards");
		
		loadCardsToAdd();
		
		logger.info("adding cards for ALICE_MAGTROID");
		
		for (AbstractCard card : cardsToAdd) {
			logger.info("Adding card : " + card.name);
			BaseMod.addCard(card);
			
			UnlockTracker.unlockCard(card.cardID);
		}
		
		logger.info("done editing cards");
	}
	
	// 必须有这个函数才能初始化
	public static void initialize() {
		new AliceMagtroidMod();
	}
	
	private static String loadJson(String jsonPath) {
		return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
	}
	
	@Override
	public void receiveEditKeywords() {
		logger.info("Setting up custom keywords");
//		System.out.println("Setting up custom keywords");

		String keywordsPath = AliceSpireKit.getLocalizationFilePath("keywords");

		Gson gson = new Gson();
		Keyword[] keywords = gson.fromJson(loadJson(keywordsPath), Keyword[].class);
		for (Keyword key : keywords) {
			logger.info("Loading keyword : " + key.NAMES[0]);
			BaseMod.addKeyword(key.NAMES, key.DESCRIPTION);
		}
		logger.info("Keywords setting finished.");
	}
	
	@Override
	public void receiveEditStrings() {
		logger.info("start editing strings");
		
		String lang = AliceSpireKit.getLangShort();
		
		AliceSpireKit.loadCustomStrings(RelicStrings.class, "relics");
		AliceSpireKit.loadCustomStrings(CardStrings.class, "cards");
//		AliceSpireKit.loadCustomStrings(PowerStrings.class, "powers");
//		AliceSpireKit.loadCustomStrings(PotionStrings.class, "potions");
//		AliceSpireKit.loadCustomStrings(EventStrings.class, "events");
		AliceSpireKit.loadCustomStrings(OrbStrings.class, "dolls");
//		AliceSpireKit.loadCustomStrings(DollStrings.class, "dolls");

		logger.info("done editing strings");
	}
	
	@Override
	public void receivePostExhaust(AbstractCard card) {
		// Auto-generated method stub
	}
	
	@Override
	public void receivePostBattle(AbstractRoom room) {
		DollManager.get().clearPostBattle();
	}
	
	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		DollManager.get().initPreBattle();
	}
	
	@Override
	public void receiveCardUsed(AbstractCard c) {
		DollManager.get().debug();
	}
	
	@Override
	public void receivePostEnergyRecharge() {
		// Auto-generated method stub
	}
	
	@Override
	public void receivePowersModified() {
		// Auto-generated method stub
	}
	
	@Override
	public void receivePostDungeonInitialize() {
		// Auto-generated method stub
	}
	
	@Override
	public void receivePostDraw(AbstractCard card) {
		DollManager.getInstance(AbstractDungeon.player).onStartOfTurn();
	}
	
	@Override
	public void receivePostInitialize() {
		logger.debug("AliceMagtroidMod.receivePostInitialize");
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL, new DollTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_EMPTY_SLOT, new DollOrEmptySlotTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_ENEMY, new DollOrEnemyTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_NONE, new DollOrNoneTargeting());
	}
	
	@Override
	public void receiveOnPlayerTurnStart() {}
	
	@Override
	public void receivePostPlayerUpdate() {
		DollManager.getInstance(AbstractDungeon.player).update();
	}
	
	public int receiveOnPlayerDamaged(int amount, DamageInfo damageInfo) {
		if (!(damageInfo.owner instanceof AbstractMonster) || damageInfo.type != DamageInfo.DamageType.NORMAL)
			return amount;
		
		int index = AliceSpireKit.getMonsterIndex((AbstractMonster) damageInfo.owner);
		if (index == -1) {
			AliceSpireKit.log("AliceMagtroidMod.receiveOnPlayerDamaged", "index == -1");
			return amount;
		}
		
		AbstractDoll doll = DollManager.get().getDolls().get(index);
		if (doll instanceof EmptyDollSlot)
			return amount;
		
		int remaining = doll.onPlayerDamaged(amount);
		DollManager.get().dollTakesDamage(doll, amount - remaining);
		
		return remaining;
	}
	
	public int receiveOnPlayerLoseBlock(int amount) {
		DollManager.get().updatePreservedBlock();
		DollManager.get().clearBlock();
		
		int preserve = DollManager.get().getPreservedBlock();
		return Integer.min(amount, AbstractDungeon.player.currentBlock - preserve);
	}
	
	public void receiveRender(SpriteBatch sb) {
//		DollManager.get().render(sb);
	}

	private void loadCardsToAdd() {
		this.cardsToAdd.clear();
		
		this.cardsToAdd.add(new Strike_AliceMagtroid());
		this.cardsToAdd.add(new Defend_AliceMagtroid());
		this.cardsToAdd.add(new Thread());
		this.cardsToAdd.add(new LittleLegion());
		this.cardsToAdd.add(new ProtectiveMagic());
	}
	
	private void loadVariables() {
		BaseMod.addDynamicVariable(new TempHPVariable());
	}
}
