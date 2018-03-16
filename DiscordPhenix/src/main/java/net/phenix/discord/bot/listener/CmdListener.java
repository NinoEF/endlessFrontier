package net.phenix.discord.bot.listener;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.xml.sax.SAXException;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.io.Files;

import gui.ava.html.image.generator.HtmlImageGenerator;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Message.Attachment;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.PrivateChannelImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import net.phenix.discord.bot.data.ArtefactSetWrap;
import net.phenix.discord.bot.data.ArtefactWrap;
import net.phenix.discord.bot.data.PetWrap;
import net.phenix.discord.bot.data.Progres;
import net.phenix.discord.bot.data.UnitTeamWrap;
import net.phenix.discord.bot.data.UnitWrap;
import net.phenix.discord.bot.data.xml.Config;
import net.phenix.discord.bot.data.xml.Config.ReportMedal;
import net.phenix.discord.bot.data.xml.RaidList.Raid;
import net.phenix.discord.bot.manager.BattleManager;
import net.phenix.discord.bot.manager.BuildManager;
import net.phenix.discord.bot.manager.BundleManager;
import net.phenix.discord.bot.manager.ConfigManager;
import net.phenix.discord.bot.manager.NumberManager;
import net.phenix.discord.bot.manager.PetManager;
import net.phenix.discord.bot.manager.RaidManager;
import net.phenix.discord.bot.manager.SheetManager;
import net.phenix.discord.bot.manager.TreasureManager;
import net.phenix.discord.bot.manager.UnitManager;

public class CmdListener extends ListenerAdapter {

	Logger log = Logger.getLogger(getClass());

	private UnitManager unitManager;

	private SheetManager sheetManager;

	private BattleManager battleManager;

	private PetManager petManager;

	private TreasureManager treasureManager;

	private BundleManager bundleManager;

	private BuildManager buildManager;

	private RaidManager raidManager;
	
	private String lang = "";
	
	private Config config;

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		if (event.getAuthor().isBot())
			return;

		config = ConfigManager.getConfig(event);
		
		lang = config.getLang();
		
		unitManager.setEvent(event);
		sheetManager.setEvent(event);
		battleManager.setEvent(event);
		petManager.setEvent(event);
		treasureManager.setEvent(event);
		bundleManager.setEvent(event);
		buildManager.setEvent(event);
		raidManager.setEvent(event);
		
		MessageChannel channel = event.getChannel();

