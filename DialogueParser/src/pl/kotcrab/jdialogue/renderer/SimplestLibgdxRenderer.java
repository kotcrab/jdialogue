/*******************************************************************************
 * Copyright 2014 Pawel Pastuszak
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package pl.kotcrab.jdialogue.renderer;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kotcrab.jdialogue.parser.CallbackListener;
import pl.kotcrab.jdialogue.parser.ComponentType;
import pl.kotcrab.jdialogue.parser.DialogueParser;

public class SimplestLibgdxRenderer implements DialogueRenderer, InputProcessor {
	private DialogueParser parser;
	private BitmapFont font;

	private String lineTitle = "";
	private String[] lines = {"", "", ""};

	// abc1 }
	// abc2 } <
	// abc3 }
	// abc4
	// < selector { visible heigt

	private int selectorPointAt = 0;
	private int selectorVisiblePos = 0;
	private int visibleHeight = 0;
	private int visibleHeightPos = 0;
	private String selector = ">";
	private String[] choices = {""};

	private int lastId = -1;

	private ComponentType currentComponentType;

	public SimplestLibgdxRenderer (DialogueParser parser, BitmapFont font) {
		this.parser = parser;
		this.font = font;

		parser.addCallbackListener(new CallbackListener() {

			@Override
			public boolean handleCallbackCheck (String callbackText) {
				System.out.print("***Callback Check*** " + callbackText);

				return false;
			}

			@Override
			public void handleCallback (String callbackText) {
				System.out.println("***Callback*** " + callbackText);
			}
		});

	}

	public void update () {
	}

	public void render (SpriteBatch batch) {
		batch.begin();

		for (int i = 0; i < lines.length; i++) {
			font.draw(batch, lines[i], 100, 160 - (i * 30));
		}

		if (currentComponentType == ComponentType.CHOICE) {
			font.draw(batch, selector, 80, 160 - (selectorVisiblePos * 30));
			font.draw(batch, lineTitle, 100, 200);
			font.draw(batch, selectorPointAt + 1 + "/" + choices.length, 100, 40);
		}

		batch.end();
	}

	private void processComponent () {
		if (currentComponentType == ComponentType.END) return;
		currentComponentType = parser.processNextComponent();

		if (currentComponentType == ComponentType.TEXT) {
			clear();
			lines[0] = parser.getCharacterData().getName() + ": " + parser.getMsg();

			if (parser.isCurrentMsgFinished() == false) lines[1] = parser.getMsg();

			if (parser.isCurrentMsgFinished() == false) lines[2] = parser.getMsg();

			parser.moveToNextComponent();
		}

		if (currentComponentType == ComponentType.CHOICE) {
			if (lastId != parser.getId()) {
				clearChoice();
				lastId = parser.getId();
			}

			lineTitle = parser.getMsg();
			choices = parser.getChoiceData();

			for (int i = 0; i < choices.length; i++) {
				visibleHeight = i + 1;

				if (i == 3) {
					visibleHeight = 3;
					break;
				}

				lines[i] = choices[i];
			}

			listMove();
		}

		if (currentComponentType == ComponentType.CALLBACK || currentComponentType == ComponentType.CBCHECK || currentComponentType == ComponentType.RANDOM || currentComponentType == ComponentType.RELAY) {
			parser.moveToNextComponent();
			processComponent();
		}
	}

	private void clear () {
		for (int i = 0; i < lines.length; i++) {
			lines[i] = "";
		}

		lineTitle = "";

	}

	private void clearChoice () {
		selectorVisiblePos = 0;
		selectorPointAt = 0;
		visibleHeight = 0;
		visibleHeightPos = 0;
		choices = null;
	}

	@Override
	public boolean keyDown (int keycode) {
		if (keycode == Keys.ENTER) {
			System.out.println("Key: Enter");
			if (currentComponentType == ComponentType.CHOICE) parser.moveToNextComponent(selectorPointAt);

			processComponent();

			return true;
		}

		if (keycode == Keys.UP && currentComponentType == ComponentType.CHOICE) {
			System.out.println("Key: Up");
			if (selectorVisiblePos - 1 < 0) {
				if (selectorPointAt > 0) {
					visibleHeightPos--;
					selectorPointAt--;
					listMove();
				}
				return false;
			}

			selectorVisiblePos--;
			selectorPointAt--;

			return true;
		}

		if (keycode == Keys.DOWN && currentComponentType == ComponentType.CHOICE) {
			System.out.println("Key: Down");

			if (selectorVisiblePos + 1 > visibleHeight - 1) {
				if (selectorPointAt < choices.length - 1) {
					visibleHeightPos++;
					selectorPointAt++;
					listMove();
				}

				return true;
			}

			selectorVisiblePos++;
			selectorPointAt++;

			return true;
		}

		return false;
	}

	private void listMove () {
		System.arraycopy(choices, visibleHeightPos, lines, 0, 3);
	}

	@Override
	public void startSequence (String name) {
		clear();
		parser.startSequence(name);
		processComponent();
	}

	// ====================================================================================================================

	@Override
	public boolean keyUp (int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		return false;
	}

}
