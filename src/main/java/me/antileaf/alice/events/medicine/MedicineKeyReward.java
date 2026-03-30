package me.antileaf.alice.events.medicine;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

public class MedicineKeyReward extends CustomReward {
	ObtainKeyEffect.KeyColor color;
	public MedicineKeyReward nextLink = null, prevLink = null, otherLink = null;
	
	public MedicineKeyReward(ObtainKeyEffect.KeyColor color) {
		super(
				(Texture) null,
				LittleSweetPoison.eventStrings.DESCRIPTIONS[7 + color.ordinal()],
				Enums.MEDICINE_KEY_REWARD
		);
		
		this.color = color;
		
		String colorStr = color == ObtainKeyEffect.KeyColor.RED ? "ruby" :
				color == ObtainKeyEffect.KeyColor.GREEN ? "emerald" : "sapphire";
		this.img = ImageMaster.loadImage("images/relics/" + colorStr + "_key.png");
		this.outlineImg = ImageMaster.loadImage("images/relics/outline/" + colorStr + "_key.png");
	}
	
	@Override
	public boolean claimReward() {
		if (!this.ignoreReward)
			AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(this.color));
		
		for (RewardItem item : AbstractDungeon.combatRewardScreen.rewards)
			if (item == this.nextLink || item == this.prevLink || item == this.otherLink) {
				MedicineKeyReward other = (MedicineKeyReward) item;
				
				other.isDone = true;
				other.ignoreReward = true;
			}
		
		return true;
	}
	
	@Override
	public void render(SpriteBatch sb) {
		if (this.hb.hovered) {
			sb.setColor(new Color(0.4F, 0.6F, 0.6F, 1.0F));
		} else {
			sb.setColor(new Color(0.5F, 0.6F, 0.6F, 0.8F));
		}
		
		if (this.hb.clickStarted) {
			sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float) Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.xScale * 0.98F, Settings.scale * 0.98F, 0.0F, 0, 0, 464, 98, false, false);
		} else {
			sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float)Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.xScale, Settings.scale, 0.0F, 0, 0, 464, 98, false, false);
		}
		
		if (this.flashTimer != 0.0F) {
			sb.setColor(0.6F, 1.0F, 1.0F, this.flashTimer * 1.5F);
			sb.setBlendFunction(770, 1);
			sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float)Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale * 1.03F, Settings.scale * 1.15F, 0.0F, 0, 0, 464, 98, false, false);
			sb.setBlendFunction(770, 771);
		}
		
		ReflectionHacks.privateMethod(RewardItem.class, "renderKey", SpriteBatch.class)
				.invoke(this, sb);
		
		if (this.prevLink != null)
			ReflectionHacks.privateMethod(RewardItem.class, "renderRelicLink", SpriteBatch.class)
					.invoke(this, sb);
		
		Color c = Settings.CREAM_COLOR.cpy();
		if (this.hb.hovered) {
			c = Settings.GOLD_COLOR.cpy();
		}
		
		FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, this.text, (float)Settings.WIDTH * 0.434F, this.y + 5.0F * Settings.scale, 1000.0F * Settings.scale, 0.0F, c);
		if (!this.hb.hovered) {
			for(AbstractGameEffect e : this.effects) {
				e.render(sb);
			}
		}
		
		this.hb.render(sb);
	}
	
	public static class Enums {
		@SpireEnum
		public static RewardType MEDICINE_KEY_REWARD;
	}
}
