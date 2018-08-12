package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "unitList")
public class UnitList implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5138971634541608265L;
	
	List<Unit> units;
	
	@XmlElementRef()
	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	@XmlRootElement(name = "unit")
	public static class Unit implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7228134905683176550L;
		
		String kindNum;
		String tribe;
		String className;
		String name;
		String cost;
		String shopGem;
		String evolveGem;
		String coin;
		String rare;
		String size;
		String evolKindNum;
		String attackType;
		String isAirUnit;
		String damageType;
		String hasSkill;
		String skillAttackType;
		String skillDamageType;
		String initHp;
		String incHp;
		String initDamage;
		String incDamage;
		String initPhyDef;
		String incPhyDef;
		String initMagDef;
		String incMagDef;
		String numUnitBlock;
		String moveSpeed;
		String attackSpeed;
		String skillSpeed;
		String attackRange;
		String skillRange;
		String evadePercent;
		String blockPercent;
		String criticalPercent;
		String criticalDamage;
		String splashRange;
		String splashDamage;
		String specialSkill;
		String passiveSkill;
		String reviveTime;
		String bloody;
		String explodeDie;
		String des;
		String message;
		String skillList;
		String powerList;
		String rank;
		String sex;
		String orthoGrade;
		String shop;
		String showBook;
		String ratingPosition;
		String trans;
		String material1;
		String material2;
		String material3;
		String starBuff;
		String jewelBuff;
		String groundAir;
		String offlineSpeed;
		String offlineTime;
		String hasHeart;
		String canDetect;
		String cloaking;
		String starBuffFromPet;
		String isHonor;
		String honorNumber;
		
		public String getKindNum() {
			return kindNum;
		}
		public void setKindNum(String kindNum) {
			this.kindNum = kindNum;
		}
		public String getTribe() {
			return tribe;
		}
		public void setTribe(String tribe) {
			this.tribe = tribe;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCost() {
			return cost;
		}
		public void setCost(String cost) {
			this.cost = cost;
		}
		public String getShopGem() {
			return shopGem;
		}
		public void setShopGem(String shopGem) {
			this.shopGem = shopGem;
		}
		public String getEvolveGem() {
			return evolveGem;
		}
		public void setEvolveGem(String evolveGem) {
			this.evolveGem = evolveGem;
		}
		public String getCoin() {
			return coin;
		}
		public void setCoin(String coin) {
			this.coin = coin;
		}
		public String getRare() {
			return rare;
		}
		public void setRare(String rare) {
			this.rare = rare;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		public String getEvolKindNum() {
			return evolKindNum;
		}
		public void setEvolKindNum(String evolKindNum) {
			this.evolKindNum = evolKindNum;
		}
		public String getAttackType() {
			return attackType;
		}
		public void setAttackType(String attackType) {
			this.attackType = attackType;
		}
		public String getIsAirUnit() {
			return isAirUnit;
		}
		public void setIsAirUnit(String isAirUnit) {
			this.isAirUnit = isAirUnit;
		}
		public String getDamageType() {
			return damageType;
		}
		public void setDamageType(String damageType) {
			this.damageType = damageType;
		}
		public String getHasSkill() {
			return hasSkill;
		}
		public void setHasSkill(String hasSkill) {
			this.hasSkill = hasSkill;
		}
		public String getSkillAttackType() {
			return skillAttackType;
		}
		public void setSkillAttackType(String skillAttackType) {
			this.skillAttackType = skillAttackType;
		}
		public String getSkillDamageType() {
			return skillDamageType;
		}
		public void setSkillDamageType(String skillDamageType) {
			this.skillDamageType = skillDamageType;
		}
		public String getInitHp() {
			return initHp;
		}
		public void setInitHp(String initHp) {
			this.initHp = initHp;
		}
		public String getIncHp() {
			return incHp;
		}
		public void setIncHp(String incHp) {
			this.incHp = incHp;
		}
		public String getInitDamage() {
			return initDamage;
		}
		public void setInitDamage(String initDamage) {
			this.initDamage = initDamage;
		}
		public String getIncDamage() {
			return incDamage;
		}
		public void setIncDamage(String incDamage) {
			this.incDamage = incDamage;
		}
		public String getInitPhyDef() {
			return initPhyDef;
		}
		public void setInitPhyDef(String initPhyDef) {
			this.initPhyDef = initPhyDef;
		}
		public String getIncPhyDef() {
			return incPhyDef;
		}
		public void setIncPhyDef(String incPhyDef) {
			this.incPhyDef = incPhyDef;
		}
		public String getInitMagDef() {
			return initMagDef;
		}
		public void setInitMagDef(String initMagDef) {
			this.initMagDef = initMagDef;
		}
		public String getIncMagDef() {
			return incMagDef;
		}
		public void setIncMagDef(String incMagDef) {
			this.incMagDef = incMagDef;
		}
		public String getNumUnitBlock() {
			return numUnitBlock;
		}
		public void setNumUnitBlock(String numUnitBlock) {
			this.numUnitBlock = numUnitBlock;
		}
		public String getMoveSpeed() {
			return moveSpeed;
		}
		public void setMoveSpeed(String moveSpeed) {
			this.moveSpeed = moveSpeed;
		}
		public String getAttackSpeed() {
			return attackSpeed;
		}
		public void setAttackSpeed(String attackSpeed) {
			this.attackSpeed = attackSpeed;
		}
		public String getSkillSpeed() {
			return skillSpeed;
		}
		public void setSkillSpeed(String skillSpeed) {
			this.skillSpeed = skillSpeed;
		}
		public String getAttackRange() {
			return attackRange;
		}
		public void setAttackRange(String attackRange) {
			this.attackRange = attackRange;
		}
		public String getSkillRange() {
			return skillRange;
		}
		public void setSkillRange(String skillRange) {
			this.skillRange = skillRange;
		}
		public String getEvadePercent() {
			return evadePercent;
		}
		public void setEvadePercent(String evadePercent) {
			this.evadePercent = evadePercent;
		}
		public String getBlockPercent() {
			return blockPercent;
		}
		public void setBlockPercent(String blockPercent) {
			this.blockPercent = blockPercent;
		}
		public String getCriticalPercent() {
			return criticalPercent;
		}
		public void setCriticalPercent(String criticalPercent) {
			this.criticalPercent = criticalPercent;
		}
		public String getCriticalDamage() {
			return criticalDamage;
		}
		public void setCriticalDamage(String criticalDamage) {
			this.criticalDamage = criticalDamage;
		}
		public String getSplashRange() {
			return splashRange;
		}
		public void setSplashRange(String splashRange) {
			this.splashRange = splashRange;
		}
		public String getSplashDamage() {
			return splashDamage;
		}
		public void setSplashDamage(String splashDamage) {
			this.splashDamage = splashDamage;
		}
		public String getSpecialSkill() {
			return specialSkill;
		}
		public void setSpecialSkill(String specialSkill) {
			this.specialSkill = specialSkill;
		}
		public String getPassiveSkill() {
			return passiveSkill;
		}
		public void setPassiveSkill(String passiveSkill) {
			this.passiveSkill = passiveSkill;
		}
		public String getReviveTime() {
			return reviveTime;
		}
		public void setReviveTime(String reviveTime) {
			this.reviveTime = reviveTime;
		}
		public String getBloody() {
			return bloody;
		}
		public void setBloody(String bloody) {
			this.bloody = bloody;
		}
		public String getExplodeDie() {
			return explodeDie;
		}
		public void setExplodeDie(String explodeDie) {
			this.explodeDie = explodeDie;
		}
		public String getDes() {
			return des;
		}
		public void setDes(String des) {
			this.des = des;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getSkillList() {
			return skillList;
		}
		public void setSkillList(String skillList) {
			this.skillList = skillList;
		}
		public String getPowerList() {
			return powerList;
		}
		public void setPowerList(String powerList) {
			this.powerList = powerList;
		}
		public String getRank() {
			return rank;
		}
		public void setRank(String rank) {
			this.rank = rank;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getOrthoGrade() {
			return orthoGrade;
		}
		public void setOrthoGrade(String orthoGrade) {
			this.orthoGrade = orthoGrade;
		}
		public String getShop() {
			return shop;
		}
		public void setShop(String shop) {
			this.shop = shop;
		}
		public String getShowBook() {
			return showBook;
		}
		public void setShowBook(String showBook) {
			this.showBook = showBook;
		}
		public String getRatingPosition() {
			return ratingPosition;
		}
		public void setRatingPosition(String ratingPosition) {
			this.ratingPosition = ratingPosition;
		}
		public String getTrans() {
			return trans;
		}
		public void setTrans(String trans) {
			this.trans = trans;
		}
		public String getMaterial1() {
			return material1;
		}
		public void setMaterial1(String material1) {
			this.material1 = material1;
		}
		public String getMaterial2() {
			return material2;
		}
		public void setMaterial2(String material2) {
			this.material2 = material2;
		}
		public String getMaterial3() {
			return material3;
		}
		public void setMaterial3(String material3) {
			this.material3 = material3;
		}
		public String getStarBuff() {
			return starBuff;
		}
		public void setStarBuff(String starBuff) {
			this.starBuff = starBuff;
		}
		public String getJewelBuff() {
			return jewelBuff;
		}
		public void setJewelBuff(String jewelBuff) {
			this.jewelBuff = jewelBuff;
		}
		public String getGroundAir() {
			return groundAir;
		}
		public void setGroundAir(String groundAir) {
			this.groundAir = groundAir;
		}
		public String getOfflineSpeed() {
			return offlineSpeed;
		}
		public void setOfflineSpeed(String offlineSpeed) {
			this.offlineSpeed = offlineSpeed;
		}
		public String getOfflineTime() {
			return offlineTime;
		}
		public void setOfflineTime(String offlineTime) {
			this.offlineTime = offlineTime;
		}
		public String getHasHeart() {
			return hasHeart;
		}
		public void setHasHeart(String hasHeart) {
			this.hasHeart = hasHeart;
		}
		public String getCanDetect() {
			return canDetect;
		}
		public void setCanDetect(String canDetect) {
			this.canDetect = canDetect;
		}
		public String getCloaking() {
			return cloaking;
		}
		public void setCloaking(String cloaking) {
			this.cloaking = cloaking;
		}
		public String getStarBuffFromPet() {
			return starBuffFromPet;
		}
		public void setStarBuffFromPet(String starBuffFromPet) {
			this.starBuffFromPet = starBuffFromPet;
		}
		public String getIsHonor() {
			return isHonor;
		}
		public void setIsHonor(String isHonor) {
			this.isHonor = isHonor;
		}
		public String getHonorNumber() {
			return honorNumber;
		}
		public void setHonorNumber(String honorNumber) {
			this.honorNumber = honorNumber;
		}
	}
}
