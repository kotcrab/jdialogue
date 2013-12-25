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

package pl.kotcrab.dialoguelib.editor;

import java.awt.Rectangle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public abstract class DComponent
{
	private KotcrabText title;
	
	private int x, y, inputs, outputs, height, width;
	
	public DComponent(String title, int x, int y, int inputs, int outputs)
	{
		this.x = x;
		this.y = y;
		this.inputs = inputs;
		this.outputs = outputs;
		
		this.title = new KotcrabText(Assets.consolasFont, title, false, 0, y);
		this.title.setScale(0.7f);
		//this.title.cener
		
		this.width = (int) (this.title.getTextBounds().width + 30);
		if(inputs > outputs)
			height = (inputs + 1) * 30;
		else
			height = (outputs + 1) * 30;
		
		calcTextPos();
			
	}
	
	public void render(SpriteBatch batch)
	{
		//if()
		title.draw(batch);
	}
	
	public void setX(int x)
	{
		this.x = x;
		calcTextPos();

	}

	public void setY(int y)
	{
		this.y = y;
		calcTextPos();

	}
	
	private void calcTextPos()
	{
		this.title.center(width);
		this.title.setPosition(x + this.title.getPosition().x + 3, y);
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

	public void renderShapes(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Filled); //background
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(x, y - height / 2, width, height);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line); //outline
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(x, y - height / 2, width, height);
		shapeRenderer.end();
	}
	
	public void renderSelectionOutline(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Line); //outline
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.rect(x, y - height / 2, width, height);
		shapeRenderer.end();
	}
	
	public boolean contains(float x, float y)
	{
		return this.x <= x && this.x + this.width >= x && this.y - this.height / 2 <= y && this.y + this.height / 2 >= y;
	}
}