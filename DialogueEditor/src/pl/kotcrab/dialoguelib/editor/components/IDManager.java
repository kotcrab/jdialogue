package pl.kotcrab.dialoguelib.editor.components;

import java.util.ArrayList;
import java.util.Collections;

public class IDManager
{
	private ArrayList<Integer> freeIDs = new ArrayList<>(100);
	
	private int IDCounter = 0;
	
	public IDManager()
	{
		for(int i = 0; i < 100; i++)
		{
			freeIDs.add(i + 1);
			IDCounter++;
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
		int id = freeIDs.get(0);
		freeIDs.remove(0);
		return id;
	}
}