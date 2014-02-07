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

package pl.kotcrab.jdialogue.editor.project;

import java.io.File;
import java.util.ArrayList;

import pl.kotcrab.jdialogue.editor.IOUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Project
{
	private String name;
	
	@XStreamOmitField
	private String mainDir;
	
	private String customOut;
	
	private boolean gzipProject;
	private boolean gzipExport;
	
	@XStreamOmitField
	private File configFile;
	
	@XStreamOmitField
	private ArrayList<Sequence> sequences = new ArrayList<Sequence>();
	@XStreamOmitField
	private Sequence activeSequence = null;
	
	private String activeSequenceName = null;
	
	public Project(String projectName, String projectMainDir, boolean gzipProject, boolean gzipExport)
	{
		name = projectName;
		mainDir = projectMainDir;
		
		this.gzipProject = gzipProject;
		this.gzipExport = gzipExport;
		
		prepareProject();
	}
	
	private void prepareProject()
	{
		if(mainDir.endsWith(File.separator) == false) mainDir += File.separator;
		if(customOut.endsWith(File.separator) == false) customOut  += File.separator;
		
		configFile = new File(mainDir + "project.xml");
		
		if(customOut == null) new File(mainDir + "out").mkdir();
	}
	
	public void setCustomOut(String projectOut)
	{
		this.customOut = projectOut;
	}
	
	public void newProject()
	{
	}
	
	public void loadProject(File projectConfigFile, XStream xstream)
	{
		sequences = new ArrayList<Sequence>();
		configFile = projectConfigFile;
		mainDir = configFile.getParent();
		if(mainDir.endsWith(File.separator) == false) mainDir += File.separator;
		
		if(customOut != null && customOut.equals(""))
			customOut = null;
		
		refreshSequences();
		
		for(Sequence seq : sequences)
		{
			if(seq.getName().equals(activeSequenceName))
			{
				activeSequence = seq;
				activeSequence.load(xstream, gzipProject);
				return;
			}
		}
		
		System.out.println("error");
		// TODO throw exception, active seq not found, project is broken etc.
	}
	
	public void refreshSequences()
	{
		File[] list = new File(mainDir).listFiles();
		for(int i = 0; i < list.length; i++)
		{
			if(list[i].getName().equals("project.xml") || list[i].getName().equals("out") ) continue;
			
			sequences.add(new Sequence(list[i]));
		}
	}
	
	public void save(XStream xstream)
	{
		IOUtils.saveNormal(xstream, configFile, this);
		saveActiveSeqeunce(xstream);
	}
	
	public void saveActiveSeqeunce(XStream xstream)
	{
		activeSequence.save(xstream, gzipProject);
	}
	
	public void newSequence(String name, boolean setAsActive)
	{
		sequences.add(new Sequence(mainDir + name + ".xml", name));
		activeSequence = sequences.get(sequences.size() - 1);
		activeSequenceName = activeSequence.getName();
	}
	
	public Sequence getActiveSequence()
	{
		return activeSequence;
	}
	
	public ArrayList<Sequence> getSequences()
	{
		return sequences;
	}
	
	public void exportProject(XStream xstream) // TODO export all sequencees and project file
	{
		if(customOut != null)
			activeSequence.export(xstream, gzipExport, customOut);
		else
			activeSequence.export(xstream, gzipExport, mainDir + "out" + File.separator);
	}
}
