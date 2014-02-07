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

package pl.kotcrab.jdialogue.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

/**
 * Text that you can scale, rotate, change color itp. Supports distance field fonts
 * 
 * @author Pawel Pastuszak
 */
public class KotcrabText
{
	private Vector2 position; // pozycja
	private Vector2 origin; // origin
	private Vector2 scale; // skala
	
	private float rotation; // rotacja
	
	private Color color; // kolor
	
	private BitmapFontCache bitmapFontCache;
   	
	private TextBounds textBounds;
	
	private boolean autoSetOriginToMiddle; // automatyczne ustawianie originiu
	
	private Matrix4 oldMatrix; // stary matrix do przeksztalcen
	private Matrix4 newMatrix; // nowy matrix do przeksztalcen
	
	// konstruktor
	public KotcrabText(BitmapFont bitmapFont, String text, boolean alwaysSetOriginToMiddle, float x, float y)
	{
		bitmapFontCache = new BitmapFontCache(bitmapFont); // przygotowanie tekstu
		textBounds = bitmapFontCache.setText(text, 0, 0);
		
		position = new Vector2(x, y); // ustawienie pozycji
		scale = new Vector2(1, 1); // ustaiwnie skali
		rotation = 0; // ustawienie rotacji
		
		autoSetOriginToMiddle = alwaysSetOriginToMiddle; // config automatycznego origina
		
		if(autoSetOriginToMiddle == true)
		{
			calculateOrigin(); // obliczenie wlasciwego origina
		}
		else
			origin = new Vector2(0, 0); // ustawienie orgina na 0
			
		color = new Color(); // kolor
		newMatrix = new Matrix4(); // matrix
		
		translate();
	}
	
	public void draw(SpriteBatch spriteBatch) // render tekstu
	{
		oldMatrix = spriteBatch.getTransformMatrix().cpy(); // kopia matrixa
		
		spriteBatch.setTransformMatrix(newMatrix); // ustawienie nowego matrixa do tekstu
		
		bitmapFontCache.draw(spriteBatch); // redner tekstu
		
		spriteBatch.setTransformMatrix(oldMatrix); // przywrocenie matrixa
	}
	
	private void translate()
	{
		newMatrix.idt(); // przeksztalcenie tekstu
		newMatrix.translate(position.x + origin.x, position.y + origin.y + bitmapFontCache.getBounds().height, 0);
		newMatrix.rotate(0, 0, 1, rotation);
		newMatrix.scale(scale.x, scale.y, 1);
		newMatrix.translate(-origin.x, -origin.y, 0);
	}
	
	public void setText(String text) // zmiana tekstu
	{
		textBounds = bitmapFontCache.setText(text, 0, 0); // tak to musi byc 0 poniewaz pozycja jest ustalona podczas przeksztalanie matrixa (zobacz translate())
		
		if(autoSetOriginToMiddle == true) // wymagane poznowne obliczenie origina
		calculateOrigin();
		
		translate();
	}
	
	protected void calculateOrigin() // obliczanie origina
	{
		origin = new Vector2(textBounds.width / 2, -textBounds.height / 2);
	}
	
	// Getter and setter
	public Vector2 getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector2 newPosition)
	{
		position = newPosition;
		
		translate();
	}
	
	public void setPosition(float x, float y)
	{
		position.set(x, y);
		
		translate();
	}
		
	public void setX(float x)
	{
		position.x = x;
		
		translate();
	}
	
	public void setY(float y)
	{
		position.y = y;
		
		translate();
	}
	
	public Vector2 getOrigin()
	{
		return origin;
	}
	
	public void setOrigin(Vector2 newOrigin)
	{
		origin = newOrigin;
		
		translate();
	}
	
	public void setOrigin(float x, float y)
	{
		origin.set(x, y);
		
		translate();
	}
	
	public Vector2 getScale()
	{
		return scale;
	}
	
	public void setScale(Vector2 newScale)
	{
		scale = newScale;
		
		translate();
	}
	
	public void setScale(float x, float y)
	{
		scale.set(x, y);
		
		translate();
	}
	
	public void setScale(float XY)
	{
		scale.set(XY, XY);
		
		translate();
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public void setRotation(float rotation)
	{
		this.rotation = rotation;
		
		translate();
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		color.r = r;
		color.g = g;
		color.b = b;
		color.a = a;
		bitmapFontCache.setColor(color.r, color.g, color.b, color.a); // ustawienie koloru
	}
	
	public TextBounds getTextBounds()
	{
		return textBounds;
	}
	
	public BitmapFontCache getBitmapFontCache()
	{
		return bitmapFontCache;
	}
	
	public void center(int scrWidth)
	{
		position.x = (scrWidth - textBounds.width * scale.x) / 2;
		translate();
	}
}