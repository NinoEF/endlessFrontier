package net.phenix.discord.bot.manager;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.phenix.discord.bot.data.UnitUpGoldList;
import net.phenix.discord.bot.data.UnitUpGoldList.UnitUpGold;

public class UnitManager {

	Logger log = Logger.getLogger(getClass());
	
	public static UnitManager getInstance() {
		return new UnitManager();
	}
	
	public static Integer MAX_GOLD_LEVEL = 3400;
	
	public Map<Integer, Map<Integer,String>> goldLevels = new HashMap<Integer, Map<Integer,String>>();
	
	public String getTotalUpgradeGoldLevel(int goldLevel, int id, String bonus) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		InputStream isunit = getClass().getResourceAsStream("/xml/unitbook.xml");
		DocumentBuilder docbuilder = factory.newDocumentBuilder();
		// Pour windows quand il y a des espace dans le chemin
		Document xmlUnit = docbuilder.parse(isunit);
		
		XPath xPathName = XPathFactory.newInstance().newXPath();
		String pathRare = "/main/unitList/unit[kindNum='"+id+"']/rare";
		
		String unitRare = xPathName.compile(pathRare).evaluate(xmlUnit, XPathConstants.STRING).toString();
		
		BigDecimal result = new BigDecimal(0);
		for (int i = 1; i < goldLevel; i++) {
			result = result.add(new BigDecimal(goldLevels.get(i).get(Integer.parseInt(unitRare))));
		}
		
		BigDecimal bonusReduction = NumberManager.getNumber(bonus);
		bonusReduction = bonusReduction.divide(new BigDecimal(100));
		
		return NumberManager.getEFFormat(result.divide(bonusReduction,10,BigDecimal.ROUND_HALF_DOWN));
	}
	
	public void init() throws ParserConfigurationException, XPathExpressionException, SAXException, IOException, JAXBException{
		
		InputStream isunit = getClass().getResourceAsStream("/xml/unitUpGoldList.xml");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(UnitUpGoldList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		UnitUpGoldList list = (UnitUpGoldList) jaxbUnmarshaller.unmarshal(isunit);
			
		int nodesLength = list.getUnitUpGolds().size();

		for (int i = 0; i < nodesLength; i++) {
			UnitUpGold n = list.getUnitUpGolds().get(i);
			
			Map<Integer,String> rares = new HashMap<>();
			rares.put(1, n.getRare1());
			rares.put(2, n.getRare2());
			rares.put(3, n.getRare3());
			rares.put(4, n.getRare4());
			rares.put(5, n.getRare5());
			rares.put(6, n.getRare6());
			rares.put(7, n.getRare7());
			rares.put(8, n.getRare8());
			rares.put(9, n.getRare9());
			rares.put(10, n.getRare10());
			
			goldLevels.put(i+1, rares);
		}
		log.info("UnitManager : Init done");
	}

	public MessageEmbed getUnitsList(String rank) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		InputStream isunit = getClass().getResourceAsStream("/xml/unitbook.xml");
		InputStream isfr = getClass().getResourceAsStream("/xml/fr.xml");

		DocumentBuilder docbuilder = factory.newDocumentBuilder();
		// Pour windows quand il y a des espace dans le chemin
		Document xmlUnit = docbuilder.parse(isunit);
		Document xmlFr = docbuilder.parse(isfr);

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Unité disponible : ");
		
		if(rank.equals("1")){
			builder.setColor(new Color(80, 80, 80));
		} else if(rank.equals("2")){
			builder.setColor(new Color(56, 139, 10));
		} else if(rank.equals("3")){
			builder.setColor(new Color(87, 16, 151));
		} else if(rank.equals("4")){
			builder.setColor(new Color(6, 64, 165));
		} else if(rank.equals("5")){
			builder.setColor(new Color(175, 2, 2));
		} else if(rank.equals("6")){
			builder.setColor(new Color(248, 150, 3));
		} 

		String kindNum = "/main/unitList/unit[rank='"+rank+"' and cost != 0]/kindNum";

		XPath xPath = XPathFactory.newInstance().newXPath();

		NodeList nodeList = (NodeList) xPath.compile(kindNum).evaluate(xmlUnit, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {

			int id = Integer.parseInt(nodeList.item(i).getFirstChild().getNodeValue()); 
			if (id < 200) {
				XPath xPathName = XPathFactory.newInstance().newXPath();
				String unitName = xPathName.compile("/main/textList/text[id='UNIT_NAME_" + id + "']/value").evaluate(xmlFr, XPathConstants.STRING)
						.toString();
				builder.appendDescription(id + " - " +unitName + " " + "\n");
			}
		}

		return builder.build();
	}

}
