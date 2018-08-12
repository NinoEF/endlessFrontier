package net.phenix.discord.bot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
import net.phenix.discord.bot.data.xml.PetList.Pet;

public class PetManager extends AbstractManager {

	Logger log = Logger.getLogger(getClass());

	private BundleManager bundleManager;
	
	public static PetManager getInstance() {
		return new PetManager();
	}
	
	private List<Pet> pets;
	
	public void init(BundleManager bundleManager) throws ParserConfigurationException, XPathExpressionException, SAXException, IOException, JAXBException {
		this.setBundleManager(bundleManager);
		
		InputStream is = getClass().getResourceAsStream("/xml/pet/petList.xml");

		JAXBContext jaxbContext = JAXBContext.newInstance(PetList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		PetList petList = (PetList) jaxbUnmarshaller.unmarshal(is);

		setPets(petList.getPets());
		log.info("petList : Init done ");

	}
	
	public Pet getPetById(String id) {

		Pet result = null;
		for (Pet pet : pets) {
			if (pet.getKindNum().equals(id)) {
				result = pet;
				break;
			}
		}
		return result;
	}
	
	public Pet getPetByUnitId(String code) {
		Pet result = null;
		for (Pet pet : pets) {
			if (pet.getCouple().equals(code)) {
				result = pet;
				break;
			}
		}
		return result;
	}
	
	public String getAbilityValue(String values, String level) {
		if(level.equals("0")){
			return "0";
		}
		Integer indexLevel = Integer.parseInt(level);
		return values.split("\\|")[indexLevel-1];
	}
	
	public void petForecast(MessageChannel channel, String content) {
		
		String lang = ConfigManager.getConfig(event).getLang();
		
		String[] args = content.split(" ");
		
		String exemple = bundleManager.getBundleForProperties("message.error.petfc.example", lang);
		if (args.length != 4) {
			channel.sendMessage(bundleManager.getBundleForProperties("message.error.petfc.cmd.error", lang)+"\n" 
			+ bundleManager.getBundleForProperties("message.error.petfc.cmd.ok", lang)+"\n" 
			+ exemple).queue();
			return;
		}

		Double curFrag = null;
		Double petLevel = null;
		Double nbFragPerDay = null;
		try {
			curFrag = Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
			channel.sendMessage(bundleManager.getBundleForProperties("message.error.petfc.cmd.error", lang)+"\n"
					+ args[1] +" "+bundleManager.getBundleForProperties("message.error.petfc.not.number", lang)+"\n"
					+ exemple).queue();
			return;
		}
		
		try{
			petLevel = Double.parseDouble(args[2]);
		} catch (NumberFormatException e) {
			channel.sendMessage(bundleManager.getBundleForProperties("message.error.petfc.cmd.error", lang)+"\n"
					+ args[2] +" "+bundleManager.getBundleForProperties("message.error.petfc.not.number", lang)+"\n"
					+ exemple).queue();
			return;
		}
		if(petLevel < 0 || petLevel > 5){
			channel.sendMessage(bundleManager.getBundleForProperties("message.error.petfc.cmd.error", lang)+"\n"
					+ bundleManager.getBundleForProperties("message.error.petfc.lvlpet.error", lang) ).queue();
			return;
		}			
		
		try{
			nbFragPerDay = Double.parseDouble(args[3]);
			if(nbFragPerDay == 0){
				channel.sendMessage(bundleManager.getBundleForProperties("message.error.petfc.cmd.error", lang)+"\n"
						+ bundleManager.getBundleForProperties("message.error.petfc.frag.error", lang)).queue();
				return;
			}
		} catch (NumberFormatException e) {
			channel.sendMessage(bundleManager.getBundleForProperties("message.error.petfc.cmd.error", lang)+"\n"
					+ args[3] +" "+bundleManager.getBundleForProperties("message.error.petfc.not.number", lang)+"\n"
					+ exemple).queue();
			return;
		}
		
		String forecast5 = petFC(curFrag, nbFragPerDay, 330.0, petLevel);
		String forecast4 = petFC(curFrag, nbFragPerDay, 180.0, petLevel);
		String forecast3 = petFC(curFrag, nbFragPerDay, 80.0, petLevel);
		String forecast2 = petFC(curFrag, nbFragPerDay, 30.0, petLevel);
		String forecast1 = petFC(curFrag, nbFragPerDay, 10.0, petLevel);
		
		
		if(petLevel == 0){				
			channel.sendMessage(bundleManager.formatNote(1, lang)+" : "+ forecast1+"\n"
					+ bundleManager.formatNote(2, lang)+" : "+ forecast2+"\n"
					+ bundleManager.formatNote(3, lang)+" : "+ forecast3+"\n"
					+ bundleManager.formatNote(4, lang)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5, lang)+" : "+ forecast5+"\n").queue();
			return;
			
		} else if(petLevel == 1){
			channel.sendMessage( bundleManager.formatNote(2, lang)+" : "+ forecast2+"\n"
					+ bundleManager.formatNote(3, lang)+" : "+ forecast3+"\n"
					+ bundleManager.formatNote(4, lang)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5, lang)+" : "+ forecast5+"\n").queue();
			return;
		} else if(petLevel == 2){
			channel.sendMessage( bundleManager.formatNote(3, lang)+" : "+ forecast3+"\n"
					+ bundleManager.formatNote(4, lang)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5, lang)+" : "+ forecast5+"\n").queue();
			return;
			
		} else if(petLevel == 3){
			channel.sendMessage( bundleManager.formatNote(4, lang)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5, lang)+" : "+ forecast5+"\n").queue();
			return;
		} else if(petLevel == 4){
			channel.sendMessage( bundleManager.formatNote(5, lang)+" : "+ forecast5+"\n").queue();
			return;
		} else if(petLevel == 5){
			channel.sendMessage(bundleManager.getBundleForProperties("message.error.petfc.better", lang)).queue();
			return;
		}
	}

	private String petFC(Double curFrag, Double nbFragPerDay, Double maxFrag, Double petLevel) {
		Double totalFrag = curFrag;
		if(petLevel == 1){
			totalFrag += 10;
		} else if(petLevel == 2){
			totalFrag += 10;
			totalFrag += 20;
		} else if(petLevel == 3){
			totalFrag += 10;
			totalFrag += 20;
			totalFrag += 50;
		} else if(petLevel == 4){
			totalFrag += 10;
			totalFrag += 20;
			totalFrag += 50;
			totalFrag += 100;
		}
		
		Double missingFrag = maxFrag - totalFrag;
		Integer nbDay = ((Double)Math.ceil(missingFrag/nbFragPerDay)).intValue();
		Calendar forecast = Calendar.getInstance();
		forecast.add(Calendar.DAY_OF_YEAR, nbDay);
		SimpleDateFormat sdf = new SimpleDateFormat(MainBot.DATE_FORMATTER);
		return sdf.format(forecast.getTime());
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	

}
