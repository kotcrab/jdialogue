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

package pl.kotcrab.dialoguelib.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

public class Project
{
	private String name;
	private String mainDir;
	
	private String customOut;
	
	private boolean gzipProject;
	private boolean gzipExport;
	
	private File configFile;
	private ArrayList<File> projectFiles = new ArrayList<File>();
	private File activeFile = null;
	
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
	
	public void save(XStream xstream)
	{
		try
		{
			PrintWriter writer = new PrintWriter(configFile, "UTF-8");
			xstream.toXML(this, writer);
			writer.close();
		}
		catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	public void newSequence(String name, boolean setAsActive)
	{
		projectFiles.add(new File(mainDir + name + ".xml"));
		activeFile = projectFiles.get(projectFiles.size() - 1);
	}
}
