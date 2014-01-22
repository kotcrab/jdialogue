/*******************************************************************************
 * Copyright 2013 - 2014 Pawel Pastuszak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package pl.kotcrab.dialoguelib.editor.project;

import java.io.File;
import java.util.ArrayList;

import pl.kotcrab.dialoguelib.editor.IOUtils;

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
		configFile = new File(mainDir + "project.xml");
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
			if(list[i].getName().equals("project.xml")) continue;
			
			sequences.add(new Sequence(list[i]));
		}
	}
	
	public void save(XStream xstream)
	{
		IOUtils.saveNormal(xstream, configFile, this);
		activeSequence.save(xstream, gzipProject);
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
	
	
}
