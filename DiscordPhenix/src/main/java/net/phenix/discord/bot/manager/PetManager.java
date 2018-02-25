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

public class PetManager {

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
		String[] args = content.split(" ");
		
		String exemple = "exemple : !petfc 32 3 7";
		if (args.length != 4) {
			channel.sendMessage("La commande est erronée.\n" 
			+ "Veuillez saisir : !petfc <nb fragment> <lvl pet> <nb fragment/jour>\n" 
			+ exemple).queue();
			return;
		}

		Double curFrag = null;
		Double petLevel = null;
		Double nbFragPerDay = null;
		try {
			curFrag = Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
			channel.sendMessage("La commande est erronée.\n"
					+ args[1] +" n'est pas un nombre\n"
					+ exemple).queue();
			return;
		}
		
		try{
			petLevel = Double.parseDouble(args[2]);
		} catch (NumberFormatException e) {
			channel.sendMessage("La commande est erronée.\n"
					+ args[2] +" n'est pas un nombre\n"
					+ exemple).queue();
			return;
		}
		if(petLevel < 0 || petLevel > 5){
			channel.sendMessage("La commande est erronée.\n"
					+ "<lvl pet> n'est pas correct, devrait être 0,1,2,3,4 ou 5").queue();
			return;
		}			
		
		try{
			nbFragPerDay = Double.parseDouble(args[3]);
			if(nbFragPerDay == 0){
				channel.sendMessage("La commande est erronée.\n"
						+ "<nb fragment/jour> ne peut pas être 0.").queue();
				return;
			}
		} catch (NumberFormatException e) {
			channel.sendMessage("La commande est erronée.\n"
					+ args[3] +" n'est pas un nombre\n"
					+ exemple).queue();
			return;
		}
		
		String forecast5 = petFC(curFrag, nbFragPerDay, 330.0, petLevel);
		String forecast4 = petFC(curFrag, nbFragPerDay, 180.0, petLevel);
		String forecast3 = petFC(curFrag, nbFragPerDay, 80.0, petLevel);
		String forecast2 = petFC(curFrag, nbFragPerDay, 30.0, petLevel);
		String forecast1 = petFC(curFrag, nbFragPerDay, 10.0, petLevel);
		
		
		if(petLevel == 0){				
			channel.sendMessage(bundleManager.formatNote(1)+" : "+ forecast1+"\n"
					+ bundleManager.formatNote(2)+" : "+ forecast2+"\n"
					+ bundleManager.formatNote(3)+" : "+ forecast3+"\n"
					+ bundleManager.formatNote(4)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5)+" : "+ forecast5+"\n").queue();
			return;
			
		} else if(petLevel == 1){
			channel.sendMessage( bundleManager.formatNote(2)+" : "+ forecast2+"\n"
					+ bundleManager.formatNote(3)+" : "+ forecast3+"\n"
					+ bundleManager.formatNote(4)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5)+" : "+ forecast5+"\n").queue();
			return;
		} else if(petLevel == 2){
			channel.sendMessage( bundleManager.formatNote(3)+" : "+ forecast3+"\n"
					+ bundleManager.formatNote(4)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5)+" : "+ forecast5+"\n").queue();
			return;
			
		} else if(petLevel == 3){
			channel.sendMessage( bundleManager.formatNote(4)+" : "+ forecast4+"\n"
					+ bundleManager.formatNote(5)+" : "+ forecast5+"\n").queue();
			return;
		} else if(petLevel == 4){
			channel.sendMessage( bundleManager.formatNote(5)+" : "+ forecast5+"\n").queue();
			return;
		} else if(petLevel == 5){
			channel.sendMessage("Tu peux pas avoir mieux :)").queue();
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
