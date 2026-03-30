package me.antileaf.alice.patches.misc.compatibility;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

import java.util.WeakHashMap;

@SuppressWarnings("unused")
public class ExploderCompatibilityPatch {
	public static final WeakHashMap<DamageInfo, Boolean> explosions = new WeakHashMap<>();
	
	public static void markExplosion(DamageInfo info) {
		explosions.put(info, true);
	}
	
	@SpirePatch(clz = ExplosivePower.class, method = "duringTurn", paramtypez = {})
	public static class ExplosivePowerPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(NewExpr n) throws CannotCompileException {
					if (n.getClassName().equals(DamageAction.class.getName())) {
						n.replace("{ " + ExploderCompatibilityPatch.class.getName() +
								".markExplosion($2); $_ = $proceed($$); }");
					}
				}
			};
		}
	}
}
