package pl.kotcrab.jdialogue.parser;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pl.kotcrab.jdialogue.loader.Loader;

public class JDOMDialogueParser extends DialogueParser
{
	private List<Element> elementList;
	private int target;
	private Element currentElement;
	
	public JDOMDialogueParser(Loader projectFile, int maxChars)
	{
		super(projectFile, maxChars);
		
		Document document;
		try
		{
			SAXBuilder builder = new SAXBuilder();
			document = (Document) builder.build(projectFile.getFile());
			Element rootNode = document.getRootElement();

			elementList = rootNode.getChildren();
			
			Element startNode =  rootNode.getChildren("dStart").get(0);
			target = Integer.valueOf(startNode.getChildText("target0"));
		}
		catch (JDOMException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
	@Override
	public void startSequence()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public ComponentType getNextType()
	{
		currentElement = elementList.get(target);
		
		if(currentElement.getName().equals("dText"))
			return ComponentType.TEXT;
		
		if(currentElement.getName().equals("dEnd"))
			return ComponentType.END;
		
		return null;
	}
	
	@Override
	public void nextComponent()
	{
		target = Integer.valueOf(currentElement.getChildText("target0"));
	}
	
	@Override
	public String getNextMsg() //TODO implement maxChars
	{
		return currentElement.getChildText("text");
	}
	
	@Override
	public String getNextCharacterData()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getNextData()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCurrentMsgFinished()
	{
		// TODO Auto-generated method stub
		return false;
	}

	
}
