/*******************************************************************************
 * Copyright 2013 Pawel Pastuszak
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
