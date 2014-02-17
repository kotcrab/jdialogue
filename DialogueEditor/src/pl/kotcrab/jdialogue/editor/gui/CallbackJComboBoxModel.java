package pl.kotcrab.jdialogue.editor.gui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import pl.kotcrab.jdialogue.editor.project.PCallback;

public class CallbackJComboBoxModel extends AbstractListModel<PCallback> implements ComboBoxModel<PCallback>
{
	private static final long serialVersionUID = 1L;

	ArrayList<PCallback> characterList;
	
	Object selection = null;
	
	public CallbackJComboBoxModel(ArrayList<PCallback> characterList)
	{
		this.characterList = characterList;
	}
	
	public PCallback getElementAt(int index)
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
