/*******************************************************************************
 * DialogueEditor
 * Copyright (C) 2013-2014 Pawel Pastuszak
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package pl.kotcrab.jdialogue.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import pl.kotcrab.jdialogue.editor.components.DComponent;

import java.util.ArrayList;

public class RectangularSelection extends InputAdapter {

	private Rectangle currentRect = null;
	private Rectangle rectToDraw = null;
	private Rectangle previousRectDrawn = new Rectangle();

	private int drawingPointer = -1;

	private RectangularSelectionListener listener;
	private ArrayList<DComponent> componentList;

	public RectangularSelection (RectangularSelectionListener listener, ArrayList<DComponent> componentList) {
		this.listener = listener;
		this.componentList = componentList;
	}

	public void render (ShapeRenderer shapeRenderer) {
		if (rectToDraw != null) {
			Gdx.graphics.getGL20().glEnable(GL10.GL_BLEND);

			shapeRenderer.setColor(Color.RED);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.rect(rectToDraw.getX(), rectToDraw.getY(), rectToDraw.getWidth(), rectToDraw.getHeight());
			shapeRenderer.end();

			shapeRenderer.setColor(0.7f, 0, 0, 0.3f);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rect(rectToDraw.getX(), rectToDraw.getY(), rectToDraw.getWidth(), rectToDraw.getHeight());
			shapeRenderer.end();

		}
	}

	public void setComponentList (ArrayList<DComponent> componentList) {
		this.componentList = componentList;
	}

	public void findContainedComponents () {
		// if(rectToDraw)
		ArrayList<DComponent> foundComponents = new ArrayList<DComponent>();
		for (DComponent comp : componentList) {
			if (rectToDraw.contains(new Rectangle(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight() / 2))) {
				foundComponents.add(comp);
			}
		}
		listener.finishedDrawing(foundComponents);
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			drawingPointer = pointer;
			int x = (int) Touch.calcX(screenX);
			int y = (int) Touch.calcY(screenY);
			currentRect = new Rectangle(x, y, 0, 0);
			updateDrawableRect();
		}

		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (drawingPointer == pointer && !Gdx.input.isButtonPressed(Buttons.LEFT)) {
			findContainedComponents();
			rectToDraw = null;
			drawingPointer = -1;
			return true;
		}

		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		if (drawingPointer == pointer && Gdx.input.isButtonPressed(Buttons.LEFT)) {
			int x = (int) Touch.calcX(screenX);
			int y = (int) Touch.calcY(screenY);
			currentRect.setSize(x - currentRect.x, y - currentRect.y);
			updateDrawableRect();
			return true;
		}

		return false;
	}

	private void updateDrawableRect () {
		float x = currentRect.x;
		float y = currentRect.y;
		float width = currentRect.width;
		float height = currentRect.height;

		// Make the width and height positive, if necessary.
		if (width < 0) {
			width = 0 - width;
			x = x - width + 1;
		}

		if (height < 0) {
			height = 0 - height;
			y = y - height + 1;
		}

		// Update rectToDraw after saving old value.
		if (rectToDraw != null) {
			previousRectDrawn.set(rectToDraw.x, rectToDraw.y, rectToDraw.width, rectToDraw.height);
			rectToDraw.set(x, y, width, height);
		} else {
			rectToDraw = new Rectangle(x, y, width, height);
		}
	}
}

interface RectangularSelectionListener {
	public void finishedDrawing (ArrayList<DComponent> matchingComponents);
}
