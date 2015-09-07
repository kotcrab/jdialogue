/*******************************************************************************
 * Copyright 2014 Pawel Pastuszak
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package pl.kotcrab.jdialogue.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Project {
	private String name;
	private boolean gzipExport;
	private ArrayList<PCharacter> characterList;
	private HashMap<Integer, Integer> characterMap;
	private ArrayList<PCallback> callbackList;
	private HashMap<Integer, Integer> callbackMap;

	public Project (String name, boolean gzipExport, ArrayList<PCharacter> characterList, HashMap<Integer, Integer> characterMap, ArrayList<PCallback> callbackList, HashMap<Integer, Integer> callbackMap) {
		this.name = name;
		this.gzipExport = gzipExport;
		this.characterList = characterList;
		this.characterMap = characterMap;
		this.callbackList = callbackList;
		this.callbackMap = callbackMap;
	}

	public String getName () {
		return name;
	}

	public boolean isGzipExport () {
		return gzipExport;
	}

	public ArrayList<PCharacter> getCharacterList () {
		return characterList;
	}

	public HashMap<Integer, Integer> getCharacterMap () {
		return characterMap;
	}

	public ArrayList<PCallback> getCallbackList () {
		return callbackList;
	}

	public HashMap<Integer, Integer> getCallbackMap () {
		return callbackMap;
	}
}
