/*******************************************************************************
    DialogueEditor
    Copyright (C) 2013-2014 Pawel Pastuszak

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
