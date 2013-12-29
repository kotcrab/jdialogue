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

import javax.swing.JOptionPane;

import pl.kotcrab.dialoguelib.editor.components.CallbackComponent;
import pl.kotcrab.dialoguelib.editor.components.ChoiceComponent;
import pl.kotcrab.dialoguelib.editor.components.ConnectionRenderer;
import pl.kotcrab.dialoguelib.editor.components.Connector;
import pl.kotcrab.dialoguelib.editor.components.DComponent;
import pl.kotcrab.dialoguelib.editor.components.DComponentType;
import pl.kotcrab.dialoguelib.editor.components.EndComponent;
import pl.kotcrab.dialoguelib.editor.components.RandomComponent;
import pl.kotcrab.dialoguelib.editor.components.RelayComponent;
import pl.kotcrab.dialoguelib.editor.components.StartComponent;
import pl.kotcrab.dialoguelib.editor.components.TextComponent;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

//TODO undo, redo
public class Renderer implements ApplicationListener, InputProcessor, GestureListener
{
	private EditorListener listener;
	
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private ArrayList<DComponent> componentList = new ArrayList<DComponent>();
	
	private Connector selectedConnection = null;
	private ConnectionRenderer connectionRenderer;
	
	private DComponent selectedComponent = null;
	private ArrayList<DComponent> selectedComponentsList = new ArrayList<DComponent>();
	private int attachPointX;
	private int attachPointY;
	
	private RectangularSelection rectangularSelection;
	
	private boolean disposed = false;
	
	private boolean renderDebug = false;
	private Matrix4 renderDebugMatrix;
	
	public Renderer(EditorListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void create()
	{
		Assets.load();
		
		camera = new OrthographicCamera(1280, 720);
		camera.position.x = 1280 / 2;
		camera.position.y = 720 / 2;
		Touch.setCamera(camera);
		
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		rectangularSelection = new RectangularSelection(new RectangularSelectionListener()
		{
			@Override
			public void finishedDrawing(ArrayList<DComponent> matchingComponents)
			{
				selectedComponentsList = matchingComponents;
			}
		}, componentList);
		
		InputMultiplexer mul = new InputMultiplexer();
		mul.addProcessor(this);
		mul.addProcessor(new GestureDetector(this));
		mul.addProcessor(rectangularSelection);
		Gdx.input.setInputProcessor(mul);
		
		componentList.add(new TextComponent(200, 300));
		
		componentList.add(new StartComponent(0, 300));
		componentList.add(new EndComponent(500, 300));
		
		connectionRenderer = new ConnectionRenderer();
		
		renderDebugMatrix = new Matrix4();
		renderDebugMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		renderDebugMatrix.scl(0.7f);
		
	}
	
	public void resetCamera()
	{
		// camera.zoom = 1;
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.x = Gdx.graphics.getWidth() / 2;
		camera.position.y = Gdx.graphics.getHeight() / 2;
		camera.zoom = 1;
	}
	
	public void update()
	{
		camera.update();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
		
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && selectedConnection != null && Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			selectedConnection.detach();
		}
	}
	
	@Override
	public void render()
	{
		if(!disposed)
		{
			update();
			
			Gdx.gl.glClearColor(0.69f, 0.69f, 0.69f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			batch.setShader(Assets.fontDistanceFieldShader);
			for(DComponent comp : componentList)
			{
				comp.renderShapes(shapeRenderer);
				
				batch.begin();
				comp.render(batch);
				batch.end();
			}
			batch.setShader(null);
			
			if(selectedComponent != null) selectedComponent.renderSelectionOutline(shapeRenderer, Color.ORANGE);
			if(selectedConnection != null)
			{
				selectedConnection.renderAsSelected(shapeRenderer);
				
				if(Gdx.input.isButtonPressed(Buttons.LEFT))
				{
					connectionRenderer.render(shapeRenderer, selectedConnection, Touch.getX(), Touch.getY());
				}
				
			}
			
			rectangularSelection.render(shapeRenderer);
			for(DComponent comp : selectedComponentsList)
			{
				comp.renderSelectionOutline(shapeRenderer, Color.RED);
			}
			
			for(DComponent comp : componentList)
			{
				connectionRenderer.render(shapeRenderer, comp);
			}
			
			if(renderDebug)
			{
				batch.setProjectionMatrix(renderDebugMatrix);
				batch.setShader(Assets.fontDistanceFieldShader);
				batch.begin();
				Assets.consolasFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() + 260);
				Assets.consolasFont.draw(batch, "Components: " + componentList.size(), 10, Gdx.graphics.getHeight() + 230);
				batch.end();
				batch.setShader(null);
			}
			
		}
	}
	
	@Override
	public void dispose()
	{
		if(!disposed)
		{
			Assets.dispose();
			shapeRenderer.dispose();
			batch.dispose();
			disposed = true;
		}
	}
	
	@Override
	public void resize(int width, int height)
	{
		Vector3 pos = camera.position.cpy();
		camera.setToOrtho(false, width, height);
		camera.position.x = pos.x;
		camera.position.y = pos.y;
		
		renderDebugMatrix.setToOrtho2D(0, 0, width, height);
		renderDebugMatrix.scl(0.7f);
	}
	
