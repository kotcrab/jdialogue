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

