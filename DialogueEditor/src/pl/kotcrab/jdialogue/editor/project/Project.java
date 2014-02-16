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
import java.util.HashMap;

import javax.swing.JOptionPane;

import pl.kotcrab.jdialogue.editor.Editor;
import pl.kotcrab.jdialogue.editor.IOUtils;
import pl.kotcrab.jdialogue.editor.components.DComponentConverter;
import pl.kotcrab.jdialogue.editor.components.IDManager;
import pl.kotcrab.jdialogue.editor.gui.StatusBar;

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
	
	private File configFile;
	
	@XStreamOmitField
	private ArrayList<Sequence> sequences = new ArrayList<Sequence>();
	@XStreamOmitField
	private Sequence activeSequence = null;
	
	private String activeSequenceName = null;
	
	private ArrayList<PCharacter> characters = new ArrayList<PCharacter>();
	private ArrayList<Callback> callbacks = new ArrayList<Callback>();
	
	@XStreamOmitField
	private ProjectCallback listener;
	
	private IDManager projectIDManager;
	
	public Project(String projectName, String projectMainDir, boolean gzipProject, boolean gzipExport)
	{
		name = projectName;
		mainDir = projectMainDir;
		
		this.gzipProject = gzipProject;
		this.gzipExport = gzipExport;
		
		prepareProjectPaths();
		
		projectIDManager = new IDManager();
		
		characters.add(new PCharacter(0, "None (default character)", "none"));
		callbacks.add(new Callback(0, "Default callback"));
	}
	
	private void prepareProjectPaths()
	{
		if(mainDir.endsWith(File.separator) == false) mainDir += File.separator;
		if(customOut != null && customOut.endsWith(File.separator) == false) customOut += File.separator;
		
		configFile = new File(mainDir + "project.xml");
		
		if(customOut == null) new File(mainDir + "out").mkdir();
	}
	
	public void setCustomOut(String projectOut)
	{
		this.customOut = projectOut;
	}
	
	public void loadProject(File projectConfigFile, XStream xstream)
	{
		sequences = new ArrayList<Sequence>();
		configFile = projectConfigFile;
		mainDir = configFile.getParent();
		if(mainDir.endsWith(File.separator) == false) mainDir += File.separator;
		
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
		
		activeSequence = sequences.get(0);
		activeSequence.load(xstream, gzipProject);
	}
	
	public void refreshSequences()
	{
		File[] list = new File(mainDir).listFiles();
		for(int i = 0; i < list.length; i++)
		{
			if(list[i].getName().equals("project.xml") || list[i].getName().equals("out")) continue;
			
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
	
	public void renameSequence(XStream xstream, Sequence seq, String newName)
	{
		boolean activeSequenceNameNeedUpdate = false;
		
		if(seq.getName().equals(activeSequenceName))
			activeSequenceNameNeedUpdate = true;
		
		seq.rename(xstream, gzipExport, newName);
		
		if(activeSequenceNameNeedUpdate)
			activeSequenceName = seq.getName();
	}
	
	public void newSequence(String name, boolean setAsActive)
	{
		sequences.add(new Sequence(mainDir + name + ".xml", name));
		
		if(setAsActive)
		{
			activeSequence = sequences.get(sequences.size() - 1);
			activeSequenceName = activeSequence.getName();
		}
	}
	
	public void switchActiveSequence(String name)
	{
		for(Sequence seq : sequences)
		{
			if(seq.getName().equals(name))
			{
				activeSequence = seq;
				activeSequenceName = seq.getName();
				listener.sequenceChanged(activeSequence); // sequence.load is NOT called before and MUST be called inside thic function
			}
		}
	}
	
	public void loadActiveSequence(XStream xstream)
	{
		if(activeSequence.isSaved() == false && activeSequence.isLoaded() == true) activeSequence.save(xstream, gzipProject);
		
		activeSequence.load(xstream, gzipProject);
	}
	
	public void newCharacter(String name, String textureName)
	{
		characters.add(new PCharacter(projectIDManager.getFreeId(), name, textureName));
	}
	
	public boolean deleteCharacter(PCharacter character)
	{
		projectIDManager.freeID(character.getId());
		return characters.remove(character);
	}
	
	public void newCallback(String name)
	{
		callbacks.add(new Callback(projectIDManager.getFreeId(), name));
	}
	
	public boolean deleteCallback(Callback callback)
	{
		projectIDManager.freeID(callback.getId());
		return callbacks.remove(callback);
	}
	
	public void exportProject(XStream xstream, StatusBar statusLabel) // TODO export all sequencees and project file
	{
		DComponentConverter.exportMode = true;
		
		if(customOut != null && customOut.equals("")) customOut = null;
		
		if(customOut != null)
			IOUtils.saveNormal(xstream, new File(customOut + "project.xml"), new ProjectExport(name, characters, buildCharacterMap()));
		else
			IOUtils.saveNormal(xstream, new File(mainDir + "out" + File.separator + "project.xml"), new ProjectExport(name, characters, buildCharacterMap()));
		
		int failedToExport = 0;
		
		for(Sequence seq : sequences)
		{
			if(seq.isLoaded() == false) seq.load(xstream, gzipProject);
			
			if(customOut != null)
			{
				if(seq.export(xstream, gzipExport, customOut) == false) failedToExport++;
			}
			else
			{
				if(seq.export(xstream, gzipExport, mainDir + "out" + File.separator) == false) failedToExport++;
			}
		}
		
		if(failedToExport > 0)
		{
			JOptionPane.showMessageDialog(Editor.window, "Finished exporting with errors. Sequences not exported: " + failedToExport, "Export", JOptionPane.WARNING_MESSAGE);
			statusLabel.setStatusText("Finished exporting with errors. Sequences not exported: " + failedToExport);
			
		}
		else
			statusLabel.setStatusText("Finished exporting");
		
		DComponentConverter.exportMode = false;
	}
	
	private HashMap<Integer, Integer> buildCharacterMap()
	{
		HashMap<Integer, Integer> characterMap = new HashMap<>();
		
		for(int i = 0; i < characters.size(); i++)
			characterMap.put(characters.get(i).getId(), i);
		
		return characterMap;
	}
	
	public Sequence getActiveSequence()
	{
		return activeSequence;
	}
	
	public ArrayList<Sequence> getSequences()
	{
		return sequences;
	}
	
	public ArrayList<PCharacter> getCharacters()
	{
		return characters;
	}
	
	public ArrayList<Callback> getCallbacks()
	{
		return callbacks;
	}
	
	public void setListener(ProjectCallback listener)
	{
		this.listener = listener;
	}
	
}
