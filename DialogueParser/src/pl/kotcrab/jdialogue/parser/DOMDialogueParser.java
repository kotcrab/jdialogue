//package pl.kotcrab.jdialogue.parser;
//
//import java.io.IOException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.xml.sax.SAXException;
//
//import pl.kotcrab.jdialogue.loader.Loader;
//
//public class DOMDialogueParser extends DialogueParser
//{
//	Document doc;
//	Element currentEl;
//	
//	public DOMDialogueParser(Loader projectFile, int maxChars)
//	{
//		super(projectFile, maxChars);
//		
//		try
//		{
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			doc = dBuilder.parse(projectFile.getFile());
//			
//			// optional, but recommended
//			doc.getDocumentElement().normalize();
//			
//			Node node = doc.getElementsByTagName("dStart").item(0);
//			Element el = (Element) node;
//			
//			String target = el.getElementsByTagName("target0").item(0).getTextContent();
//			doc.getElementById(target);
//		}
//		catch (ParserConfigurationException | SAXException | IOException e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@Override
//	public void startSequence()
//	{
//		// TODO Auto-generated method stub
//	}
//	
//	@Override
//	public ComponentType getNextType()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public String getNextMsg()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public String getNextCharacterData()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public String getNextData()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isCurrentMsgFinished()
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void nextComponent()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//	
//}