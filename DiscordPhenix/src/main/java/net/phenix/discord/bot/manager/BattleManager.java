package net.phenix.discord.bot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class BattleManager {

	Logger log = Logger.getLogger(getClass());
	
	public static BattleManager getInstance(){
		return new BattleManager();
	}
	
	public BigDecimal getRevivalMedalsQuantity(Integer floor, BigDecimal bonus) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		InputStream is = getClass().getResourceAsStream("/xml/revivestatbook.xml");
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		//Pour windows quand il y a des espace dans le chemin
		Document xml = builder.parse(is);

		Element root = xml.getDocumentElement();
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();
		
		//pour arrondir à la dizaine inférieur
		floor = floor/10;
		floor = floor*10;
		
		String floorPath = "/main/reviveStarList/reviveStar[floor="+floor+"]/star";
		BigDecimal medals = new BigDecimal((String) path.evaluate(floorPath,root));

		BigDecimal total = medals.multiply(bonus.divide(new BigDecimal(100)));
		
		return total;
	}

	public BigDecimal getRevivalSpiritRest(Integer floor, BigDecimal bonus, Double time) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		BigDecimal medals = getRevivalMedalsQuantity(floor, bonus);
		
		BigDecimal sr = medals.divide(new BigDecimal(time), RoundingMode.CEILING);
		return sr;
	}

}
