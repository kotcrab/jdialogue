package pl.kotcrab.jdialogue.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Project
{
	private String name;
	private boolean gzipExport;
	private ArrayList<PCharacter> characterList;
	private HashMap<Integer, Integer> characterMap;
	private ArrayList<PCallback> callbackList;
	private HashMap<Integer, Integer> callbackMap;
	
	public Project(String name, boolean gzipExport, ArrayList<PCharacter> characterList, HashMap<Integer, Integer> characterMap, ArrayList<PCallback> callbackList, HashMap<Integer, Integer> callbackMap)
	{
		this.name = name;
		this.gzipExport = gzipExport;
		this.characterList = characterList;
		this.characterMap = characterMap;
		this.callbackList = callbackList;
		this.callbackMap = callbackMap;
	}

	public String getName()
	{
		return name;
	}

	public boolean isGzipExport()
	{
		return gzipExport;
	}

	public ArrayList<PCharacter> getCharacterList()
	{
		return characterList;
	}

	public HashMap<Integer, Integer> getCharacterMap()
	{
		return characterMap;
	}

	public ArrayList<PCallback> getCallbackList()
	{
		return callbackList;
	}

	public HashMap<Integer, Integer> getCallbackMap()
	{
		return callbackMap;
	}
}