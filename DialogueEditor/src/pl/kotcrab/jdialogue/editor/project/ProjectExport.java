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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class created for project that is going to be exported.
 * Object is created during export process, then XStream saves it, without need to create speical converter
 * 
 * @author Pawel Pastuszak
 */
@SuppressWarnings("unused") //doesn't matter, XStream will use reflection
public class ProjectExport
{
	private String name;
	private boolean gzipExport;
	private ArrayList<PCharacter> characterList;
	private HashMap<Integer, Integer> characterMap;
	private ArrayList<PCallback> callbackList;
	private HashMap<Integer, Integer> callbackMap;
	
	public ProjectExport(String name, boolean gzipExport, ArrayList<PCharacter> characterList, HashMap<Integer, Integer> characterMap, ArrayList<PCallback> callbackList, HashMap<Integer, Integer> callbackMap)
	{
		this.name = name;
		this.gzipExport = gzipExport;
		this.characterList = characterList;
		this.characterMap = characterMap;
		this.callbackList = callbackList;
		this.callbackMap = callbackMap;
	}
}