package me.antileaf.alice.utils;

import basemod.abstracts.CustomMultiPageFtue;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.TutorialStrings;

import java.util.Arrays;

public class AliceTutorialHelper {
	private static TutorialStrings generated = null;
	
	public static void generateTutorial() {
//		AliceHelper.log("Generating tutorial...");
		
		generated = CardCrawlGame.languagePack
				.getTutorialString(AliceHelper.getModID() + "Raw");
		generated.TEXT = Arrays.stream(generated.TEXT)
				.limit(3)
				.toArray(String[]::new);
	}
	
	public static void openTutorial() {
		if (generated == null)
			generateTutorial();
		
		Texture[] images = new Texture[generated.TEXT.length];
		for (int i = 0; i < 3; i++)
			images[i] = ImageMaster.loadImage(
					AliceHelper.getImgFilePath("tutorial", "" + (i + 1))
			);
		
		AbstractDungeon.ftue = new CustomMultiPageFtue(
				images,
				generated.TEXT
		);
	}
	
	public static void openMedicineTutorial() {
		TutorialStrings tutorialStrings = CardCrawlGame.languagePack
				.getTutorialString(AliceHelper.makeID("MedicineTutorial"));
		
		Texture[] images = new Texture[tutorialStrings.TEXT.length];
		for (int i = 0; i < images.length; i++)
			images[i] = ImageMaster.loadImage(AliceHelper.getImgFilePath(
					"tutorial", "medicine" + (i + 1)));
		
		AbstractDungeon.ftue = new CustomMultiPageFtue(
				images,
				tutorialStrings.TEXT
		);
	}
}
