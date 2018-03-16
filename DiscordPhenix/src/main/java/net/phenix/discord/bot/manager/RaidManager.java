package net.phenix.discord.bot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.phenix.discord.bot.MainBot;
import net.phenix.discord.bot.data.xml.PetList;
import net.phenix.discord.bot.data.xml.RaidList;
import net.phenix.discord.bot.data.xml.PetList.Pet;
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