	public void addComponent(DComponentType componentType)
	{
		switch (componentType)
		{
		case CHOICE:
			componentList.add(new ChoiceComponent(Touch.getX(), Touch.getY()));
			break;
		case RANDOM:
			componentList.add(new RandomComponent(Touch.getX(), Touch.getY()));
			break;
		case TEXT:
			componentList.add(new TextComponent(Touch.getX(), Touch.getY()));
			break;
		case CALLBACK:
			componentList.add(new CallbackComponent(Touch.getX(), Touch.getY()));
			break;
		case END:
			componentList.add(new EndComponent(Touch.getX(), Touch.getY()));
			break;
		case RELAY:
			componentList.add(new RelayComponent(Touch.getX(), Touch.getY()));
			break;
		default:
			break;
		}
	}
	
	/**
	 * Find selected connection
	 * 
	 * @param x
	 *            in screen cords
	 * @param y
	 *            in sreen cords
	 */
	private void findConnection(float x, float y, boolean touchUp)
	{
		x = Touch.calcX(x);
		y = Touch.calcY(y);
		
		boolean found = false;
		for(DComponent comp : componentList)
		{
			Connector connection = comp.connectionContains(x, y);
			if(connection != null)
			{
				if(selectedConnection != null && selectedConnection != connection && selectedConnection.getParrentComponent() != connection.getParrentComponent() && touchUp)
				{
					if(selectedConnection.isInput() != connection.isInput()) // to prevent connecting 2 outputs or 2 inputs
					{
						connection.addTarget(selectedConnection);
						selectedConnection.addTarget(connection);
					}
				}
				
				selectedConnection = connection;
				found = true;
				break;
			}
		}
		
		if(found == false) selectedConnection = null;
	}
	
	//@formatter:off
	@Override public void resume(){}
	@Override public void pause(){}
	//@formatter:on
	
	public void setRenderCurves(boolean renderCurves)
	{
		connectionRenderer.setRenderCurves(renderCurves);
	}
	
	public void setRenderDebug(boolean renderDebug)
	{
		this.renderDebug = renderDebug;
	}
	
	// ==================================================================INPUT============================================================================
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(button == Buttons.RIGHT)
		{
			listener.mouseRightClicked(screenX, screenY); // ! we are sending raw coordinates
			return true;
		}
		return false;
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		float newZoom = 0;
		if(amount == 1) // out
		{
			if(camera.zoom >= 4) return false;
			
			newZoom = camera.zoom + 0.1f * camera.zoom * 2;
			
			camera.position.x = Touch.getX() + (camera.zoom / newZoom) * (camera.position.x - Touch.getX());
			camera.position.y = Touch.getY() + (camera.zoom / newZoom) * (camera.position.y - Touch.getY());
			
			camera.zoom += 0.1f * camera.zoom * 2;
		}
		
		if(amount == -1) // in
		{
			if(camera.zoom <= 0.5f) return false;
			
			newZoom = camera.zoom - 0.1f * camera.zoom * 2;
			
			camera.position.x = Touch.getX() + (newZoom / camera.zoom) * (camera.position.x - Touch.getX());
			camera.position.y = Touch.getY() + (newZoom / camera.zoom) * (camera.position.y - Touch.getY());
			
			camera.zoom -= 0.1f * camera.zoom * 2;
		}
		return true;
	}
	
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		if(selectedComponent == null && selectedConnection == null && selectedComponentsList.size() == 0 && Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			camera.position.x = camera.position.x + -deltaX * camera.zoom;
			camera.position.y = camera.position.y + deltaY * camera.zoom;
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		findConnection(screenX, screenY, true);
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if(selectedComponentsList.size() > 0)
		{
			for(DComponent comp : selectedComponentsList) // FIXME
			{
				int diffX = comp.getX() - selectedComponent.getX();
				int diffY = comp.getY() - selectedComponent.getY();
				
				comp.setX(Touch.calcX(screenX) - attachPointX + diffX);
				comp.setY(Touch.calcY(screenY) - attachPointY + diffY);
				
			}
			
			return true;
		}
		
		if(selectedComponent != null && selectedConnection == null && Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			selectedComponent.setX(Touch.calcX(screenX) - attachPointX);
			selectedComponent.setY(Touch.calcY(screenY) - attachPointY);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button)
	{
		x = Touch.calcX(x);
		y = Touch.calcY(y);
		
		boolean found = false;
		for(DComponent comp : componentList)
		{
			if(comp.contains(x, y))
			{
				if(selectedComponentsList.contains(comp))
				{
					if(selectedComponent != null) return false;
				}
				else
					selectedComponentsList.clear();
				
				selectedComponent = comp;
				attachPointX = (int) (x - selectedComponent.getX());
				attachPointY = (int) (y - selectedComponent.getY());
				found = true;
				listener.changePropertyTableModel(selectedComponent.getTableModel());
				
				break;
			}
		}
		
		if(found == false)
		{
			selectedComponent = null;
			listener.changePropertyTableModel(null);
			selectedComponentsList.clear();
		}
		
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode == Keys.FORWARD_DEL && selectedComponent != null) // because forward_del is delete key, del is backspace!
		{
			if(selectedComponent instanceof StartComponent)
			{
				listener.showMsg("This component cannot be deleted!", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			selectedComponent.detachAll();
			
			if(componentList.remove(selectedComponent))
			{
				selectedComponent = null;
				selectedComponentsList.clear();
			}
			else
				throw new EditorException("Component not on list! Ilegal state!");
			
			return true;
		}
		
		if(keycode == Keys.BACKSPACE && selectedComponent != null)
		{
			selectedComponent.detachAll();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		findConnection(screenX, screenY, false);
		
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
