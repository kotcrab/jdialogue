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

import pl.kotcrab.dialoguelib.editor.Assets;
import pl.kotcrab.dialoguelib.editor.KotcrabText;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public abstract class DComponent
{
	private KotcrabText title;
	
	private int x, y, ry, height, width;
	// private int inputs, outputs;
	
	private Connection[] inputs;
	private Connection[] outputs;
	
	public DComponent(String title, int x, int y, int inputs, int outputs)
	{
		this.x = x;
		this.y = y;
		// this.inputs = inputs;
		// this.outputs = outputs;
		
		this.title = new KotcrabText(Assets.consolasFont, title, false, 0, 0);
		this.title.setScale(0.7f);
		// this.title.cener
		
		this.width = (int) (this.title.getTextBounds().width + 30);
		if(inputs > outputs)
			height = (inputs + 1) * 20 + 20;
		else
			height = (outputs + 1) * 20 + 20;
		
		this.inputs = new Connection[inputs];
		this.outputs = new Connection[outputs];
		
		for (int i = 0; i < this.inputs.length; i++)
			this.inputs[i] = new Connection(0, 0);
		
		for (int i = 0; i < this.outputs.length; i++)
			this.outputs[i] = new Connection(0, 0);
		
		ry = y - height / 2;
		
		distributeConnections();
		calcTextPos();
	}
	
	private void distributeConnections()
	{
		float avY = height - 30; // avaiable space in y coordinate
		
		//12 is connection height
		
		float avYIn = (avY - (12 * (inputs.length))) / (inputs.length); // i have no idea what i'm doing, no seriously why is this working?
		for (int i = 0; i < inputs.length; i++)
			inputs[i].setPosition(x, ry + avYIn / 2 + ((avYIn + 12) * i));
		
		float avYOut = (avY - (12 * (outputs.length))) / (outputs.length); // i have no idea what i'm doing, no seriously why is this working?
		for (int i = 0; i < outputs.length; i++)
			outputs[i].setPosition(x + width - 12, ry + avYOut / 2 + ((avYOut + 12) * i));
		
//		if(inputs.length > outputs.length)
//		{
//			for (int i = 0; i < inputs.length; i++)
//				inputs[i].setPosition(x, ry + 10 + (20 * i));
//			
//			float avYOut = (avY - (12 * (outputs.length))) / (outputs.length); // i have no idea what i'm doing, no seriously why is this working?
//			
//			for (int i = 0; i < outputs.length; i++)
//				outputs[i].setPosition(x + width - 12, ry + avYOut / 2 + ((avYOut + 12) * i));
//		}
//		else
//		{
//			for (int i = 0; i < outputs.length; i++)
//				outputs[i].setPosition(x + width - 12, ry + 10 + (20 * i));
//			
//			avY = (avY - (12 * (inputs.length))) / (inputs.length); // i have no idea what i'm doing, no seriously why is this working?
//			for (int i = 0; i < inputs.length; i++)
//				inputs[i].setPosition(x, ry + avY / 2 + ((avY + 12) * i));
//		}
		
	}
	
	public void render(SpriteBatch batch)
	{
		title.draw(batch);
	}
	
	public void renderShapes(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Filled); // background
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(x, ry, width, height);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(x, ry, width, height); // outline
		shapeRenderer.line(x, ry + height - 30, x + width, ry + height - 30); // line under text
		shapeRenderer.end();
		
		for (int i = 0; i < inputs.length; i++)
			inputs[i].render(shapeRenderer);
		
		for (int i = 0; i < outputs.length; i++)
			outputs[i].render(shapeRenderer);
		
	}
	
	public void renderSelectionOutline(ShapeRenderer shapeRenderer)
	{
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.rect(x, y - height / 2, width, height);// outline
		shapeRenderer.line(x, ry + height - 30, x + width, ry + height - 30); // line under text
		shapeRenderer.end();
	}
	
	public boolean contains(float x, float y) //is given point inside component
	{
		return this.x <= x && this.x + this.width >= x && this.y - this.height / 2 <= y && this.y + this.height / 2 >= y;
	}
	
	public Connection connectionContains(float x, float y)
	{
		for (int i = 0; i < inputs.length; i++)
		{
			if(inputs[i].contains(x, y))
				return inputs[i];
		}
		
		for (int i = 0; i < outputs.length; i++)
		{
			if(outputs[i].contains(x, y))
				return outputs[i];
		}
		
		return null;
	}
	
	private void calcTextPos()
	{
		this.title.center(width);
		this.title.setPosition(x + this.title.getPosition().x + 3, ry + height - 30);
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
		ry = y - height / 2;
		
		calcTextPos();
		distributeConnections();
		
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}


}