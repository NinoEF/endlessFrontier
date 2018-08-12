package net.phenix.discord.bot.data;

public class UnitTeamWrap implements Comparable<UnitTeamWrap>{

	String code;
	Integer trans;
	Integer goldLevel;
	Integer medal;

	public UnitTeamWrap(String code, Integer trans, Integer goldLevel, Integer medal) {
		super();
		this.code = code;
		this.trans = trans;
		this.goldLevel = goldLevel;
		this.medal = medal;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getTrans() {
		return trans;
	}

	public void setTrans(Integer trans) {
		this.trans = trans;
	}

	public Integer getGoldLevel() {
		return goldLevel;
	}

	public void setGoldLevel(Integer goldLevel) {
		this.goldLevel = goldLevel;
	}

	public Integer getMedal() {
		return medal;
	}

	public void setMedal(Integer medal) {
		this.medal = medal;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof UnitTeamWrap)) {
			return false;
		}
		UnitTeamWrap u = (UnitTeamWrap) obj;
		return this.getCode().equals(u.getCode());
	}

	@Override
	public int compareTo(UnitTeamWrap o) {
		return o.getMedal().compareTo(this.getMedal());
	}

}
