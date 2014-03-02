/*******************************************************************************
 * Copyright 2014 Pawel Pastuszak
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

package pl.kotcrab.jdialogue.tests;

import pl.kotcrab.jdialogue.loader.GdxLoader;
import pl.kotcrab.jdialogue.parser.impl.JDOMDialogueParser;
import pl.kotcrab.jdialogue.renderer.SimplestLibgdxRenderer;
import pl.kotcrab.libgdx.Assets;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GzipSimplestLibgdxRendererTest implements ApplicationListener
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private SimplestLibgdxRenderer renderer;
	
	@Override
	public void create()
	{
		Assets.load();
		
		camera = new OrthographicCamera(800, 480);
		camera.position.x = 800 / 2;
		camera.position.y = 480 / 2;
		
		batch = new SpriteBatch();
		
		renderer = new SimplestLibgdxRenderer(new JDOMDialogueParser(new GdxLoader(Gdx.files.internal("assets/testProjGzip/project.xml")), 30), Assets.consolasFont);
		renderer.startSequence("test");
		Gdx.input.setInputProcessor(renderer);
	}
	
	private void update()
	{
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		renderer.update();
	}
	
	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		update();
		
		batch.setShader(Assets.fontDistanceFieldShader);
		
		renderer.render(batch);
		
		batch.setShader(null);
	}
	
	@Override
	public void dispose()
	{
		Assets.dispose();
		
		batch.dispose();
	}
	
	@Override
	public void pause()
	{
		
	}
	
	@Override
	public void resume()
	{
		
	}
	
	@Override
	public void resize(int width, int height)
	{
		
	}
}
