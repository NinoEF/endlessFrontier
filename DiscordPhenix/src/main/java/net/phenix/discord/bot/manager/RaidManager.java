package net.phenix.discord.bot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.phenix.discord.bot.data.xml.RaidList;
import net.phenix.discord.bot.data.xml.RaidList.Raid;

public class RaidManager extends AbstractManager {

	Logger log = Logger.getLogger(getClass());

	private BundleManager bundleManager;
	
	public static RaidManager getInstance() {
		return new RaidManager();
	}
	
	private List<Raid> raids;
	
	public void init(BundleManager bundleManager) throws ParserConfigurationException, XPathExpressionException, SAXException, IOException, JAXBException {
		this.setBundleManager(bundleManager);
		
		InputStream is = getClass().getResourceAsStream("/xml/raidList.xml");

		JAXBContext jaxbContext = JAXBContext.newInstance(RaidList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		RaidList raidList = (RaidList) jaxbUnmarshaller.unmarshal(is);

		setRaids(raidList.getRaids());
		log.info("raidList : Init done ");

	}
	
	public void displayRaid(MessageChannel channel, String content) {
		
		String lang = ConfigManager.getConfig(event).getLang();
		
		content = content.substring("!raid".length());
		String main = content.split("")[0];
		String difficult =  content.split("")[1];
		String sub =  content.split("")[2];
		
		String message  = "";
		Raid raid = getRaidById(main, difficult, sub );
		if(raid != null){
		
			message += bundleManager.getBundle("/main/textList/text[id='UNIT_NAME_" + raid.getBossKindNum() + "']/value",lang) + "\n";
			
			message += bundleManager.getBundleForProperties("message.info.raid.nbfrag", lang) +" "+ bundleManager.getBundle("/main/textList/text[id='PET_NAME_" + raid.getPetKindNum() + "']/value",lang)+ " : "+raid.getNumPet() + "\n";
			if (!raid.getPetKindNum2().equals("0")) {
				message += bundleManager.getBundleForProperties("message.info.raid.nbfrag", lang) +" "+ bundleManager.getBundle("/main/textList/text[id='PET_NAME_" + raid.getPetKindNum2() + "']/value",lang) + " : "+raid.getNumPet2()+"\n";
			}

			if (!raid.getRecommendFireResist().equals("0")) {
				message += bundleManager.getBundleForProperties("message.info.raid.minresit", lang)+" : " + raid.getRecommendFireResist() + "\n";
			}
			message +=  bundleManager.getBundleForProperties("message.info.raid.lvl", lang)+" : " + raid.getLevel() + "\n";
			message +=  bundleManager.getBundleForProperties("message.info.raid.hp", lang)+" : " + NumberManager.getEFNumber(new BigDecimal(raid.getHp())) + "\n";
			message +=  bundleManager.getBundleForProperties("message.info.raid.raidcoin", lang)+" : " + raid.getRaidCoin() + "\n";
			message +=  bundleManager.getBundleForProperties("message.info.raid.guildcoin", lang)+" : " + raid.getGuildCoin() + "\n";
			message += bundleManager.getBundleForProperties("message.info.raid.gem", lang)+" : " + raid.getGem() + "\n";
			
			
		} else {
			message = "Raid inconnu.";
		}
		
		channel.sendMessage(message).queue();
	}
	
	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	public List<Raid> getRaids() {
		return raids;
	}

	public void setRaids(List<Raid> raids) {
		this.raids = raids;
	}

	public Raid getRaidById(String main, String difficult, String sub) {
		
		List<Raid> result = raids.stream().filter(r -> r.getMain().equals(main) && r.getDifficult().equals(difficult) && r.getSub().equals(sub)).collect(Collectors.toList());
		if(result != null && !result.isEmpty()){
			return result.get(0);
		}
		return null;
		
	}

}
