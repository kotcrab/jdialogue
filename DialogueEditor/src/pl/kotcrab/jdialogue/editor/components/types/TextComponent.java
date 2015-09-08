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

package pl.kotcrab.jdialogue.editor.components.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import pl.kotcrab.jdialogue.editor.Assets;
import pl.kotcrab.jdialogue.editor.Editor;
import pl.kotcrab.jdialogue.editor.KotcrabText;
import pl.kotcrab.jdialogue.editor.components.ComponentTableModel;
import pl.kotcrab.jdialogue.editor.components.DComponent;
import pl.kotcrab.jdialogue.editor.project.PCharacter;

public class TextComponent extends DComponent {
	private int additionalHeight = 100;
	private Label label;
	private Rectangle scissors = new Rectangle();
	private OrthographicCamera camera;

	public TextComponent (int x, int y) {
		super("Show Text", x, y, 1, 1);
		tableModel = new ComponentTableModel(
				//@formatter:off
		new Object[][]
				{
				    {"Text", "Set Text"},
				    {"Character", new PCharacter(0, "None (default character)", "none")}
				}
		//@formatter:on
		);

		camera = Editor.window.getLogic().renderer.getCamera();

		label = new Label("", new LabelStyle(Assets.consolasFont, Color.WHITE));
		label.setWrap(true);
		label.setWidth(390);
		label.setFontScale(0.5f);

		tableModel.addTableModelListener(e -> {
			if (e.getColumn() == 1) {
				updateTextPreview();
			}
		});
	}

	@Override
	public void setup () {
		super.setup();
		updateTextPreview();
	}

	private void updateTextPreview () {
		label.setText(tableModel.getData()[0][1].toString());
		additionalHeight = (int) label.getPrefHeight() - 10;
		additionalHeight = Math.min(additionalHeight, 300);
	}

	@Override
	public boolean contains (float xt, float yt) {
		int oldHeight = height;
		int oldY = y;
		height += additionalHeight;
		y -= additionalHeight / 2;

		boolean result = super.contains(xt, yt);

		height = oldHeight;
		y = oldY;

		return result;
	}

	@Override
	public boolean contains (Rectangle rectangle) {
		int oldHeight = height;
		int oldY = y;
		height += additionalHeight;
		y -= additionalHeight / 2;

		boolean result = super.contains(rectangle);

		height = oldHeight;
		y = oldY;

		return result;
	}

	@Override
	public KotcrabText[] provideInputLabels () {
		return new KotcrabText[]{new KotcrabText(Assets.consolasFont, "In", false, 0, 0)};
	}

	@Override
	public KotcrabText[] provideOutputsLabels () {
		return new KotcrabText[]{new KotcrabText(Assets.consolasFont, "Out", false, 0, 0)};
	}

	@Override
	protected void calcSize (int inputs, int outputs) {
		super.calcSize(inputs, outputs);
	}

	@Override
	public void render (SpriteBatch batch) {
		super.render(batch);
		ScissorStack.calculateScissors(camera, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), batch.getTransformMatrix(), getBounds(), scissors);
		ScissorStack.pushScissors(scissors);
		label.setPosition(x, y - label.getPrefHeight() / 2 - 30);
		label.draw(batch, 1);
		batch.flush();
		ScissorStack.popScissors();
	}

	@Override
	public void setY (int y) {
		super.setY(y);
	}

	@Override
	public void setX (int x) {
		super.setX(x);
	}

	@Override
	public void renderShapes (ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(x, ry - additionalHeight, width, height + additionalHeight);

		for (int i = 0; i < inputs.length; i++)
			inputs[i].render(shapeRenderer);

		for (int i = 0; i < outputs.length; i++)
			outputs[i].render(shapeRenderer);
	}

	@Override
	public void renderSelectionOutline (ShapeRenderer shapeRenderer, Color color) {
		shapeRenderer.setColor(color);
		shapeRenderer.rect(x, ry - additionalHeight, width, height + additionalHeight); // outline
		shapeRenderer.line(x, ry + height - 30, x + width, ry + height - 30); // line under text
		shapeRenderer.line(x, y - 30, x + width, y - 30); // line under connectors
	}

	@Override
	public void calcIfVisible (Rectangle cameraRect) {
		if (cameraRect.overlaps(getBounds()))
			visible = true;
		else
			visible = false;
	}

	private Rectangle getBounds () {
		return bounds.set(x, ry - additionalHeight, width, height + additionalHeight);
	}
}
