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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class ConnectionRenderer
{
	private boolean renderCurves = true;
	
	private OrthographicCamera camera;
	
	Rectangle cameraRect;
	
	public ConnectionRenderer(OrthographicCamera camera)
	{
		this.camera = camera;
	}
	
	public void cameraCalc()
	{
		float cameraWidth = camera.viewportWidth * camera.zoom;
		float cameraHeight = camera.viewportHeight * camera.zoom;
		
		float cameraX = camera.position.x - cameraWidth / 2;
		float cameraY = camera.position.y - cameraHeight / 2;
		
		cameraRect = new Rectangle(cameraX, cameraY, cameraWidth, cameraHeight);
	}
	
	public void renderLines(ShapeRenderer shapeRenderer, DComponent comp)
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
			
			float startX;
			if(x2 > x1)
				startX = x1;
			else
				startX = x2;
			
			float startY;
			if(y2 > y1)
				startY = y1;
			else
				startY = y2;
			
			if(cameraRect.overlaps(new Rectangle(startX, startY, Math.abs(x2 - x1), Math.abs(y2 - y1))) == false) continue;
			
			float d = 0;
			
			if(renderCurves)
			{
				d = Math.abs(y1 - y2);
				if(d > 100) d = 100; // limit
			}
			
			if(renderCurves)
				shapeRenderer.curve(x1, y1, x1 + d, y1, x2 - d, y2, x2, y2, 32); // connection line
			else
				shapeRenderer.line(x1, y1, x2 - 12, y2);
		}
	}
	
	public void renderTraingles(ShapeRenderer shapeRenderer, DComponent comp)
	{
		Connector[] inputs = comp.getOutputs();
		
		for(int i = 0; i < inputs.length; i++)
		{
			Connector con = inputs[i];
			Connector target = con.getTarget();
			
			if(target == null) continue;
			
			if(cameraRect.overlaps(target.getParrentComponent().getBoundingRectangle()) == false && cameraRect.overlaps(con.getParrentComponent().getBoundingRectangle()) == false) continue;
			
			float y2 = target.getY() + 6;
			
			shapeRenderer.triangle(target.getX() - 8, target.getY(), target.getX() - 8, target.getY() + 12, target.getX() + 3, y2); // ending triangle
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