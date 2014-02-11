package pl.kotcrab.jdialogue.editor.gui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import pl.kotcrab.jdialogue.editor.project.Character;

public class CharactersJComboBoxModel extends AbstractListModel<Character> implements ComboBoxModel<Character>
{
	private static final long serialVersionUID = 1L;

	ArrayList<Character> characterList;
	
	Object selection = null;
	
	public CharactersJComboBoxModel(ArrayList<Character> characterList)
	{
		this.characterList = characterList;
	}
	
	public Character getElementAt(int index)
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
