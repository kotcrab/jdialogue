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
import java.util.Arrays;

import com.badlogic.gdx.Preferences;

public class App
{
	public static final String prefsLastOpenedFolder = "lastOpenedFolder";
	
	private static Preferences prefs;
	
	
	private static String lastOpenedFolder = null;
	
	//Argmuntes
	private static int screenId = 0;
	private static File projectFile = null;
	
	public static void parseArguments(String[] args)
	{
		System.out.println("Arguments: " + Arrays.toString(args));
		
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("-screen"))
			{
				if(i + 1 > args.length - 1)
				{
					throw new ArgsParserException("-screen requires moniotr id. Example: -screen 1");
				}
				
				try
				{
					screenId = Integer.parseInt(args[i + 1]);
				}
				catch (NumberFormatException e)
				{
					System.err.println("-screen requires moniotr id. Example: -screen 1");
					e.printStackTrace();
				}
				
				i++;
				continue;
			}
			
			if(args[i].equals("-p"))
			{
				if(i + 1 > args.length - 1)
				{
					throw new ArgsParserException("-p requires moniotr path to project.xml file");
				}
				
				projectFile = new File(args[i + 1]);
				
				if(projectFile.exists() == false)
					throw new ArgsParserException("Provided file does not exist!");
				
				i++;
				continue;
				
			}
			
			throw new ArgsParserException("Unrecognized parameter: " + args[i]);
		}
	}
	
	public static void loadPrefs()
	{
		lastOpenedFolder = prefs.getString(prefsLastOpenedFolder, null);
	}
	
	public static String getLastOpenedFolderPath()
	{
		return lastOpenedFolder;
	}

	public static void setLastOpenedFolder(String lastOpenedFolder)
	{
		App.lastOpenedFolder = lastOpenedFolder;
		prefs.putString(prefsLastOpenedFolder, lastOpenedFolder);
		prefs.flush();
	}

	public static Preferences getPrefs()
	{
		return prefs;
	}

	public static void setPrefs(Preferences prefs)
	{
		App.prefs = prefs;
	}

	public static int getScreenId()
	{
		return screenId;
	}

	public static File getProjectFile()
	{
		return projectFile;
	}
}
