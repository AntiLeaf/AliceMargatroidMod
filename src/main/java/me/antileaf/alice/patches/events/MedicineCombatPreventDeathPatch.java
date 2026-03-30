package me.antileaf.alice.patches.events;

import com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnPlayerDeathPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import javassist.CtBehavior;
import me.antileaf.alice.events.medicine.LittleSweetPoison;

@SuppressWarnings("unused")
public class MedicineCombatPreventDeathPatch {
	@SpirePatch(clz = OnPlayerDeathPatch.class, method = "Insert",
			paramtypez = {AbstractPlayer.class, DamageInfo.class})
	public static class OnPlayerDeathPatchPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws Exception {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(SpireReturn.class, "Continue"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<SpireReturn<Void>> Insert(AbstractPlayer player, DamageInfo info) {
			if (AbstractDungeon.getCurrRoom() instanceof EventRoom &&
					AbstractDungeon.getCurrRoom().event instanceof LittleSweetPoison &&
							AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
				AbstractDungeon.getCurrRoom().endBattle();
				AbstractDungeon.getCurrRoom().monsters.monsters.clear();
//				((LittleSweetPoison) AbstractDungeon.getCurrRoom().event).restoreState();
				
				return SpireReturn.Return(SpireReturn.Return());
			}
			
			return SpireReturn.Continue();
		}
	}
}
