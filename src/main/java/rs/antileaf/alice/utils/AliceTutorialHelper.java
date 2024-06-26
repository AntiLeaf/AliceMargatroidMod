package rs.antileaf.alice.utils;

import basemod.abstracts.CustomMultiPageFtue;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import rs.antileaf.alice.doll.AbstractDoll;

import java.util.ArrayList;

public class AliceTutorialHelper {
	private static TutorialStrings generated = null;
	
	public static void generateTutorial() {
		AliceSpireKit.log("Generating tutorial...");
		
		TutorialStrings tutorialStrings = CardCrawlGame.languagePack
				.getTutorialString(AliceSpireKit.getModID() + "Raw");
		ArrayList<String> res = new ArrayList<>();
		
		for (String key : tutorialStrings.TEXT) {
			String s = key;
			
			
			String[] dollClasses = new String[] {
					"ShanghaiDoll",
					"NetherlandsDoll",
					"HouraiDoll",
					"KyotoDoll",
					"LondonDoll",
					"FranceDoll",
					"OrleansDoll"
			};
			
			for (String dollClass : dollClasses) {
				String dollName = CardCrawlGame.languagePack.getOrbString(dollClass).NAME;
				String flavor = AbstractDoll.getFlavor(dollClass);
				String desc = AbstractDoll.getDescription(dollClass);
				
				s = s.replace("{" + dollClass + "}", dollName + " (" + flavor + ") NL " + desc);
			}
			
			res.add(s);
		}
		
		generated = new TutorialStrings();
		generated.TEXT = res.toArray(new String[0]);
		
//		Map<String, TutorialStrings> map = ReflectionHacks.getPrivateStatic(
//				LocalizedStrings.class, "tutorials");
//		map.put(AliceSpireKit.getModID(), generated);
		
		AliceSpireKit.log("Tutorial generated.");
	}
	
	public static void openTutorial() {
		if (generated == null)
			generateTutorial();
		
		Texture[] images = new Texture[generated.TEXT.length];
		for (int i = 0; i < 4; i++)
			images[i] = ImageMaster.loadImage(
					AliceSpireKit.getImgFilePath("tutorial", "" + i)
			);
		for (int i = 4; i < images.length; i++)
			images[i] = ImageMaster.loadImage(
					AliceSpireKit.getImgFilePath("tutorial", "placeholder")
			);
		
		AbstractDungeon.ftue = new CustomMultiPageFtue(
				images,
				generated.TEXT
		);
	}
}
