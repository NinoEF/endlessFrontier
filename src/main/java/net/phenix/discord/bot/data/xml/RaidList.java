package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "raidList")
public class RaidList {

	private List<Raid> raids;

	@XmlElementRef()
	public List<Raid> getRaids() {
		return raids;
	}

	public void setRaids(List<Raid> raids) {
		this.raids = raids;
	}

	@XmlRootElement(name = "raid")
	public static class Raid implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4576966668456248911L;
		
		String kindNum;
		String main;
		String sub;
		String progress;
		String difficult;
		String openCost;
		String limit;
		String name;
		String desc;
		String bossKindNum;
		String level;
		String hp;
		String attack;
		String phyDefense;
		String magDefense;
		String shield;
		String plusTribe;
		String plusValue;
		String minusTribe;
		String minusValue;
		String meleeDef;
		String rangeDef;
		String guildCoin;
		String raidCoin;
		String clearGuildCoin;
		String gem;
		String petKindNum;
		String numPet;
		String petKindNum2;
		String numPet2;
		String incAttack;
		String decSpeed;
		String fireAttackMin;
		String fireAttackMax;
		String fireDefenseMin;
		String fireDefenseMax;
		String fireAttackResistMin;
		String fireAttackResistMax;
		String fireDefenseResistMin;
		String fireDefenseResistMax;
		String recommendFireResist;
		String showRecommendFireResist;
		
