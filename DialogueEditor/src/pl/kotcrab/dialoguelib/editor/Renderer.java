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

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Renderer implements ApplicationListener, InputProcessor, GestureListener
{
	private EditorListener listener;
	
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private ArrayList<DComponent> componentList = new ArrayList<DComponent>();
	
	private DComponent selectedComponent = null;
	private int attachPointX;

	public Renderer(EditorListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void create()
	{
		Assets.load();
		camera = new OrthographicCamera(1280, 720);
		Touch.setCamera(camera);
		
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		InputMultiplexer mul = new InputMultiplexer();
		mul.addProcessor(this);
		mul.addProcessor(new GestureDetector(this));
		Gdx.input.setInputProcessor(mul);
		
		componentList.add(new TextComponent(200, 200));
	}
	
	public void update()
	{
		camera.update();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
	}
	
	@Override
	public void render()
	{
		update();
		
		Gdx.gl.glClearColor(0.69f, 0.69f, 0.69f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setShader(Assets.fontDistanceFieldShader);
		for (DComponent comp : componentList)
		{
			comp.renderShapes(shapeRenderer);
			
			batch.begin();
			comp.render(batch);
			batch.end();
		}
		batch.setShader(null);

		if(selectedComponent != null) selectedComponent.renderSelectionOutline(shapeRenderer);
		
//		batch.setShader(Assets.fontDistanceFieldShader);
//		batch.begin();
//		for (DComponent comp : componentList)
//		{
//			comp.render(batch);
//		}
//		batch.end();
//		batch.setShader(null);
		//
		// shapeRenderer.begin(ShapeType.Filled);
		// shapeRenderer.setColor(Color.GRAY);
		// shapeRenderer.rect(0, 0, 128, 128);
		// shapeRenderer.end();
		// shapeRenderer.setColor(Color.BLACK);
		// shapeRenderer.begin(ShapeType.Line);
		// int x1 = 400;
		// int y1 = 400;
		// int x2 = Touch.getX();
		// int y2 = Touch.getY();
		// int d = 200;
		// shapeRenderer.curve(x1, y1, x1 + d, y1,// x1+dx, x1,
		// x2 - d, y2,// x2+dy, y2,
		// x2, y2, 32);
		// // shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
		// shapeRenderer.end();
	}
	
	@Override
	public void dispose()
	{
		Assets.dispose();
	}
	
	@Override
	public void resize(int width, int height)
	{
		Vector3 pos = camera.position.cpy();
		camera.setToOrtho(false, width, height);
		camera.position.x = pos.x;
		camera.position.y = pos.y;
	}
	
	public void addComponent(DComponentType componentType)
	{
		switch (componentType)
		{
		case CHOICE:
			break;
		case RANDOM:
			break;
		case TEXT:
			componentList.add(new TextComponent(Touch.getX(), Touch.getY()));
			break;
		default:
			break;
		
		}
	}
	
	//@formatter:off
	@Override public void resume(){}
	@Override public void pause(){}
	//@formatter:on
	
	// ==================================================================INPUT============================================================================
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(button == Buttons.RIGHT) listener.mouseRightClicked(screenX, screenY); // ! we are sending raw coordinates
		return false;
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		if(camera.zoom - 0.1f < 1 && camera.zoom + 0.1f > 10) return false;
		
		float newZoom = 0;
		if(amount == 1)
		{
			if(camera.zoom >= 4) return false;
			
			camera.zoom += 0.1f * camera.zoom;
			newZoom = camera.zoom + 0.1f * camera.zoom;
		}
		
		if(amount == -1)
		{
			if(camera.zoom <= 0.5f) return false;
			
			camera.zoom -= 0.1f * camera.zoom;
			newZoom = camera.zoom - 0.1f * camera.zoom;
		}
		
		float width = camera.position.x - Touch.getX();
		float height = camera.position.y + Touch.getY();
		
		camera.position.x -= width * (1 - newZoom / camera.zoom);
		camera.position.y -= height * (1 - newZoom / camera.zoom);
		
		return false;
	}
	
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		if(selectedComponent == null)
		{
		camera.position.x = camera.position.x + -deltaX * camera.zoom;
		camera.position.y = camera.position.y + deltaY * camera.zoom;
		}

		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if(selectedComponent != null)
		{
			selectedComponent.setX(Touch.calcX(screenX) - attachPointX);//  + (selectedComponent.getX() + selectedComponent.getWidth() - Touch.calcX(screenX)));
			selectedComponent.setY(Touch.calcY(screenY));
		}
		return false;
	}
		
	@Override
	public boolean touchDown(float x, float y, int pointer, int button)
	{
		x = Touch.calcX(x);
		y = Touch.calcY(y);
		
		
		boolean found = false;
		for (DComponent comp : componentList)
		{
			if(comp.contains(x, y))
			{
				selectedComponent = comp;
				attachPointX = (int) (x - selectedComponent.getX());
				found = true;
				break;
			}
		}
		
		if(found == false) selectedComponent = null;
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button)
	{
		return false;
	}
	
	@Override
	public boolean longPress(float x, float y)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean fling(float velocityX, float velocityY, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean panStop(float x, float y, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean zoom(float initialDistance, float distance)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}
	
}
