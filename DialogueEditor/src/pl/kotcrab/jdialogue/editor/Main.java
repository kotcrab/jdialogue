package pl.kotcrab.jdialogue.editor;

import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		App.parseArguments(args);
		
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				Editor window = new Editor();
				Editor.window = window;
				
				window.setVisible(true);
				
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] gd = ge.getScreenDevices();
				if(App.getScreenId() > -1 && App.getScreenId() < gd.length)
				{
					window.setLocation(gd[App.getScreenId()].getDefaultConfiguration().getBounds().x, window.getY());
				}
				else if(gd.length > 0)
				{
					window.setLocation(gd[0].getDefaultConfiguration().getBounds().x, window.getY());
				}
				else //really sombady can launch this with no screen?
				{
					throw new RuntimeException("No Screens Found - Get a screen");
				}
				
				window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				
				if(App.getProjectFile() != null) window.loadProject(App.getProjectFile());
			}
		});
	}
	
}