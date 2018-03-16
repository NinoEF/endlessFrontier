package net.phenix.discord.bot.data.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
public class Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6367901240423421074L;

	private String lang;
	
	private ReportKL reportKL;

	private ReportMedal reportMedal;
	
	public Config() {
		lang = "fr";
	}
	
	@XmlRootElement(name = "reportKL")
	public static class ReportKL implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7749334756588167094L;

		private String role;

		public String getRole() {
			return role;
		}
		@XmlElement
		public void setRole(String role) {
			this.role = role;
		}
	}

	@XmlRootElement(name = "reportMedal")
	public static class ReportMedal implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6203658593774943563L;

		private String excel;
		private String sheet;
		private String rangeMedal;
		private String rangePercent;
		private String rangeName;

		public String getExcel() {
			return excel;
		}
		@XmlElement
		public void setExcel(String excel) {
			this.excel = excel;
		}

		public String getSheet() {
			return sheet;
		}
		@XmlElement
		public void setSheet(String sheet) {
			this.sheet = sheet;
		}

		public String getRangeMedal() {
			return rangeMedal;
		}
		@XmlElement
		public void setRangeMedal(String rangeMedal) {
			this.rangeMedal = rangeMedal;
		}

		public String getRangeName() {
			return rangeName;
		}
		@XmlElement
		public void setRangeName(String rangeName) {
			this.rangeName = rangeName;
		}
		public String getRangePercent() {
			return rangePercent;
		}
		@XmlElement
		public void setRangePercent(String rangePercent) {
			this.rangePercent = rangePercent;
		}
	}

	public String getLang() {
		return lang;
	}

	@XmlElement
	public void setLang(String lang) {
		this.lang = lang;
	}

	@XmlElementRef()
	public ReportKL getReportKL() {
		return reportKL;
	}

	public void setReportKL(ReportKL reportKL) {
		this.reportKL = reportKL;
	}

	@XmlElementRef()
	public ReportMedal getReportMedal() {
		return reportMedal;
	}

	public void setReportMedal(ReportMedal reportMedal) {
		this.reportMedal = reportMedal;
	}
}
