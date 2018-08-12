package net.phenix.discord.bot.data;

public class UnitWrap {

	String code;
	Integer countT0;
	Integer countT1;
	Integer countT2;
	Integer countT3;
	
	public UnitWrap(String code, Integer countT0, Integer countT1, Integer countT2, Integer countT3) {
		this.code = code;
		this.countT0 = countT0;
		this.countT1 = countT1;
		this.countT2 = countT2;
		this.countT3 = countT3;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getCountT0() {
		return countT0;
	}
	public void setCountT0(Integer countT0) {
		this.countT0 = countT0;
	}
	public Integer getCountT1() {
		return countT1;
	}
	public void setCountT1(Integer countT1) {
		this.countT1 = countT1;
	}
	public Integer getCountT2() {
		return countT2;
	}
	public void setCountT2(Integer countT2) {
		this.countT2 = countT2;
	}
	public Integer getCountT3() {
		return countT3;
	}
	public void setCountT3(Integer countT3) {
		this.countT3 = countT3;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof UnitWrap)){
			return false;
		}
		UnitWrap u = (UnitWrap) obj;
		return this.getCode().equals(u.getCode());
	}
	
}
