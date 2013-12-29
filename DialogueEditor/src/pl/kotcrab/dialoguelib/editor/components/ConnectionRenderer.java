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

public class ConnectionRenderer
{
	private boolean renderCurves = true;
	
	public void render(ShapeRenderer shapeRenderer, DComponent comp)
	{
		Connector[] inputs = comp.getOutputs();
		
		for(int i = 0; i < inputs.length; i++)
		{
			Connector con = inputs[i];
			Connector target = con.getTarget();
			
			if(target == null) continue;
			
			float x1 = con.getX() + 6;
			float y1 = con.getY() + 6;
			float x2 = target.getX() + 6;
			float y2 = target.getY() + 6;
			
			float d = 0;
			
			if(renderCurves)
			{
				d = Math.abs(y1 - y2);
				if(d > 100) d = 100; // limit
			}
			
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.begin(ShapeType.Line);
			
			if(renderCurves)
				shapeRenderer.curve(x1, y1, x1 + d, y1, x2 - d, y2, x2, y2, 32); // connection line
			else
				shapeRenderer.line(x1, y1, x2 - 12, y2);
			
			shapeRenderer.end();
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.triangle(target.getX() - 8, target.getY(), target.getX() - 8, target.getY() + 12, target.getX() + 3, y2); // ending triangle
			shapeRenderer.end();
		}
	}
	
	public void render(ShapeRenderer shapeRenderer, Connector selectedConnection, float x2, float y2)
	{
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.ORANGE);
		float x1 = selectedConnection.getX() + 6;
		float y1 = selectedConnection.getY() + 6;
		
		float d = 0;
		
		if(renderCurves)
		{
			d = Math.abs(y1 - y2);
			if(d > 100) d = 100; // limit
		}
		
		if(selectedConnection.isInput() == true) // swaping values because curve will look weird without this
		{
			float temp = x1;
			x1 = x2;
			x2 = temp;
			
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		
		if(renderCurves)
			shapeRenderer.curve(x1, y1, x1 + d, y1, x2 - d, y2, x2, y2, 32);
		else
			shapeRenderer.line(x1, y1, x2, y2);
		
		shapeRenderer.end();
	}
	
	public void setRenderCurves(boolean renderCurves)
	{
		this.renderCurves = renderCurves;
	}
	
}