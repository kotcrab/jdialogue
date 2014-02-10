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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class ConnectionRenderer
{
	private boolean renderCurves = true;
	
	Rectangle cameraRect;
	
	private int rendered;
	
	public int renderLines(ShapeRenderer shapeRenderer, DComponent comp)
	{
		rendered = 0;
		
		Connector[] outputs = comp.getOutputs();
		
		for(int i = 0; i < outputs.length; i++)
		{
			Connector con = outputs[i];
			Connector target = con.getTarget();
			
			if(target == null) continue;
			
			float x1 = con.getX() + 6;
			float y1 = con.getY() + 6;
			float x2 = target.getX() + 6;
			float y2 = target.getY() + 6;
			
			float startX;
			if(x2 > x1)
			{
				startX = x1;
				shapeRenderer.setColor(Color.BLACK);
			}
			else
			{
				shapeRenderer.setColor(Color.BLUE);
				startX = x2;
			}
			
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
			
			rendered++;
		}
		
		return rendered;
	}
	
	public void renderTraingles(ShapeRenderer shapeRenderer, DComponent comp)
	{
		Connector[] inputs = comp.getOutputs();
		
		for(int i = 0; i < inputs.length; i++)
		{
			Connector con = inputs[i];
			Connector target = con.getTarget();
			
			if(target == null) continue;
			
			if(target.getParrentComponent().isVisible() == false && con.getParrentComponent().isVisible() == false) continue;
			
			float y2 = target.getY() + 6;
			
			shapeRenderer.setColor(Color.BLACK);
			
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
	
	public void setCameraCalc(Rectangle cameraRect)
	{
		this.cameraRect = cameraRect;
	}
	
	public void setRenderCurves(boolean renderCurves)
	{
		this.renderCurves = renderCurves;
	}
	
}