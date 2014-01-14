/*******************************************************************************
 * Copyright 2013 - 2014 Pawel Pastuszak
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

import pl.kotcrab.dialoguelib.editor.components.ConnectionRenderer;
import pl.kotcrab.dialoguelib.editor.components.Connector;
import pl.kotcrab.dialoguelib.editor.components.DComponent;
import pl.kotcrab.dialoguelib.editor.components.DComponentType;
import pl.kotcrab.dialoguelib.editor.components.IDManager;
import pl.kotcrab.dialoguelib.editor.components.types.CallbackComponent;
import pl.kotcrab.dialoguelib.editor.components.types.ChoiceComponent;
import pl.kotcrab.dialoguelib.editor.components.types.EndComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RandomComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RelayComponent;
import pl.kotcrab.dialoguelib.editor.components.types.StartComponent;
import pl.kotcrab.dialoguelib.editor.components.types.TextComponent;
import pl.kotcrab.dialoguelib.editor.project.Project;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.thoughtworks.xstream.XStream;

//TODO rozmiar komponentów przy wczytytwaniu
//TODO przesuwanie kamery, gdy przy rysowaniu zblizymy kursor do krawêdzie ekranu, u¿yteczna funkcja
public class Renderer implements ApplicationListener, InputProcessor, GestureListener
{
	private EditorListener listener;
	
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private ArrayList<DComponent> componentList = new ArrayList<DComponent>();
	
	private ArrayList<ArrayList<DComponent>> undoList = new ArrayList<ArrayList<DComponent>>();
	private ArrayList<ArrayList<DComponent>> redoList = new ArrayList<ArrayList<DComponent>>();
	
	private ArrayList<DComponent> clipboardList;
	
	private Connector selectedConnector = null;
	private ConnectionRenderer connectionRenderer;
	
	private DComponent selectedComponent = null;
	private ArrayList<DComponent> selectedComponentsList = new ArrayList<DComponent>();
	private int attachPointX;
	private int attachPointY;
	
	private RectangularSelection rectangularSelection;
	
	private boolean disposed = false;
	
	private IDManager idManager;
	
	private boolean renderDebug = false;
	private Matrix4 renderDebugMatrix = new Matrix4();
	private Matrix4 renderInfoTextMatrix = new Matrix4();
	
	private Preferences prefs;
	
	private KotcrabText infoText;
	
	//private Project project = null;
	
	private XStream xstream;
	
	private Rectangle cameraRect;
	
	public Renderer(EditorListener listener, XStream xstream)
	{
		this.listener = listener;
		this.xstream = xstream;
	}
	
	@Override
	public void create()
	{
		Assets.load();
		
		prefs = Gdx.app.getPreferences("pl.kotcrab.dialoguelib.editorprefs");
		App.setPrefs(prefs);
		App.loadPrefs();
		
		camera = new OrthographicCamera(1280, 720);
		camera.position.x = 1280 / 2;
		camera.position.y = 720 / 2;
		Touch.setCamera(camera);
		cameraRect = CameraUtils.calcCameraBoundingRectangle(camera);
		
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		idManager = new IDManager();
		
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
		
		connectionRenderer = new ConnectionRenderer();
		
		infoText = new KotcrabText(Assets.consolasFont, "Load or create new project to begin!", false, 0, 0);
		infoText.setScale(1.4f);
		
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
		cameraRect = CameraUtils.calcCameraBoundingRectangle(camera);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
		
		for(DComponent comp : componentList)
		{
			comp.calcVisible(cameraRect);
		}
		
		// this is here for simplicity, because we are using a key and a mouse button
		// keyDown, will only provide us with key pressed event, and we will have to create flags for keyPressed and ButtonPressed
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && selectedConnector != null && Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			selectedConnector.detach();
		}
	}
	
	@Override
	public void render()
	{
		if(!disposed)
		{
			update();
			
			int renderedComponents = 0;
			int renderedConnections = 0;
			
			Gdx.gl.glClearColor(0.69f, 0.69f, 0.69f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			// why more loops == better
			// we can draw more shapes/sprites in single batch, wich reduces lwgjl nSwapBuffers and imroves perofmence
			
			shapeRenderer.begin(ShapeType.Filled);
			for(DComponent comp : componentList)
			{
				if(comp.isVisible())
				{
					comp.renderShapes(shapeRenderer);
					renderedComponents++;
				}
			}
			shapeRenderer.end();
			
			shapeRenderer.begin(ShapeType.Line);
			for(DComponent comp : componentList)
			{
				if(comp.isVisible()) comp.renderOutline(shapeRenderer);
			}
			shapeRenderer.end();
			
			batch.setShader(Assets.fontDistanceFieldShader);
			batch.begin();
			for(DComponent comp : componentList)
			{
				comp.render(batch);
			}
			batch.end();
			batch.setShader(null);
			
			shapeRenderer.begin(ShapeType.Line);
			if(selectedComponent != null) selectedComponent.renderSelectionOutline(shapeRenderer, Color.ORANGE);
			shapeRenderer.end();
			
			// these if's are difrrent, easy to omit...
			
			if(selectedConnector != null)
			{
				if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
					selectedConnector.renderAsSelected(shapeRenderer, Color.RED);
				else
					selectedConnector.renderAsSelected(shapeRenderer, Color.ORANGE);
				
				if(Gdx.input.isButtonPressed(Buttons.LEFT))
				{
					connectionRenderer.render(shapeRenderer, selectedConnector, Touch.getX(), Touch.getY());
				}
			}
			
			rectangularSelection.render(shapeRenderer);
			
			shapeRenderer.begin(ShapeType.Line);
			for(DComponent comp : selectedComponentsList)
			{
				comp.renderSelectionOutline(shapeRenderer, Color.RED);
			}
			shapeRenderer.end();
			
			connectionRenderer.setCameraCalc(cameraRect);
			
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.begin(ShapeType.Line);
			for(DComponent comp : componentList)
			{
				renderedConnections += connectionRenderer.renderLines(shapeRenderer, comp); //we are counitng how many connections we had drawn
			}
			shapeRenderer.end();
			
			shapeRenderer.begin(ShapeType.Filled);
			for(DComponent comp : componentList)
			{
				connectionRenderer.renderTraingles(shapeRenderer, comp);
			}
			shapeRenderer.end();
			
			if(renderDebug)
			{
				batch.setProjectionMatrix(renderDebugMatrix);
				batch.setShader(Assets.fontDistanceFieldShader);
				batch.begin();
				Assets.consolasFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() * 10 / 7);
				Assets.consolasFont.draw(batch, "Components: " + componentList.size(), 10, Gdx.graphics.getHeight() * 10 / 7 - (23 * 1));
				Assets.consolasFont.draw(batch, "Rendered components: " + renderedComponents, 10, Gdx.graphics.getHeight() * 10 / 7 - (23 * 2));
				Assets.consolasFont.draw(batch, "Rendered connections: " + renderedConnections, 10, Gdx.graphics.getHeight() * 10 / 7 - (23 * 3));
				batch.end();
				batch.setShader(null);
			}
			
			if(componentList.size() == 0)
			{
				batch.setProjectionMatrix(renderInfoTextMatrix);
				batch.setShader(Assets.fontDistanceFieldShader);
				batch.begin();
				infoText.draw(batch);
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
		
		renderInfoTextMatrix.setToOrtho2D(0, 0, width, height);
		
		infoText.center(Gdx.graphics.getWidth());
		infoText.setY(Gdx.graphics.getHeight() / 2);
	}
	
	public void addComponent(DComponentType componentType)
	{
		if(componentList.size() > 0) //if list is empty, we don't have any sequnce loaded(sequnce must have start component)
		{
			switch (componentType)
			{
			case CHOICE:
				componentList.add(new ChoiceComponent(Touch.getX(), Touch.getY(), idManager.getFreeId()));
				break;
			case RANDOM:
				componentList.add(new RandomComponent(Touch.getX(), Touch.getY(), idManager.getFreeId()));
				break;
			case TEXT:
				componentList.add(new TextComponent(Touch.getX(), Touch.getY(), idManager.getFreeId()));
				break;
			case CALLBACK:
				componentList.add(new CallbackComponent(Touch.getX(), Touch.getY(), idManager.getFreeId()));
				break;
			case END:
				componentList.add(new EndComponent(Touch.getX(), Touch.getY(), idManager.getFreeId()));
				break;
			case RELAY:
				componentList.add(new RelayComponent(Touch.getX(), Touch.getY(), idManager.getFreeId()));
				break;
			default:
				break;
			}
		}
		else
			listener.showMsg("Create or load project to edit dialouge structure", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Find selected connection
	 * 
	 * @param x
	 *            in screen cords
	 * @param y
	 *            in screen cords
	 */
	private void findConnection(float x, float y, boolean touchUp)
	{
		x = Touch.calcX(x);
		y = Touch.calcY(y);
		
		boolean found = false;
		for(DComponent comp : componentList)
		{
			if(comp.isVisible() == false)
				continue;
			
			Connector connector = comp.connectionContains(x, y);
			if(connector != null) // in drawing mode
			{
				if(selectedConnector != null && selectedConnector != connector && selectedConnector.getParrentComponent() != connector.getParrentComponent() && touchUp)
				{
					if(selectedConnector.isInput() != connector.isInput()) // to prevent connecting 2 outputs or 2 inputs
					{
						// proper target found, adding
						connector.addTarget(selectedConnector);
						selectedConnector.addTarget(connector);
					}
				}
				
				selectedConnector = connector;
				found = true;
				break;
			}
		}
		if(found == false) selectedConnector = null;
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
	
	public ArrayList<DComponent> getComponentList()
	{
		return componentList;
	}
	
	/**
	 * Change componentList and perfom cleanup
	 * 
	 */
	public void setComponentList(ArrayList<DComponent> componentList)
	{
		this.componentList = componentList;
		rectangularSelection.setComponentList(componentList); //rectangularselection potrzebuje listy do znajdywania kompponentów
		selectedComponent = null;
		selectedConnector = null;
		
		undoList.clear();
		redoList.clear();
		
		for(DComponent comp : componentList) //TODO rezerwowaæ id, czy ustaiwaæ je od nowa, teoretycznie bez znaczenia, ale mo¿na sprawdziæ.
		{
			comp.setId(idManager.getFreeId());
		}
	}
	
//	public void setProject(Project project)
//	{
//		this.project = project;
//	}
	
	public void undo()
	{
		if(undoList.size() > 0)
		{
			ArrayList<DComponent> undoComponents = undoList.get(undoList.size() - 1);
			
			redoList.add(undoComponents);
			
			for(DComponent comp : undoComponents)
				comp.setId(idManager.getFreeId());
			
			componentList.addAll(undoComponents);
			
			undoList.remove(undoList.size() - 1);
		}
	}
	
	public void redo()
	{
		if(redoList.size() > 0)
		{
			ArrayList<DComponent> redoComponents = redoList.get(redoList.size() - 1);
			
			removeComponentList(redoComponents);
			
			redoList.remove(redoList.size() - 1);
		}
	}
	
	public void removeComponent(DComponent comp)
	{
		if(selectedComponent instanceof StartComponent)
		{
			listener.showMsg("This component cannot be deleted!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		idManager.freeID(comp.getId());
		listener.changePropertyTableModel(null);
		comp.detachAll();
		
		componentList.remove(comp);
		
		ArrayList<DComponent> tempList = new ArrayList<DComponent>();
		tempList.add(comp);
		
		undoList.add(tempList);
		
		selectedComponent = null;
	}
	
	public void removeComponentList(ArrayList<DComponent> compList)
	{
		for(DComponent comp : compList)
		{
			if(comp instanceof StartComponent)
			{
				listener.showMsg("Selection containts components that cannot be deleted!", "Error", JOptionPane.ERROR_MESSAGE);
				compList.clear();
				return;
			}
		}
		
		for(DComponent comp : compList)
		{
			idManager.freeID(comp.getId());
			comp.detachAllNotOnList(selectedComponentsList);
		}
		
		componentList.removeAll(compList);
		
		undoList.add(new ArrayList<DComponent>(compList));
		
		selectedComponent = null;
		selectedComponentsList.clear();
		listener.changePropertyTableModel(null);
		
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
	
	// pan is worse because you must drag mouse a little bit to fire this event
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		if(selectedComponentsList.size() > 0) // FIXME gdy przesuwam kompoennty w lewo, one lekko przesuwaja sie w strone srodka, dlaczego? nie zawsze tak sie dzieje...
		{
			for(DComponent comp : selectedComponentsList)
			{
				comp.setX((int) (comp.getX() + deltaX * camera.zoom));
				comp.setY((int) (comp.getY() - deltaY * camera.zoom));
			}
			return true;
		}
		
		if(selectedComponent == null && selectedConnector == null && selectedComponentsList.size() == 0 && Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			camera.position.x = camera.position.x - deltaX * camera.zoom;
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
			return false;
		}
		
		if(selectedComponent != null && selectedConnector == null && Gdx.input.isButtonPressed(Buttons.LEFT))
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
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode == Keys.FORWARD_DEL) // because forward_del is delete key, del is backspace!
		{
			if(selectedComponent != null && selectedComponentsList.size() == 0)
			{
				removeComponent(selectedComponent);
			}
			
			if(selectedComponentsList.size() > 0)
			{
				removeComponentList(selectedComponentsList);
			}
		}
		
		if(keycode == Keys.BACKSPACE && selectedComponent != null)
		{
			selectedComponent.detachAll();
			return true;
		}
		
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.A))
		{
			selectedComponentsList.clear();
			selectedComponentsList.addAll(componentList);
		}
		
		if(selectedComponentsList.size() > 0 && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.C))
		{
			clipboardList = new ArrayList<DComponent>(selectedComponentsList);
		}
		
		if(clipboardList != null && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.V))
		{
			// TODO better positing system
			int x = clipboardList.get(0).getX(); // we will need this to position component later
			int y = clipboardList.get(0).getY();
			
			// probably the best way to deep copy object is to serialize it and deserialize, we can use xstream for that
			String result = xstream.toXML(clipboardList);
			clipboardList = (ArrayList<DComponent>) xstream.fromXML(result);
			
			for(DComponent comp : clipboardList) // we need to asign new id's to components
			{
				if(comp.getClass() == StartComponent.class)
				{
					comp.detachAll();
					continue;
				}
				
				comp.detachAllNotOnList(clipboardList);
				comp.setId(idManager.getFreeId());
				comp.setX(Touch.getX() + (comp.getX() - x)); // move new component to cursor pos
				comp.setY(Touch.getY() + (comp.getY() - y));
			}
			
			componentList.addAll(clipboardList);
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