		// We don't want to respond to other bot accounts, including ourself
		Message message = event.getMessage();
		String content = message.getContentRaw();
		if (content.startsWith("!")) {
			log.info(event.getAuthor().getName() + " : " + content);
			if (channel.getName().startsWith("cmds") || (channel instanceof PrivateChannelImpl) || channel.getName().startsWith("dev")) {

				// getRawContent() is an atomic getter
				// getContent() is a lazy getter which modifies the content for
				// e.g.
				// console view (strip discord formatting)
				if (content.equals("!help")) {
					help(channel);

				} else if (content.equals("!ping")) {
					// Important to call .queue() on the RestAction returned by
					// sendMessage(...)
					channel.sendMessage("Pong!").queue();

				} else if (content.startsWith("!petfc")) {
					petManager.petForecast(channel, content);

				} else if (content.startsWith("!revive")) {
					revive(channel, content);

				} else if (content.startsWith("!unit")) {
					unit(channel, content);

				} else if (content.startsWith("!stat init")) {
					statInit(event.getAuthor(), channel, content);

				} else if (content.startsWith("!stat")) {
					stat(event.getAuthor(), channel, content);

				} else {
					channel.sendMessage(bundleManager.getBundleForProperties("message.error.commande.inconnu", lang)).queue();
				}
			} else if (channel.getName().startsWith("progres") || channel.getName().startsWith("dev")) {

				progres(event.getGuild().getName(), channel, content);
			} else {
				channel.sendMessage(bundleManager.getBundleForProperties("message.error.mauvais.channel", lang)).queue();
			}
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;
			
		config = ConfigManager.getConfig(event);
		
		lang = config.getLang();
		
		unitManager.setEvent(event);
		sheetManager.setEvent(event);
		battleManager.setEvent(event);
		petManager.setEvent(event);
		treasureManager.setEvent(event);
		bundleManager.setEvent(event);
		buildManager.setEvent(event);
		raidManager.setEvent(event);
		
		MessageChannel channel = event.getChannel();

		// We don't want to respond to other bot accounts, including ourself
		Message message = event.getMessage();
		if (!message.getAttachments().isEmpty()) {
			if (message.getAttachments().size() != 1) {
				channel.sendMessage(bundleManager.getBundleForProperties("message.error.only.one.file", lang)).queue();
			} else {
				String content = message.getContentRaw();
				if (content.toLowerCase().startsWith("!config")) {
					
					File file = new File("/home/pi/discord/config/"+event.getGuild().getId()+".xml");
					try {
						InputStream is = message.getAttachments().get(0).getInputStream();
						byte[] buffer = new byte[is.available()];
						is.read(buffer);					 
						Files.write(buffer, file);
						
					} catch (IOException e) {
						sendErrorMessage(channel, e);
					}
					
				} else if (content.toLowerCase().startsWith("!stat")) {

					Attachment attachment = message.getAttachments().get(0);
					try {

						List<ArtefactWrap> arte = new ArrayList<>();
						List<ArtefactSetWrap> arteSet = new ArrayList<>();
						List<UnitWrap> unitsTimeShop = new ArrayList<>();
						List<UnitWrap> unitsTeamRevive = new ArrayList<>();
						List<UnitTeamWrap> unitsTeams = new ArrayList<>();

						List<PetWrap> pets = new ArrayList<>();

						Workbook workbook = new XSSFWorkbook(attachment.getInputStream());
						buildManager.build(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive, unitsTeams, sheetManager, treasureManager, petManager, null, workbook);

						// Quest Data
						String result = "";
						try {
							result = buildManager.computeQuest(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive);
							sendImage(event.getAuthor().getName(), channel, result);
						} catch (Exception e) {
							sendErrorMessage(channel, e);
							return;
						}

						// Other
						result = "";
						try {
							result = buildManager.computeMedal(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive);
							sendImage(event.getAuthor().getName(), channel, result);
						} catch (Exception e) {
							sendErrorMessage(channel, e);
							return;
						}

						result = "";
						try {
							result = buildManager.compute(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive);
							sendImage(event.getAuthor().getName(), channel, result);
						} catch (Exception e) {
							sendErrorMessage(channel, e);
							return;
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} else {
			String content = message.getContentRaw();
			if (content.startsWith("!")) {
				log.info(event.getAuthor().getName() + " : " + content);
				if (channel.getName().startsWith("cmds") || (channel instanceof PrivateChannelImpl) || channel.getName().startsWith("dev")) {

					// getRawContent() is an atomic getter
					// getContent() is a lazy getter which modifies the content
					// for
					// e.g.
					// console view (strip discord formatting)
					if (content.equals("!help")) {
						help(channel);
						return;
					} else if (content.equals("!ping")) {
						// Important to call .queue() on the RestAction returned
						// by
						// sendMessage(...)
						channel.sendMessage("Pong!").queue();
						return;
					} else if (content.toLowerCase().startsWith("!petfc")) {
						petManager.petForecast(channel, content);
						return;
					} else if (content.toLowerCase().startsWith("!revive")) {
						revive(channel, content);
						return;
					} else if (content.toLowerCase().startsWith("!unit")) {
						unit(channel, content);
						return;
					} else if (content.toLowerCase().startsWith("!stat init")) {
						statInit(event.getAuthor(), channel, content);
						return;
					} else if (content.startsWith("!stat")) {
						stat(event.getAuthor(), channel, content);
						return;
					} else if (content.toLowerCase().startsWith("!kl ")) {
						kl(event, channel, content);
						return;
					} else if (content.toLowerCase().startsWith("!guild")) {
						guild(event, channel);
						return;
					} else if (content.toLowerCase().startsWith("!raid")) {
						try {
							raid(event, channel, content);
						} catch (XPathExpressionException e) {
							sendErrorMessage(channel, e);
						}
						return;
					} else if (content.toLowerCase().startsWith("!config")) {
						
						File file = new File(String.valueOf("/home/pi/discord/config/"+event.getGuild().getId()+".xml"));
						
						MessageAction action = channel.sendFile(file, "config.xml");
						action.queue();
					} else if (content.toLowerCase().startsWith("!growth")) {
						growth(event, channel, content);
					} else	{
						channel.sendMessage(bundleManager.getBundleForProperties("message.error.commande.inconnu", lang)).queue();
						return;
					}
				} else if (channel.getName().startsWith("progres") || channel.getName().startsWith("dev")) {
					progres(event.getGuild().getName(), channel, content);

				} else {
					channel.sendMessage(bundleManager.getBundleForProperties("message.error.mauvais.channel", lang)).queue();
				}
			}
		}
	}

	private void raid(MessageReceivedEvent event, MessageChannel channel, String content) throws XPathExpressionException {
		
		content = content.substring("!raid".length());
		String main = content.split("")[0];
		String difficult =  content.split("")[1];
		String sub =  content.split("")[2];
		
		String message  = "";
		Raid raid = raidManager.getRaidById(main, difficult, sub );
		if(raid != null){
		
			message += bundleManager.getBundle("/main/textList/text[id='UNIT_NAME_" + raid.getBossKindNum() + "']/value",lang) + "\n";
			message += raid.getNumPet() + " fragment de " + bundleManager.getBundle("/main/textList/text[id='PET_NAME_" + raid.getPetKindNum() + "']/value",lang) + "\n";
			if (!raid.getPetKindNum2().equals("0")) {
				message += raid.getNumPet2() + " fragment de " + bundleManager.getBundle("/main/textList/text[id='PET_NAME_" + raid.getPetKindNum2() + "']/value",lang) + "\n";
			}

			if (!raid.getRecommendFireResist().equals("0")) {
				message += "resistance minimum : " + raid.getRecommendFireResist() + "\n";
			}
			message += "son niveau : " + raid.getLevel() + "\n";
			message += "point de vie : " + NumberManager.getEFNumber(new BigDecimal(raid.getHp())) + "\n";
			message += "piece de raid : " + raid.getRaidCoin() + "\n";
			message += "piece de guilde : " + raid.getGuildCoin() + "\n";
			message += "gemmes : " + raid.getGem() + "\n";
			
			
		} else {
			message = "Raid inconnu.";
		}
		
		channel.sendMessage(message).queue();
		
	}

	public void guild(MessageReceivedEvent event, MessageChannel channel) {
		
		Map<String, Integer> kls = new TreeMap<>();
		Integer klmin = 1000;
		Integer klmax = 0;
		Double klmoy = 0.0;

		for (Member member : event.getGuild().getMembers()) {

			String name = member.getNickname();

			boolean vip = true;
			for (Role role : member.getRoles()) {
				if (role.getName().equals(config.getReportKL().getRole())) {
					vip = false;
				}
			}

			String coupe = bundleManager.getBundleForProperties("coupe",lang);
			int index = name == null ? -1 : name.indexOf(coupe);
			if (index != -1 && !vip) {
				Number value = NumberManager.getNumber(name.substring(index + 2).trim());
				Integer kl = value.intValue();
				if (klmax < kl) {
					klmax = kl;
				}
				if (klmin > kl) {
					klmin = kl;
				}

				klmoy += kl;

				kls.put(name.substring(0, index - 1), kl);
			}
		}
		

		klmoy = klmoy / kls.size();

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (String name : kls.keySet()) {
			dataset.addValue(kls.get(name), event.getGuild().getName(), name);
		}

		JFreeChart barChart = ChartFactory.createBarChart(bundleManager.getBundleForProperties("message.report.guild.level",lang), bundleManager.getBundleForProperties("message.report.guild.member",lang), "Kl", dataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryAxis axis = barChart.getCategoryPlot().getDomainAxis();
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		
		BufferedImage img = barChart.createBufferedImage(1000, 500);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", os);
		} catch (IOException e) {
			sendErrorMessage(channel, e);
			return;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdHHmmss");
		MessageAction action = channel.sendFile(is, "Guild_" + sdf.format(new Date()) + ".png");
		action.queue();

		// répartition des kl par tranche de 10

		Map<Integer, Integer> partGraph = new TreeMap();

		for (int i = klmin; i < klmax + 10; i = i + 10) {
			int value = (i / 10) * 10;
			partGraph.put(value, 0);
		}

		for (String key : kls.keySet()) {

			Integer value = kls.get(key);
			value = (value / 10) * 10;

			int count = partGraph.get(value);
			count++;
			partGraph.put(value, count);
		}

		final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		for (Integer key : partGraph.keySet()) {
			dataset2.addValue(partGraph.get(key), event.getGuild().getName(), key);
		}

		barChart = ChartFactory.createBarChart(bundleManager.getBundleForProperties("message.report.repartition",lang), "KL", bundleManager.getBundleForProperties("message.report.repartition.number",lang), dataset2, PlotOrientation.VERTICAL, true, true, false);
		img = barChart.createBufferedImage(1000, 500);

		os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", os);
		} catch (IOException e) {
			sendErrorMessage(channel, e);
			return;
		}
		is = new ByteArrayInputStream(os.toByteArray());

		sdf = new SimpleDateFormat("yyyyMMdHHmmss");
		action = channel.sendFile(is, "Guild_" + sdf.format(new Date()) + ".png");
		action.queue();

		
		//formatter:off
		channel.sendMessage(
			bundleManager.getBundleForProperties("message.report.kl.min",lang)+" : " + klmin + "\n"
		+ bundleManager.getBundleForProperties("message.report.kl.max",lang)+" : " + klmax + "\n" 
		+ bundleManager.getBundleForProperties("message.report.kl.average",lang)+" : " + klmoy + "\n").queue();
		//formatter:on
	}

	private void growth(MessageReceivedEvent event, MessageChannel channel, String content) {
		try{
			String lang = config.getLang();
			String spreadsheetId = config.getReportMedal().getExcel();
			ValueRange responseName;
			ValueRange responseMedal;
			ValueRange responsePercent;
			List<List<Object>> valuesName = null;
			List<List<Object>> valuesMedal = null;
			List<List<Object>> valuesPercent = null;
			if (spreadsheetId != null) {
				ReportMedal reportMedal = config.getReportMedal();
				responseName = sheetManager.getService().spreadsheets().values().get(spreadsheetId, reportMedal.getSheet()+"!"+reportMedal.getRangeName()).execute();
				responseMedal = sheetManager.getService().spreadsheets().values().get(spreadsheetId, reportMedal.getSheet()+"!"+reportMedal.getRangeMedal()).execute();
				responsePercent = sheetManager.getService().spreadsheets().values().get(spreadsheetId, reportMedal.getSheet()+"!"+reportMedal.getRangePercent()).execute();
				
				valuesName = responseName.getValues();
				valuesMedal = responseMedal.getValues();
				valuesPercent = responsePercent.getValues();
			} 
			if (valuesName != null && valuesMedal != null) {
				
				Map<String, Double> partGraphMedal = new TreeMap();
				Map<String, Double> partGraphPercent = new TreeMap();
				int i = 0;
				for (List<Object> row : valuesName) {
					
					String name = (String) row.get(0);
					Double medal = 0.0;
					Double percent = 0.0;
					try{
						String smedal = ((String)valuesMedal.get(i).get(0)).replaceAll(",", ".").replaceAll("[^0-9\\.]", "");
						
						if(!smedal.isEmpty()){
							medal = Double.parseDouble(smedal.replace(",", ""));
						}
						
					} catch (Exception e){
						
					}
					try {
						String spercent = ((String) valuesPercent.get(i).get(0)).replaceAll(",", ".").replaceAll("[^0-9\\.]", "");
						if (!spercent.isEmpty()) {
							percent = Double.parseDouble(spercent.replace(",", ""));
						}
					} catch (Exception e) {

					}
					partGraphMedal.put(name, medal);
					partGraphPercent.put(name, percent);
					i++;
				}
	
				final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				partGraphMedal = partGraphMedal.entrySet().stream().sorted(Map.Entry.comparingByValue(new Comparator<Double>() {
					public int compare(Double o1, Double o2) {
						return o2.compareTo(o1);};
				})).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e2, e1) -> e1, LinkedHashMap::new));
				for (String key : partGraphMedal.keySet()) {
					dataset.addValue(partGraphMedal.get(key), event.getGuild().getName(), key);
				}
	
				JFreeChart barChart = ChartFactory.createBarChart(bundleManager.getBundleForProperties("message.report.guild.medal",lang), bundleManager.getBundleForProperties("message.report.guild.member",lang), "Medal", dataset, PlotOrientation.VERTICAL, true, true, false);
				CategoryAxis axis = barChart.getCategoryPlot().getDomainAxis();
				axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
				
				BufferedImage img = barChart.createBufferedImage(1000, 500);
	
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					ImageIO.write(img, "png", os);
				} catch (IOException e) {
					sendErrorMessage(channel, e);
					return;
				}
				InputStream is = new ByteArrayInputStream(os.toByteArray());
	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdHHmmss");
				MessageAction action = channel.sendFile(is, "Guild_Medal_" + sdf.format(new Date()) + ".png");
				action.queue();
				
				
				final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
				partGraphPercent = partGraphPercent.entrySet().stream().sorted(Map.Entry.comparingByValue(new Comparator<Double>() {
					public int compare(Double o1, Double o2) {
						return o2.compareTo(o1);};
				})).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e2, e1) -> e1, LinkedHashMap::new));
				for (String key : partGraphPercent.keySet()) {
					
					Double percent = partGraphPercent.get(key);
					String setName = event.getGuild().getName();
					if(percent < 30 ){
						setName += " <30%";
					} else if(30 < percent && percent < 50) {
						setName += " 30%-50%";
					} else {
						setName += " >50% ";
					}
					dataset2.addValue(partGraphPercent.get(key), setName, key);      
				}
	
