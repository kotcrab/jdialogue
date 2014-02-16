package pl.kotcrab.jdialogue.editor.gui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import pl.kotcrab.jdialogue.editor.project.PCharacter;

public class CharactersJComboBoxModel extends AbstractListModel<PCharacter> implements ComboBoxModel<PCharacter>
{
	private static final long serialVersionUID = 1L;

	ArrayList<PCharacter> characterList;
	
	Object selection = null;
	
	public CharactersJComboBoxModel(ArrayList<PCharacter> characterList)
	{
		this.characterList = characterList;
	}
	
	public PCharacter getElementAt(int index)
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
