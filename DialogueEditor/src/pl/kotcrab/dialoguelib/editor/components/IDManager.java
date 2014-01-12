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

package pl.kotcrab.dialoguelib.editor.components;

import java.util.ArrayList;
import java.util.Collections;

public class IDManager
{
	private ArrayList<Integer> freeIDs = new ArrayList<>(100);
		
	private int IDCounter = 0;
	
	public IDManager()
	{
		genertesIDs();
	}
	
	private void genertesIDs()
	{
		int targetIDCounter = IDCounter + 100;
		for(; IDCounter < targetIDCounter; IDCounter++)
		{
			freeIDs.add(IDCounter + 1);
		}
	}
	
	public void freeID(int id)
	{
		if(freeIDs.contains(id))
			return;
		else
			freeIDs.add(id);
		
		Collections.sort(freeIDs);
	}
	
	public int getFreeId()
	{
		if(freeIDs.size() == 0) genertesIDs(); //we ran out of ids, generate more!
		
		int id = freeIDs.get(0);
		freeIDs.remove(0);
		return id;
	}
}