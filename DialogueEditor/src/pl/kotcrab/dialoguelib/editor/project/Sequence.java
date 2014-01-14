package pl.kotcrab.dialoguelib.editor.project;

import java.io.File;
import java.util.ArrayList;

import pl.kotcrab.dialoguelib.editor.components.DComponent;
import pl.kotcrab.dialoguelib.editor.components.types.StartComponent;

import com.thoughtworks.xstream.XStream;

public class Sequence
{
	private File file;
	
	private ArrayList<DComponent> componentList = new ArrayList<DComponent>();
	
	public Sequence(String path)
	{
		file = new File(path);
		componentList.add(new StartComponent(200, 200));
	}
	
	public void load(XStream xstream, boolean gzip)
	{
		
	}

	public ArrayList<DComponent> getComponentList()
	{
		return componentList;
	}
	
}