				/* Get instance of CategoryPlot */
				CategoryPlot plot = barChart.getCategoryPlot();

				/* Change Bar colors */
				BarRenderer renderer = (BarRenderer) plot.getRenderer();

				renderer.setSeriesPaint(0, Color.red);
				renderer.setSeriesPaint(1, Color.green);
				renderer.setSeriesPaint(2, Color.blue);
				
				barChart = ChartFactory.createBarChart(bundleManager.getBundleForProperties("message.report.guild.progression", lang), bundleManager.getBundleForProperties("message.report.guild.member", lang), "%", dataset2, PlotOrientation.VERTICAL, true, true, false);
				axis = barChart.getCategoryPlot().getDomainAxis();
				axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
				
				img = barChart.createBufferedImage(1000, 500);
	
				os = new ByteArrayOutputStream();
				try {
					ImageIO.write(img, "png", os);
				} catch (IOException e) {
					sendErrorMessage(channel, e);
					return;
				}
				is = new ByteArrayInputStream(os.toByteArray());
	
				sdf = new SimpleDateFormat("yyyyMMdHHmmss");
				action = channel.sendFile(is, "Guild_Percent_" + sdf.format(new Date()) + ".png");
				action.queue();
			}
		}catch (Exception e){
			sendErrorMessage(channel, e);
		}
	}
	
	public void kl(MessageReceivedEvent event, MessageChannel channel, String content) {
		content = content.substring("!kl ".length());    
		GuildController controller = new GuildController(event.getGuild());
		String name = event.getMember().getNickname();
		if (name == null) {
			name = event.getAuthor().getName();
		}
		
		String coupe = bundleManager.getBundleForProperties("coupe", lang);
		if (name.contains(coupe)) {
			name = name.split(coupe)[0].trim();
		}
		controller.setNickname(event.getMember(), name + " "+coupe+" " + content).submit();
		String plus = "";
		try {
			Integer kl = Integer.parseInt(content);
			if (kl % 10 == 0) {
				plus = ":raised_hands: ";
			}
		} catch (NumberFormatException e) {

		}

		channel.sendMessage(name + " " + bundleManager.getBundleForProperties("message.update.kl", lang)+ content + " " + plus).queue();
		return;
	}

	public void progres(String guildName, MessageChannel channel, String content) {
		
		File directory = new File(String.valueOf("/home/pi/discord/progres"));
		File file = new File(directory.getPath() + "/" + guildName + ".txt");

		if (content.startsWith("!progres init ")) {
			String liste = content.substring("!progres init ".length());

			if (!directory.exists()) {
				directory.mkdir();
			}

			channel.sendMessage(bundleManager.getBundleForProperties("message.update.progre", lang)).queue();
			try {
				Files.write(liste, file, Charset.forName("UTF-8"));
			} catch (IOException e) {
				sendErrorMessage(channel, e);
				return;
			}
			return;
		}

		if (content.startsWith("!progres top")) {

			String limit = content.substring("!progres top".length());
			List<Progres> list = new ArrayList<>();
			try {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
					for (String line; (line = br.readLine()) != null;) {
						String[] csv = line.split(";");
						list.add(new Progres(csv[0], Integer.parseInt(csv[1])));
					}
				}
				String result = "";
				Collections.sort(list);

				int max = Integer.parseInt(limit);
				if (max > list.size()) {
					max = list.size();
				}
				for (int i = 0; i < max; i++) {
					result += bundleManager.getBundleForProperties("losange", lang) + " " + list.get(i).getName() + " " + bundleManager.getBundleForProperties("arrow", lang) + " "
							+ bundleManager.formatNote(list.get(i).getNote(), lang) + "\n";
				}
				channel.sendMessage(result).queue();
				return;
			} catch (IOException e) {
				sendErrorMessage(channel, e);
				return;
			}
		}
		if (content.startsWith("!progres ")) {

			String name = content.substring("!progres ".length());

			try {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
					for (String line; (line = br.readLine()) != null;) {
						String[] csv = line.split(";");

						if (name.equals(csv[0])) {
							String result = bundleManager.getBundleForProperties("losange", lang) + " " + name + " " + bundleManager.getBundleForProperties("arrow", lang) + " "
									+ bundleManager.formatNote(Integer.parseInt(csv[1]), lang);
							channel.sendMessage(result).queue();
							return;
						}
					}
				}
			} catch (IOException e) {
				sendErrorMessage(channel, e);
				return;
			}
		}
	}

	private void statInit(User author, MessageChannel channel, String content) {
		try {
			String[] args = content.split(" ");

			if (args.length == 2) {
				channel.sendMessage(bundleManager.getBundleForProperties("message.error.stat.noexcelid", lang)).queue();
			} else if (args.length != 3) {
				channel.sendMessage(bundleManager.getBundleForProperties("message.error.stat.toonamy.parameter", lang)).queue();
			}
			String excelId = content.substring("!stat init ".length());
			File directory = new File(String.valueOf("/home/pi/discord/user/" + author.getId()));
			if (!directory.exists()) {
				directory.mkdir();
			}
			File file = new File(directory.getPath() + "/excel_id.txt");
			if (file.exists()) {
				channel.sendMessage(bundleManager.getBundleForProperties("message.info.stat.update.link", lang)).queue();
			} else {
				channel.sendMessage(bundleManager.getBundleForProperties("message.info.stat.creation.link", lang)).queue();
			}
			Files.write(excelId, file, Charset.forName("UTF-8"));
		} catch (IOException e) {
			sendErrorMessage(channel, e);
			return;
		}
	}	
	
	public void stat(User author, MessageChannel channel, String content) {
		try {
			String[] args = content.split(" ");
			String spreadsheetId = "";
			List<ArtefactWrap> arte = new ArrayList<>();
			List<ArtefactSetWrap> arteSet = new ArrayList<>();
			List<UnitWrap> unitsTimeShop = new ArrayList<>();
			List<UnitWrap> unitsTeamRevive = new ArrayList<>();
			List<UnitTeamWrap> unitsTeams = new ArrayList<>();

			List<PetWrap> pets = new ArrayList<>();

			File file = new File("/home/pi/discord/user/" + author.getId() + "/excel_id.txt");
			if (!file.exists()) {
				channel.sendMessage(bundleManager.getBundleForProperties("message.error.stat.nolink", lang)).queue();
				return;
			}
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				spreadsheetId = br.readLine();
			} catch (IOException e) {
				sendErrorMessage(channel, e);
				return;
			}

			buildManager.build(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive, unitsTeams, sheetManager, treasureManager, petManager, spreadsheetId, null);

			if (args[1].equals("quest")) {
				// Quest Data
				String result = "";
				try {
					result = buildManager.computeQuest(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive);
					sendImage(author.getName(), channel, result);
				} catch (Exception e) {
					sendErrorMessage(channel, e);
					return;
				}

			} else if (args[1].equals("unit")) {
				// Unit Data
				String result = "";
				try {
					result = buildManager.computeUnit(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive, unitsTeams);
					channel.sendMessage(result).queue();
					// sendImage(author.getName(), channel, result);
				} catch (Exception e) {
					sendErrorMessage(channel, e);
					return;
				}

			} else {

				// Other
				String result = "";
				try {
					result = buildManager.compute(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive);
					sendImage(author.getName(), channel, result);
				} catch (Exception e) {
					sendErrorMessage(channel, e);
					return;
				}

				// Quest Data
				result = "";
				try {
					result = buildManager.computeQuest(arte, arteSet, pets, unitsTimeShop, unitsTeamRevive);
					sendImage(author.getName(), channel, result);
				} catch (Exception e) {
					sendErrorMessage(channel, e);
					return;
				}
			}

		} catch (Exception e) {
			sendErrorMessage(channel, e);
			return;
		}
	}

	public void unit(MessageChannel channel, String content) {
		String[] args = content.split(" ");

		try {
			if (args[1].toLowerCase().equals("list")) {

				if (args[2].matches("[0-9]+")) {
					channel.sendMessage(unitManager.getUnitsListByRank(args[2])).queue();
				} else {
					channel.sendMessage(unitManager.getUnitsListByTribe(args[2])).queue();
				}
			} else if (args[1].toLowerCase().equals("goldlevel")) {

				if (args.length != 5) {
					channel.sendMessage(bundleManager.getBundleForProperties("message.error.nb.parameter", lang)).queue();
					return;
				}
				Integer goldLevel = Integer.parseInt(args[2]);
				if (goldLevel > 3400) {
					channel.sendMessage(bundleManager.getBundleForProperties("message.error.unit.goldlevel.max", lang)).queue();
					;
					return;
				}

				Integer unitID = Integer.parseInt(args[3]);
				String bonusReduction = args[4];

				channel.sendMessage(unitManager.getTotalUpgradeGoldLevel(goldLevel, unitID, bonusReduction)).queue();
			}
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
			channel.sendMessage("ERREUR : " + e.getMessage()).queue();
			e.printStackTrace();
			return;
		}
	}

	public void revive(MessageChannel channel, String content) {
		try {
			String[] args = content.split(" ");
			Integer floor = Integer.parseInt(args[1]);
			if (floor > 30000) {
				channel.sendMessage(bundleManager.getBundleForProperties("message.error.revive.max", lang)).queue();
				return;
			}

			BigDecimal bonus = new BigDecimal(Integer.parseInt(args[2]));

			String response = "";

			if (args.length == 4) {
				Double time = Double.parseDouble(args[3]);
				BigDecimal total = battleManager.getRevivalSpiritRest(floor, bonus, time);

				response = NumberManager.getEFNumber(total) + "/min";
			} else {
				BigDecimal total = battleManager.getRevivalMedalsQuantity(floor, bonus);

				response = NumberManager.getEFNumber(total);
			}

			channel.sendMessage(response).queue();

		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void help(MessageChannel channel) {
		//@formatter:off
		channel.sendMessage("!ping - ...\n" 
				+ bundleManager.getBundleForProperties("message.help.petfc", lang) + "\n" 
				+ "\n"
				+ bundleManager.getBundleForProperties("message.help.revive", lang) + "\n"
				+ bundleManager.getBundleForProperties("message.help.revive.sr", lang) + "\n" 
				+ "\n"
				+ bundleManager.getBundleForProperties("message.help.unit.list.star", lang) + "\n"
				+ bundleManager.getBundleForProperties("message.help.unit.list.race", lang) + "\n" 
				+ "\n"
				+ bundleManager.getBundleForProperties("message.help.unit.goldlevel", lang) + "\n" 
				+ bundleManager.getBundleForProperties("message.help.unit.goldlevel.exemple", lang) + "\n"
				+ bundleManager.getBundleForProperties("message.help.unit.goldlevel.resultat", lang) + "\n"
				+ "\n"
				+ bundleManager.getBundleForProperties("message.help.kl", lang)).queue();
		//@formatter:on
	}

	public void sendErrorMessage(MessageChannel channel, Exception e) {
		channel.sendMessage("ERROR : " + e.getMessage()).queue();
		e.printStackTrace();
	}

	public void sendImage(String author, MessageChannel channel, String result) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadHtml(result);

		BufferedImage img = imageGenerator.getBufferedImage();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", os);
		} catch (IOException e) {
			sendErrorMessage(channel, e);
			return;
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdHHmmss");
		MessageAction action = channel.sendFile(is, "Stat_" + author + "_" + sdf.format(new Date()) + ".png");
		action.queue();

	}

	public UnitManager getUnitManager() {
		return unitManager;
	}

	public void setUnitManager(UnitManager unitManager) {
		this.unitManager = unitManager;
	}

	public SheetManager getSheetManager() {
		return sheetManager;
	}

	public void setSheetManager(SheetManager sheetManager) {
		this.sheetManager = sheetManager;
	}

	public BattleManager getBattleManager() {
		return battleManager;
	}

	public void setBattleManager(BattleManager battleManager) {
		this.battleManager = battleManager;
	}

	public PetManager getPetManager() {
		return petManager;
	}

	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public TreasureManager getTreasureManager() {
		return treasureManager;
	}

	public void setTreasureManager(TreasureManager treasureManager) {
		this.treasureManager = treasureManager;
	}

	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	public BuildManager getBuildManager() {
		return buildManager;
	}

	public void setBuildManager(BuildManager buildManager) {
		this.buildManager = buildManager;
	}

	public void setRaidManager(RaidManager raidManager) {
		this.raidManager = raidManager;
	}

}
