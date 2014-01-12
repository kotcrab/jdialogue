package pl.kotcrab.dialoguelib.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import pl.kotcrab.dialoguelib.editor.components.DComponent;

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
