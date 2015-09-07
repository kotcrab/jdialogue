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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Touch {
	private static OrthographicCamera camera; // kamera
	private static Vector3 calcVector; // wektor do obliczen

	/** Przygotowywuje klase do uzytku */
	public static void setCamera (OrthographicCamera camera) {
		Touch.camera = camera;
		calcVector = new Vector3(0, 0, 0);
	}

	/** Zwaraca kamere */
	public static OrthographicCamera getCamera () {
		return camera;
	}

	/**
	 * Zwaraca poprawne miejsce dotyku pobrane przy ucyzciu Gdx.input.getX() Aby uzywac nalezy wyowalc jednorazowo setCamera()
	 *
	 * @param x
	 *            pochadzace z Gdx.input.getX()
	 */
	public static int calcX (float x) {
		calcVector.x = x;
		camera.unproject(calcVector);
		return (int) calcVector.x;
	}

	/**
	 * Zwaraca poprawne miejsce dotyku pobrane przy ucyzciu Gdx.input.getY() Aby uzywac nalezy wyowalc jednorazowo setCamera()
	 *
	 * @param y
	 *            pochadzace z Gdx.input.getY()
	 */
	public static int calcY (float y) {
		calcVector.y = y;
		camera.unproject(calcVector);
		return (int) calcVector.y;
	}

	public static int getX () {
		calcVector.x = Gdx.input.getX();
		camera.unproject(calcVector);
		return (int) calcVector.x;
	}

	public static int getY () {
		calcVector.y = Gdx.input.getY();
		camera.unproject(calcVector);
		return (int) calcVector.y;
	}

}
