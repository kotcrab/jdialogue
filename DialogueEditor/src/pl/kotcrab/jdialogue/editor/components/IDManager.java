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

package pl.kotcrab.jdialogue.editor.components;

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
			freeIDs.add(IDCounter);
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