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

package pl.kotcrab.dialoguelib.editor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

public class CameraUtils
{
	public static Rectangle calcCameraBoundingRectangle(OrthographicCamera camera)
	{
		float cameraWidth = camera.viewportWidth * camera.zoom;
		float cameraHeight = camera.viewportHeight * camera.zoom;
		
		float cameraX = camera.position.x - cameraWidth / 2;
		float cameraY = camera.position.y - cameraHeight / 2;
		
		return new Rectangle(cameraX, cameraY, cameraWidth, cameraHeight);
	}
}

