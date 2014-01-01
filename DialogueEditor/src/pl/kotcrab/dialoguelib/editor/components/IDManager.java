package pl.kotcrab.dialoguelib.editor.components;

import java.util.ArrayList;
import java.util.Collections;

public class IDManager
{
	private ArrayList<Integer> freeIDs = new ArrayList<>(100);
	
	// FIXME idcounter koniecznie!
	
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