package pl.kotcrab.jdialogue.editor.gui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import pl.kotcrab.jdialogue.editor.project.Callback;

public class CallbackJComboBoxModel extends AbstractListModel<Callback> implements ComboBoxModel<Callback>
{
	private static final long serialVersionUID = 1L;

	ArrayList<Callback> characterList;
	
	Object selection = null;
	
	public CallbackJComboBoxModel(ArrayList<Callback> characterList)
	{
		this.characterList = characterList;
	}
	
	public Callback getElementAt(int index)
	{
		return characterList.get(index);
	}
	
	public int getSize()
	{
		return characterList.size();
	}
	
	public void setSelectedItem(Object anItem)
	{
		selection = anItem;
	}
	
	public Object getSelectedItem()
	{
		return selection;
	}
	
}
