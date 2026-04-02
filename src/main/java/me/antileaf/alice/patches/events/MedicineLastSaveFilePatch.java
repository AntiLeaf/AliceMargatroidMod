package me.antileaf.alice.patches.events;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class MedicineLastSaveFilePatch {
	private static final Logger logger = LogManager.getLogger(MedicineLastSaveFilePatch.class);
	
	public static SaveFile lastSaveFile = null;
	
	@SpirePatch(clz = SaveAndContinue.class, method = "save",
			paramtypez = {SaveFile.class})
	public static class SavePatch {
		@SpirePrefixPatch
		public static void Prefix(SaveFile saveFile) {
			lastSaveFile = saveFile;
			logger.info("Saved save file");
		}
	}
	
	@SpirePatch(clz = SaveAndContinue.class, method = "loadSaveFile",
			paramtypez = {String.class})
	public static class LoadSaveFilePatch {
		@SpirePostfixPatch
		public static SaveFile Postfix(SaveFile saveFile, String filePath) {
			lastSaveFile = saveFile;
			logger.info("Load save file");
			return saveFile;
		}
	}
}
