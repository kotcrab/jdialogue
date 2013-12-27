package pl.kotcrab.dialoguelib.editor.components;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ConnectionRenderer
{
	public void render(ShapeRenderer shapeRenderer, DComponent comp)
	{
		Connection[] inputs = comp.getOutputs();
		
		for (int i = 0; i < inputs.length; i++)
		{
			Connection con = inputs[i];
			Connection target = con.getTarget();
			
			if(target == null) continue;
			
			float x1 = con.getX() + 6;
			float y1 = con.getY() + 6;
			float x2 = target.getX() + 6;
			float y2 = target.getY() + 6;
			
			float d = Math.abs(y1 - y2);
			if(d > 200) d = 200; // limit
			
			shapeRenderer.curve(x1, y1, x1 + d, y1, x2 - d, y2, x2, y2, 32);
		}
	}
	
}