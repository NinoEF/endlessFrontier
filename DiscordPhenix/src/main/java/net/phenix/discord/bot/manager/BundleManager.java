package net.phenix.discord.bot.manager;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BundleManager {

	public static BundleManager getInstance() {
		return new BundleManager();
	}
	
	private Document xmlFr;

	public void init() throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docbuilder = factory.newDocumentBuilder();

		InputStream isfr = getClass().getResourceAsStream("/xml/fr.xml");
		xmlFr = docbuilder.parse(isfr);
		
	}
	
	public String getBundle(String path) throws XPathExpressionException {
		XPath xPathName = XPathFactory.newInstance().newXPath();
		String value = xPathName.compile(path).evaluate(xmlFr, XPathConstants.STRING).toString();
		return value;
	}

	public Document getXmlFr() {
		return xmlFr;
	}
	
	public void setXmlFr(Document xmlFr) {
		this.xmlFr = xmlFr;
	}

}
