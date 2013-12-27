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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Connector
{
	private float x, y;
	
	private Connector target; // null if input!
	private boolean isInput;
	
	private DComponent parrentComponent;
	
	public Connector(float x, float y, DComponent parrentComponent, boolean isInput)
	{
		this.x = x;
		this.y = y;
		this.isInput = isInput;
		this.parrentComponent = parrentComponent;
	}
	
	public void render(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(x, y, 12, 12);
		shapeRenderer.end();
	}
	
	public void renderAsSelected(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.rect(x, y, 12, 12);
		shapeRenderer.end();
	}
	
	public Connector getTarget()
	{
		return target;
	}
	
	public void detach()
	{
		if(isInput) //if its input we must detach from output component
		{
			Connector[] parrentOut = null;
			try
			{
				parrentOut = target.getParrentComponent().getOutputs();
			}
			catch (NullPointerException e) // target was not set, ignore
			{
				return;
			}
			
			for (int j = 0; j < parrentOut.length; j++) //searching for matching output connector 
			{
				if(parrentOut[j] == target) //found
				{
					parrentOut[j].setTarget(null); //detach
					break;
				}
			}
		}
		
		setTarget(null);
		
	}
	
	public void setTarget(Connector target)
	{
		this.target = target;
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
