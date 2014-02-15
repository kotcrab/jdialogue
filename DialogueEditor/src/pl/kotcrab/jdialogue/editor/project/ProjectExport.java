package pl.kotcrab.jdialogue.editor.project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class created for project that is going to be exported.
 * Object is created during export process, then XStream saves it, without creating speical converter
 * 
 * @author Pawel Pastuszak
 */

@SuppressWarnings("unused") //doesn't matter, XStream will use reflection
public class ProjectExport
{
	private String name;
	private ArrayList<DCharacter> characterList;
	private HashMap<Integer, Integer> characterMap;
	
	public ProjectExport(String name, ArrayList<DCharacter> characterList, HashMap<Integer, Integer> characterMap)
	{
		this.name = name;
		this.characterList = characterList;
		this.characterMap = characterMap;
	}
}