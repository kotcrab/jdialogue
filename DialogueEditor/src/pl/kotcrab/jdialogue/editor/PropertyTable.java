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

package pl.kotcrab.jdialogue.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import pl.kotcrab.jdialogue.editor.components.ChoiceComponentChoices;
import pl.kotcrab.jdialogue.editor.gui.ChoiceComponentChoicesEditor;
import pl.kotcrab.jdialogue.editor.gui.LeftNumberEditor;

/**
 * Table with custom renderer
 * 
 * @author Pawel Pastuszak
 */
public class PropertyTable extends JTable
{
	private static final long serialVersionUID = 1L;
	
	DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	DefaultCellEditor textEditor;
	
	public PropertyTable(TableModel dm)
	{
		super(dm);
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		
		final JTextField textField = new JTextField();
		textField.setName("Table.editor");
		textField.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				textField.selectAll();
			}
		});
		
		textEditor = new DefaultCellEditor(textField);
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int column)
	{
		Object value = super.getValueAt(row, column);
		if(value instanceof Boolean)
		{
			return getDefaultEditor(Boolean.class);
		}
		if(value instanceof Date)
		{
			return getDefaultEditor(Date.class);
		}
		
		if(value instanceof String)
		{
			return textEditor;
		}
		
		if(value instanceof ChoiceComponentChoices) // TODO za kazdym razem nowy czy stary moze byc?
		{
			return new ChoiceComponentChoicesEditor();
		}
		
		if(value instanceof Integer)
		{
			return new LeftNumberEditor();
		}
		// no special case
		return super.getCellEditor(row, column);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		Object value = super.getValueAt(row, column);
		
		if(value instanceof Boolean)
		{
			return getDefaultRenderer(Boolean.class);
		}
		
		if(value instanceof Integer || value instanceof String || value instanceof ChoiceComponentChoices)
		{
			return leftRenderer;
		}
		
		// no special case
		return super.getCellRenderer(row, column);
	}
	
}
