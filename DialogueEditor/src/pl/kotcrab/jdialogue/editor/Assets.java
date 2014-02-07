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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Assets
{
	public static ShaderProgram fontDistanceFieldShader;
	public static BitmapFont consolasFont;
	
	public static void load()
	{
		fontDistanceFieldShader = new ShaderProgram(Gdx.files.internal("assets/font/font.vert"), Gdx.files.internal("assets/font/font.frag"));
		if(!fontDistanceFieldShader.isCompiled())
		{
			Gdx.app.error("fontShader", "compilation failed:\n" + fontDistanceFieldShader.getLog());
		}
		
		Texture fontTexture = new Texture(Gdx.files.internal("assets/font/consolas.png"), true);
		fontTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		
		consolasFont = new BitmapFont(Gdx.files.internal("assets/font/consolas.fnt"), new TextureRegion(fontTexture), false);
		
		Gdx.app.log("Assets", "Finished loading assets");
	}
	
	public static void dispose()
	{
		consolasFont.dispose();
		fontDistanceFieldShader.dispose();
		Gdx.app.log("Assets", "Finished disposing assets");
	}
}