package net.phenix.discord.bot.manager;

import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class BundleManager extends AbstractManager{

	public static BundleManager getInstance() {
		return new BundleManager();
	}
	
	public String getBundle(String path, String langage) {
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
			DocumentBuilder docbuilder = factory.newDocumentBuilder();
	
			InputStream is = getClass().getResourceAsStream("/xml/"+langage+".xml");
			Document xml = docbuilder.parse(is);
			XPath xPathName = XPathFactory.newInstance().newXPath();
			String value = xPathName.compile(path).evaluate(xml, XPathConstants.STRING).toString();
			return value;
		} catch (Exception e){
			return "";
		}
	}
	
	public String getBundleForProperties(String name, String langage) {
		try{
			InputStream properties = getClass().getResourceAsStream("/messages_"+langage+".properties");
			
			Properties prop = new Properties();
			prop.load(properties);	
			String value = prop.getProperty(name);
			return value;
		} catch (Exception e){
			return "";
		}
	}

	public String formatNote(Integer note, String langage) {
		String result = "";
		for (int i = 0; i < note; i++) {
			result += getBundleForProperties("star.black", langage);
		}
		for (int i = 0; i < 5 - note; i++) {
			result += getBundleForProperties("star.white", langage);
		}
		return result;
	}
}
