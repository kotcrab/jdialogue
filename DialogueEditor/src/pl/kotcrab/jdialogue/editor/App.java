/*******************************************************************************
 * DialogueEditor
 * Copyright (C) 2013-2014 Pawel Pastuszak
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package pl.kotcrab.jdialogue.editor;

import com.badlogic.gdx.Preferences;

import java.io.File;
import java.util.Arrays;

//TODO wersja przy jakiej utworzono projekt i sprawdzanie tego przy uruchomieniu
public class App {
	public static final String prefsLastOpenedFolder = "lastOpenedFolder";

	private static Preferences prefs;

	private static String lastOpenedFolder = null;

	//Argmuntes
	private static int screenId = 0;
	private static File projectFile = null;

	//TODO nie potrzeba rzucaæ wyj¹tku, ale nie ma to az tak du¿ego znaczenia
	public static void parseArguments (String[] args) {
		System.out.println("Arguments: " + Arrays.toString(args));

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-screen")) {
				if (i + 1 > args.length - 1) {
					throw new ArgsParserException("-screen requires moniotr id. Example: -screen 1");
				}

				try {
					screenId = Integer.parseInt(args[i + 1]);
				} catch (NumberFormatException e) {
					System.err.println("-screen requires moniotr id. Example: -screen 1");
					e.printStackTrace();
				}

				i++;
				continue;
			}

			if (args[i].equals("-p")) {
				if (i + 1 > args.length - 1) {
					throw new ArgsParserException("-p requires path to project.xml file");
				}

				projectFile = new File(args[i + 1]);

				if (projectFile.exists() == false)
					throw new ArgsParserException("Provided file does not exist!");

				i++;
				continue;

			}

			throw new ArgsParserException("Unrecognized parameter: " + args[i]);
		}
	}

	public static void loadPrefs () {
		lastOpenedFolder = prefs.getString(prefsLastOpenedFolder, null);
	}

	public static String getLastOpenedFolderPath () {
		return lastOpenedFolder;
	}

	public static void setLastOpenedFolder (String lastOpenedFolder) {
		App.lastOpenedFolder = lastOpenedFolder;
		prefs.putString(prefsLastOpenedFolder, lastOpenedFolder);
		prefs.flush();
	}

	public static Preferences getPrefs () {
		return prefs;
	}

	public static void setPrefs (Preferences prefs) {
		App.prefs = prefs;
	}

	public static int getScreenId () {
		return screenId;
	}

	public static File getProjectFile () {
		return projectFile;
	}
}
