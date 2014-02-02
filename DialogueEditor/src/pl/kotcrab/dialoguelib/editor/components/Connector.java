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
package pl.kotcrab.dialoguelib.editor.components;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Connector
{
	private float x, y;
	
	private ArrayList<Connector> targets = new ArrayList<Connector>();
	private ArrayList<Connector> targetsToRemove = new ArrayList<Connector>();
	private boolean isInput;
	
	private DComponent parrentComponent;
	
	public Connector(DComponent parrentComponent, boolean isInput)
	{
		this.x = 0;
		this.y = 0;
		this.isInput = isInput;
		this.parrentComponent = parrentComponent;
	}
	
	public void render(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(x, y, 12, 12);
	}
	
	public void renderAsSelected(ShapeRenderer shapeRenderer, Color color)
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(x, y, 12, 12);
		shapeRenderer.end();
	}
	
	public Connector getTarget()
	{
		if(targets.size() > 0)
			return targets.get(0);
		else
			return null;
	}
	
	public void detach()
	{
		for(Connector target : targets)
		{
			Connector[] targetConnectors = null;
			try
			{
				if(isInput)
					targetConnectors = target.getParrentComponent().getOutputs();
				else
					targetConnectors = target.getParrentComponent().getInputs();
			}
			catch (NullPointerException e) // target was not set, ignore
			{
				continue;
			}
			
			for(int j = 0; j < targetConnectors.length; j++) // searching for matching output connector
			{
				if(targetConnectors[j] == target) // found
				{
					Connector temp = targetConnectors[j];
					targetConnectors[j].removeTarget(this); // detach
					targetsToRemove.add(temp);
					break;
				}
			}
		}
		
		if(targetsToRemove.size() > 0)
		{
			targets.removeAll(targetsToRemove);
			targetsToRemove.clear();
		}
	}
	
	public void detachNotOnList(ArrayList<DComponent> componentList)
	{
		for(Connector target : targets)
		{
			Connector[] targetConnectors = null;
			try
			{
				if(isInput)
					targetConnectors = target.getParrentComponent().getOutputs();
				else
					targetConnectors = target.getParrentComponent().getInputs();
			}
			catch (NullPointerException e) // target was not set, ignore
			{
				continue;
			}
			
			for(int j = 0; j < targetConnectors.length; j++) // searching for matching output connector
			{
				if(componentList.contains(targetConnectors[j].getParrentComponent())) return;
				
				if(targetConnectors[j] == target) // found
				{
					Connector temp = targetConnectors[j];
					targetConnectors[j].removeTarget(this); // detach
					targetsToRemove.add(temp);
					break;
				}
			}
		}
		
		if(targetsToRemove.size() > 0)
		{
			targets.removeAll(targetsToRemove);
			targetsToRemove.clear();
		}
	}
	
	public void addTarget(Connector target)
	{
		targets.add(target);
	}
	
	public void removeTarget(Connector target)
	{
		targets.remove(target);
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public DComponent getParrentComponent()
	{
		return parrentComponent;
	}
	
	public boolean isInput()
	{
		return isInput;
	}
	
	public boolean contains(float x, float y) // is given point inside component
	{
		return this.x <= x && this.x + 12 >= x && this.y <= y && this.y + 12 >= y;
	}
}