		public String getKindNum() {
			return kindNum;
		}
		@XmlElement
		public void setKindNum(String kindNum) {
			this.kindNum = kindNum;
		}
		public String getMain() {
			return main;
		}
		@XmlElement
		public void setMain(String main) {
			this.main = main;
		}
		public String getSub() {
			return sub;
		}
		@XmlElement
		public void setSub(String sub) {
			this.sub = sub;
		}
		public String getProgress() {
			return progress;
		}
		@XmlElement
		public void setProgress(String progress) {
			this.progress = progress;
		}
		public String getDifficult() {
			return difficult;
		}
		@XmlElement
		public void setDifficult(String difficult) {
			this.difficult = difficult;
		}
		public String getOpenCost() {
			return openCost;
		}
		@XmlElement
		public void setOpenCost(String openCost) {
			this.openCost = openCost;
		}
		public String getLimit() {
			return limit;
		}
		@XmlElement
		public void setLimit(String limit) {
			this.limit = limit;
		}
		public String getName() {
			return name;
		}
		@XmlElement
		public void setName(String name) {
			this.name = name;
		}
		public String getDesc() {
			return desc;
		}
		@XmlElement
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getBossKindNum() {
			return bossKindNum;
		}
		@XmlElement
		public void setBossKindNum(String bossKindNum) {
			this.bossKindNum = bossKindNum;
		}
		public String getLevel() {
			return level;
		}
		@XmlElement
		public void setLevel(String level) {
			this.level = level;
		}
		public String getHp() {
			return hp;
		}
		@XmlElement
		public void setHp(String hp) {
			this.hp = hp;
		}
		public String getAttack() {
			return attack;
		}
		@XmlElement
		public void setAttack(String attack) {
			this.attack = attack;
		}
		public String getPhyDefense() {
			return phyDefense;
		}
		@XmlElement
		public void setPhyDefense(String phyDefense) {
			this.phyDefense = phyDefense;
		}
		public String getMagDefense() {
			return magDefense;
		}
		@XmlElement
		public void setMagDefense(String magDefense) {
			this.magDefense = magDefense;
		}
		public String getShield() {
			return shield;
		}
		@XmlElement
		public void setShield(String shield) {
			this.shield = shield;
		}
		public String getPlusTribe() {
			return plusTribe;
		}
		@XmlElement
		public void setPlusTribe(String plusTribe) {
			this.plusTribe = plusTribe;
		}
		public String getPlusValue() {
			return plusValue;
		}
		@XmlElement
		public void setPlusValue(String plusValue) {
			this.plusValue = plusValue;
		}
		public String getMinusTribe() {
			return minusTribe;
		}
		@XmlElement
		public void setMinusTribe(String minusTribe) {
			this.minusTribe = minusTribe;
		}
		public String getMinusValue() {
			return minusValue;
		}
		@XmlElement
		public void setMinusValue(String minusValue) {
			this.minusValue = minusValue;
		}
		public String getMeleeDef() {
			return meleeDef;
		}
		@XmlElement
		public void setMeleeDef(String meleeDef) {
			this.meleeDef = meleeDef;
		}
		public String getRangeDef() {
			return rangeDef;
		}
		@XmlElement
		public void setRangeDef(String rangeDef) {
			this.rangeDef = rangeDef;
		}
		public String getGuildCoin() {
			return guildCoin;
		}
		@XmlElement
		public void setGuildCoin(String guildCoin) {
			this.guildCoin = guildCoin;
		}
		public String getRaidCoin() {
			return raidCoin;
		}
		@XmlElement
		public void setRaidCoin(String raidCoin) {
			this.raidCoin = raidCoin;
		}
		public String getClearGuildCoin() {
			return clearGuildCoin;
		}
		@XmlElement
		public void setClearGuildCoin(String clearGuildCoin) {
			this.clearGuildCoin = clearGuildCoin;
		}
		public String getGem() {
			return gem;
		}
		@XmlElement
		public void setGem(String gem) {
			this.gem = gem;
		}
		public String getPetKindNum() {
			return petKindNum;
		}
		@XmlElement
		public void setPetKindNum(String petKindNum) {
			this.petKindNum = petKindNum;
		}
		public String getNumPet() {
			return numPet;
		}
		@XmlElement
		public void setNumPet(String numPet) {
			this.numPet = numPet;
		}
		public String getPetKindNum2() {
			return petKindNum2;
		}
		@XmlElement
		public void setPetKindNum2(String petKindNum2) {
			this.petKindNum2 = petKindNum2;
		}
		public String getNumPet2() {
			return numPet2;
		}
		@XmlElement
		public void setNumPet2(String numPet2) {
			this.numPet2 = numPet2;
		}
		public String getIncAttack() {
			return incAttack;
		}
		@XmlElement
		public void setIncAttack(String incAttack) {
			this.incAttack = incAttack;
		}
		public String getDecSpeed() {
			return decSpeed;
		}
		@XmlElement
		public void setDecSpeed(String decSpeed) {
			this.decSpeed = decSpeed;
		}
		public String getFireAttackMin() {
			return fireAttackMin;
		}
		@XmlElement
		public void setFireAttackMin(String fireAttackMin) {
			this.fireAttackMin = fireAttackMin;
		}
		public String getFireAttackMax() {
			return fireAttackMax;
		}
		@XmlElement
		public void setFireAttackMax(String fireAttackMax) {
			this.fireAttackMax = fireAttackMax;
		}
		public String getFireDefenseMin() {
			return fireDefenseMin;
		}
		@XmlElement
		public void setFireDefenseMin(String fireDefenseMin) {
			this.fireDefenseMin = fireDefenseMin;
		}
		public String getFireDefenseMax() {
			return fireDefenseMax;
		}
		@XmlElement
		public void setFireDefenseMax(String fireDefenseMax) {
			this.fireDefenseMax = fireDefenseMax;
		}
		public String getFireAttackResistMin() {
			return fireAttackResistMin;
		}
		@XmlElement
		public void setFireAttackResistMin(String fireAttackResistMin) {
			this.fireAttackResistMin = fireAttackResistMin;
		}
		public String getFireAttackResistMax() {
			return fireAttackResistMax;
		}
		@XmlElement
		public void setFireAttackResistMax(String fireAttackResistMax) {
			this.fireAttackResistMax = fireAttackResistMax;
		}
		public String getFireDefenseResistMin() {
			return fireDefenseResistMin;
		}
		@XmlElement
		public void setFireDefenseResistMin(String fireDefenseResistMin) {
			this.fireDefenseResistMin = fireDefenseResistMin;
		}
		public String getFireDefenseResistMax() {
			return fireDefenseResistMax;
		}
		@XmlElement
		public void setFireDefenseResistMax(String fireDefenseResistMax) {
			this.fireDefenseResistMax = fireDefenseResistMax;
		}
		public String getRecommendFireResist() {
			return recommendFireResist;
		}
		@XmlElement
		public void setRecommendFireResist(String recommendFireResist) {
			this.recommendFireResist = recommendFireResist;
		}
		public String getShowRecommendFireResist() {
			return showRecommendFireResist;
		}
		@XmlElement
		public void setShowRecommendFireResist(String showRecommendFireResist) {
			this.showRecommendFireResist = showRecommendFireResist;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	}
}
