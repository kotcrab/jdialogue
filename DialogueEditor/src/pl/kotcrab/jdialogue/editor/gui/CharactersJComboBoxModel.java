package pl.kotcrab.jdialogue.editor.gui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import pl.kotcrab.jdialogue.editor.project.DCharacter;

public class CharactersJComboBoxModel extends AbstractListModel<DCharacter> implements ComboBoxModel<DCharacter>
{
	private static final long serialVersionUID = 1L;

	ArrayList<DCharacter> characterList;
	
	Object selection = null;
	
	public CharactersJComboBoxModel(ArrayList<DCharacter> characterList)
	{
		this.characterList = characterList;
	}
	
	public DCharacter getElementAt(int index)
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
