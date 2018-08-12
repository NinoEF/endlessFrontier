package net.phenix.discord.bot.data;

import java.math.BigDecimal;

public class QuestWrap {

	BigDecimal openGold = new BigDecimal(0);
	BigDecimal completeTime = new BigDecimal(0);
	BigDecimal upgradeGold = new BigDecimal(0);
	BigDecimal goldGain = new BigDecimal(0);

	public BigDecimal getOpenGold() {
		return openGold;
	}
	public void setOpenGold(BigDecimal openGold) {
		this.openGold = openGold;
	}
	public BigDecimal getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(BigDecimal completeTime) {
		this.completeTime = completeTime;
	}
	public BigDecimal getUpgradeGold() {
		return upgradeGold;
	}
	public void setUpgradeGold(BigDecimal upgradeGold) {
		this.upgradeGold = upgradeGold;
	}
	public BigDecimal getGoldGain() {
		return goldGain;
	}
	public void setGoldGain(BigDecimal goldGain) {
		this.goldGain = goldGain;
	}
	
	
}
