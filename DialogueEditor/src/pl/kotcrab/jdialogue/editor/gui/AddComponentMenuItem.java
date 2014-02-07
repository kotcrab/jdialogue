package pl.kotcrab.jdialogue.editor.gui;

import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.MenuShortcut;

import pl.kotcrab.jdialogue.editor.components.DComponentType;

public class AddComponentMenuItem extends MenuItem
{
	private static final long serialVersionUID = 1L;
	
	private DComponentType type;
	
	public AddComponentMenuItem(String label, DComponentType type) throws HeadlessException
	{
		super(label);
		this.type = type;
	}
	
	public AddComponentMenuItem(String label, MenuShortcut s, DComponentType type) throws HeadlessException
	{
		super(label, s);
		this.type = type;
	}

	public DComponentType getType()
	{
		return type;
	}
}
