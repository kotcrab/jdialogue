/*******************************************************************************
    DialogueEditor
    Copyright (C) 2013-2014 Pawel Pastuszak

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
	
	public Sequence(File file)
	{
		this.file = file;
		name = file.getName().split("\\.")[0]; //because . is reserved for regular expression
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
	
	@Override
	public String toString()
	{
		return name;
	}
}