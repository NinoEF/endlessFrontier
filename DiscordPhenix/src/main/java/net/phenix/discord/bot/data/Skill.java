package net.phenix.discord.bot.data;

import java.math.BigDecimal;

public class Skill {

	private String code;
	private BigDecimal bonusPet;
	private BigDecimal bonusArtefact;
	private BigDecimal bonusShop;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getBonusPet() {
		return bonusPet;
	}

	public void setBonusPet(BigDecimal bonusPet) {
		this.bonusPet = bonusPet;
	}

	public BigDecimal getBonusArtefact() {
		return bonusArtefact;
	}

	public void setBonusArtefact(BigDecimal bonusArtefact) {
		this.bonusArtefact = bonusArtefact;
	}

	public BigDecimal getBonusShop() {
		return bonusShop;
	}

	public void setBonusShop(BigDecimal bonusShop) {
		this.bonusShop = bonusShop;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Skill)) {
			return false;
		}

		Skill skill = (Skill) obj;
		return skill.getCode().equals(this.getCode());
	}

}
