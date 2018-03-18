package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.List;

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
	
	private Webhooks webhooks;
	
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
	
	@XmlRootElement(name = "webhooks")
	public static class Webhooks implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7597244543169955233L;
		List<Webhook> webhooks;
		
		@XmlRootElement(name = "webhook")
		public static class Webhook implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1997814694630601460L;
			String url;
			String botname;
			String content;
			String time;
			
			Every every;
			
			@XmlRootElement(name = "every")
			public static class Every implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = -8924393510145014376L;
				Integer value;
				Integer unit;
				
				public Integer getValue() {
					return value;
				}
				@XmlElement
				public void setValue(Integer value) {
					this.value = value;
				}
				public Integer getUnit() {
					return unit;
				}
				@XmlElement
				public void setUnit(Integer unit) {
					this.unit = unit;
				}
				
			}

			public String getUrl() {
				return url;
			}
			@XmlElement
			public void setUrl(String url) {
				this.url = url;
			}

			public String getBotname() {
				return botname;
			}
			@XmlElement
			public void setBotname(String botname) {
				this.botname = botname;
			}

			public String getContent() {
				return content;
			}
			@XmlElement
			public void setContent(String content) {
				this.content = content;
			}

			public String getTime() {
				return time;
			}
			@XmlElement
			public void setTime(String time) {
				this.time = time;
			}
			@XmlElementRef()
			public Every getEvery() {
				return every;
			}

			public void setEvery(Every every) {
				this.every = every;
			}
		}
		@XmlElementRef()
		public List<Webhook> getWebhooks() {
			return webhooks;
		}

		public void setWebhooks(List<Webhook> webhooks) {
			this.webhooks = webhooks;
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
	@XmlElementRef()
	public Webhooks getWebhooks() {
		return webhooks;
	}

	public void setWebhooks(Webhooks webhooks) {
		this.webhooks = webhooks;
	}
}
