package pl.kotcrab.jdialogue.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

public class StatusBar extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	private Timer displayTimer;
	
	public StatusBar(String text)
	{
		super.setText(text);
		
		displayTimer = new Timer(3000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setText("");
				displayTimer.stop();
			}
		});
		
		displayTimer.setRepeats(false);
	}
	
	public void setStatusText(String text)
	{
		setText(text);
		displayTimer.start();
	}
}
