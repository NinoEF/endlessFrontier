package net.phenix.discord.bot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import net.phenix.discord.bot.data.xml.TreasureList;
import net.phenix.discord.bot.data.xml.TreasureList.Treasure;
import net.phenix.discord.bot.data.xml.TreasureSetList;
import net.phenix.discord.bot.data.xml.TreasureSetList.TreasureSet;
import net.phenix.discord.bot.data.xml.TypeList;
import net.phenix.discord.bot.data.xml.TypeList.Type;

public class TreasureManager {

	Logger log = Logger.getLogger(getClass());

	private List<TreasureList.Treasure> treasure;
	
	private List<TreasureSetList.TreasureSet> treasureSet;

	private List<Type> types;

	public static TreasureManager getInstance() {
		return new TreasureManager();
	}

	public void init() throws ParserConfigurationException, XPathExpressionException, SAXException, IOException, JAXBException {
		InputStream is = getClass().getResourceAsStream("/xml/treasure/treasureList.xml");

		JAXBContext jaxbContext = JAXBContext.newInstance(TreasureList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		TreasureList treasurelist = (TreasureList) jaxbUnmarshaller.unmarshal(is);

		treasure = treasurelist.getTreasures();
		log.info("treasureList : Init done ");

		is = getClass().getResourceAsStream("/xml/treasure/typeList.xml");

		jaxbContext = JAXBContext.newInstance(TypeList.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		TypeList typeList = (TypeList) jaxbUnmarshaller.unmarshal(is);

		types = typeList.getType();
		log.info("typeList : Init done");
		
		is = getClass().getResourceAsStream("/xml/treasure/treasureSetList.xml");

		jaxbContext = JAXBContext.newInstance(TreasureSetList.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		TreasureSetList treasureSetList = (TreasureSetList) jaxbUnmarshaller.unmarshal(is);

		treasureSet = treasureSetList.getTreasureSets();
		log.info("treasureSetList : Init done");

	}

	public TreasureSet getSetByTreasureId(String id) {
		
		for (TreasureSet set : treasureSet) {
			String itemlist = set.getItemList();
			for(String item : itemlist.split("\\|")){
				if(item.equals(id)){
					return set;
				}
			}
		}
		
		return null;
	}

	public Treasure getTreasureById(String id) {

		Treasure result = null;
		for (Treasure treasure : treasure) {
			if (treasure.getKindNum().equals(id)) {
				result = treasure;
				break;
			}
		}
		return result;
	}

	public TreasureSet getTreasureSetById(String id) {
		TreasureSet result = null;
		for (TreasureSet treasureSet : treasureSet) {
			if (treasureSet.getKindNum().equals(id)) {
				result = treasureSet;
				break;
			}
		}
		return result;
	}
	
	public String getAbilityValue(String indexType, String level) {

		Integer index = Integer.parseInt(indexType);
		Type type = types.get(index-1);
		String content = (String) type.getContent().get(0);
		
		Integer indexLevel = Integer.parseInt(level);
		return content.split("\\|")[indexLevel];
	}

	public List<TreasureList.Treasure> getTreasure() {
		return treasure;
	}

	public void setTreasure(List<TreasureList.Treasure> treasure) {
		this.treasure = treasure;
	}

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public List<TreasureSetList.TreasureSet> getTreasureSet() {
		return treasureSet;
	}

	public void setTreasureSet(List<TreasureSetList.TreasureSet> treasureSet) {
		this.treasureSet = treasureSet;
	}
	
}
