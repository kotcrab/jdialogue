package pl.kotcrab.jdialogue.editor.project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class created for project that is going to be exported.
 * Object is created during export process, then XStream saves it, without need to create speical converter
 * 
 * @author Pawel Pastuszak
 */
@SuppressWarnings("unused") //doesn't matter, XStream will use reflection
public class ProjectExport
{
	private String name;
	private boolean gzipExport;
	private ArrayList<PCharacter> characterList;
	private HashMap<Integer, Integer> characterMap;
	private ArrayList<PCallback> callbackList;
	private HashMap<Integer, Integer> callbackMap;
	
	public ProjectExport(String name, boolean gzipExport, ArrayList<PCharacter> characterList, HashMap<Integer, Integer> characterMap, ArrayList<PCallback> callbackList, HashMap<Integer, Integer> callbackMap)
	{
		this.name = name;
		this.gzipExport = gzipExport;
		this.characterList = characterList;
		this.characterMap = characterMap;
		this.callbackList = callbackList;
		this.callbackMap = callbackMap;
	}
}