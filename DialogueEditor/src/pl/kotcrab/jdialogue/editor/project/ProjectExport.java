package pl.kotcrab.jdialogue.editor.project;

import java.util.ArrayList;

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
	private ArrayList<Character> characterList;
	
	public ProjectExport(String name, ArrayList<Character> characterList)
	{
		this.name = name;
		this.characterList = characterList;
	}
}