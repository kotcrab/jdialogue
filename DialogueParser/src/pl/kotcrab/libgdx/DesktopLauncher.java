package pl.kotcrab.libgdx;

import pl.kotcrab.jdialogue.tests.SimplestLibgdxRendererTest;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Desktop Launcher - SimplestLibgdxRendererTest";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;
		
		new LwjglApplication(new SimplestLibgdxRendererTest(), cfg);
	}
	
}