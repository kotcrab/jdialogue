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

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import pl.kotcrab.jdialogue.editor.components.ChoiceComponentChoices;

public class ChoiceComponentChoicesEditor extends DefaultCellEditor
{
	private static final long serialVersionUID = 1L;
	
	protected static final String EDIT = "edit";
	
	ChoicesEditDialog dialog;
	
	public ChoiceComponentChoicesEditor()
	{
		super(new JTextField());
		setClickCountToStart(2);
	}
	
	@Override
	public Object getCellEditorValue()
	{
		return null;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		ChoiceComponentChoices choices = (ChoiceComponentChoices) value;

		dialog = new ChoicesEditDialog(choices);
		
		return null;
	}
	
}
