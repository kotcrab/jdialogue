package pl.kotcrab.jdialogue.parser.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pl.kotcrab.jdialogue.loader.DialogueLoader;
import pl.kotcrab.jdialogue.parser.CallbackListener;
import pl.kotcrab.jdialogue.parser.ComponentType;
import pl.kotcrab.jdialogue.parser.DialogueParser;
import pl.kotcrab.jdialogue.parser.PCallback;
import pl.kotcrab.jdialogue.parser.PCharacter;
import pl.kotcrab.jdialogue.parser.Project;

public class JDOMDialogueParser extends DialogueParser
{
	private Random random = new Random();
	
	private Project project;
	private String projectPath;
	
	private List<Element> elementList;
	private int target;
	private Element currentElement;
	
	private boolean lastCallbackCheckResult;
	
	private ArrayList<CallbackListener> listeners = new ArrayList<>();
	
	public JDOMDialogueParser(DialogueLoader projectFile, int maxChars)
	{
		super(projectFile, maxChars);
		
		project = loadProject(projectFile);
		projectPath = projectFile.getFile().getParent() + File.separator;
	}
	
	@Override
	public void startSequence(String name)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new File(projectPath + name + ".xml"));
			Element rootNode = document.getRootElement();
			
			elementList = rootNode.getChildren();
			
			Element startNode = rootNode.getChildren("dStart").get(0);
			target = Integer.valueOf(startNode.getChildText("target0"));
		}
		catch (JDOMException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public ComponentType getNextType()
	{
		currentElement = elementList.get(target);
		
		String name = currentElement.getName();
		
		if(name.equals("dText")) return ComponentType.TEXT;
		if(name.equals("dEnd")) return ComponentType.END;
		if(name.equals("dChoice")) return ComponentType.CHOICE;
		if(name.equals("dRandom")) return ComponentType.RANDOM;
		if(name.equals("dRelay")) return ComponentType.RELAY;
		
		if(name.startsWith("dCallback"))
		{
			int id = Integer.parseInt(currentElement.getChildText("callback"));
			String callbackText = project.getCallbackList().get(project.getCallbackMap().get(id)).getName();
			
			if(name.equals("dCallback"))
			{
				for(CallbackListener lis : listeners)
					lis.handleCallback(callbackText);
				
				return ComponentType.CALLBACK;
			}
			
			if(name.equals("dCallbackCheck"))
			{
				for(CallbackListener lis : listeners)
					lastCallbackCheckResult = lis.handleCallbackCheck(callbackText);
				
				return ComponentType.CBCHECK;
			}
		}
		
		return null;
	}
	
	@Override
	public void nextComponent(int target)
	{
		this.target = Integer.valueOf(currentElement.getChildText("target" + target));
	}
	
	@Override
	public void nextComponent()
	{
		nextComponent(0);
	}
	
	@Override
	public void processCallbackCheck()
	{
		if(lastCallbackCheckResult == true)
			nextComponent(0);
		else
			nextComponent(1);
		
	}
	
	@Override
	public String getNextMsg() // TODO implement maxChars
	{
		return currentElement.getChildText("text");
	}
	
	@Override
	public String[] getChoiceData()
	{
		List<Element> choiceList = currentElement.getChildren("choiceData").get(0).getChildren("string");
		String[] choiceData = new String[choiceList.size()];
		
		for(int i = 0; i < choiceData.length; i++)
			choiceData[i] = choiceList.get(i).getText();
		
		return choiceData;
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
	
	@Override
	public void processRandom()
	{
		List<Element> randomList = currentElement.getChildren();
		target = Integer.parseInt(randomList.get(1 + random.nextInt(randomList.size() - 1)).getText());
	}
	
	@Override
	public void addCallbackListener(CallbackListener listener)
	{
		listeners.add(listener);
	}
	
	@Override
	public boolean removeCallbackListener(CallbackListener listener)
	{
		return listeners.remove(listener);
	}
	
	// =====================================LOADING PROJECT========================================
	
	private Project loadProject(DialogueLoader projectFile)
	{
		String name = "";
		boolean gzipExport = false;
		ArrayList<PCharacter> characterList = new ArrayList<>();
		HashMap<Integer, Integer> characterMap = new HashMap<>();
		ArrayList<PCallback> callbackList = new ArrayList<>();
		HashMap<Integer, Integer> callbackMap = new HashMap<>();
		
		try
		{
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(projectFile.getFile());
			Element rootNode = document.getRootElement();
			
			name = rootNode.getChildText("name");
			gzipExport = Boolean.valueOf(rootNode.getChildText("gzipExport"));
			
			List<Element> characterElementList = rootNode.getChild("characterList").getChildren();
			for(Element el : characterElementList)
				characterList.add(loadCharacter(el));
			
			List<Element> callbackElementList = rootNode.getChild("callbackList").getChildren();
			for(Element el : callbackElementList)
				callbackList.add(loadCallback(el));
			
			List<Element> characterHaspMapElementList = rootNode.getChild("characterMap").getChildren();
			for(Element el : characterHaspMapElementList)
				characterMap.put(Integer.valueOf(el.getChildren().get(0).getText()), Integer.valueOf(el.getChildren().get(1).getText()));
			
			List<Element> callbackHaspMapElementList = rootNode.getChild("callbackMap").getChildren();
			for(Element el : callbackHaspMapElementList)
				callbackMap.put(Integer.valueOf(el.getChildren().get(0).getText()), Integer.valueOf(el.getChildren().get(1).getText()));
			
		}
		catch (JDOMException | IOException e)
		{
		}
		
		return new Project(name, gzipExport, characterList, characterMap, callbackList, callbackMap);
	}
	
	private PCallback loadCallback(Element callbackElement)
	{
		int id = Integer.valueOf(callbackElement.getChildText("id"));
		String name = callbackElement.getChildText("name");
		
		return new PCallback(id, name);
	}
	
	private PCharacter loadCharacter(Element characterElement)
	{
		int id = Integer.valueOf(characterElement.getChildText("id"));
		String name = characterElement.getChildText("name");
		String textureName = characterElement.getChildText("textureName");
		
		return new PCharacter(id, name, textureName);
	}
	
}
