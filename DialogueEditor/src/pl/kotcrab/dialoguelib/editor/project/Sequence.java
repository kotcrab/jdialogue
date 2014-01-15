package pl.kotcrab.dialoguelib.editor.project;

import java.io.File;
import java.util.ArrayList;

import pl.kotcrab.dialoguelib.editor.IOUtils;
import pl.kotcrab.dialoguelib.editor.components.DComponent;
import pl.kotcrab.dialoguelib.editor.components.types.StartComponent;

import com.thoughtworks.xstream.XStream;

public class Sequence
{
	private File file;
	private String name;
	
	private ArrayList<DComponent> componentList = new ArrayList<DComponent>();
	
	public Sequence(String path, String name)
	{
		file = new File(path);
		this.name = name;
		componentList.add(new StartComponent(200, 200));
	}
	
	@SuppressWarnings("unchecked")
	public void load(XStream xstream, boolean gzip)
	{
		if(gzip)
			componentList = (ArrayList<DComponent>) IOUtils.loadGzip(xstream, file);
		else
			componentList = (ArrayList<DComponent>) IOUtils.loadNormal(xstream, file);
	}
	
	public void save(XStream xstream, boolean gzip)
	{
		if(gzip)
			IOUtils.saveGzip(xstream, file, componentList);
		else
			IOUtils.saveNormal(xstream, file, componentList);
	}
	
	public ArrayList<DComponent> getComponentList()
	{
		return componentList;
	}
	
	public String getName()
	{
		return name;
	}
	

	
}