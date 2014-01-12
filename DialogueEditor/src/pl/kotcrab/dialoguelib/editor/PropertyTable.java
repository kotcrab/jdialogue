/*******************************************************************************
 * Copyright 2013 - 2014 Pawel Pastuszak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package pl.kotcrab.dialoguelib.editor;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

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
		
		JTextField textField = new JTextField();
		textField.setName("Table.editor");
		
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
		
		if(value instanceof Integer || value instanceof String)
		{
			return leftRenderer;
		}
		// no special case
		return super.getCellRenderer(row, column);
	}
	
	@SuppressWarnings("rawtypes")
	static class LeftNumberEditor extends DefaultCellEditor //modifed copy of JTable.GenericEditor
	{
		private static final long serialVersionUID = 1L;
		Class[] argTypes = new Class[] { String.class };
		java.lang.reflect.Constructor constructor;
		Object value;
		
		public LeftNumberEditor()
		{
			super(new JTextField());
			getComponent().setName("Table.editor");
			((JTextField) getComponent()).setHorizontalAlignment(JTextField.LEFT); // changed aligment
		}
		
		public boolean stopCellEditing()
		{
			String s = (String) super.getCellEditorValue();
			// Here we are dealing with the case where a user
			// has deleted the string value in a cell, possibly
			// after a failed validation. Return null, so that
			// they have the option to replace the value with
			// null or use escape to restore the original.
			// For Strings, return "" for backward compatibility.
			if("".equals(s))
			{
				((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
				return false;
			}
			
			try
			{
				value = constructor.newInstance(new Object[] { s });
			}
			catch (Exception e)
			{
				((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
				return false;
			}
			
			int a = new Integer(s); // added value checking
			
			if(a <= 0 || a > 999)
			{
				((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
				return false;
			}
			
			return super.stopCellEditing();
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
		{
			this.value = null;
			((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
			try
			{
				Class<?> type = table.getColumnClass(column);
				// Since our obligation is to produce a value which is
				// assignable for the required type it is OK to use the
				// String constructor for columns which are declared
				// to contain Objects. A String is an Object.
				if(type == Object.class)
				{
					type = String.class;
				}
				constructor = type.getConstructor(argTypes);
			}
			catch (Exception e)
			{
				return null;
			}
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}
		
		public Object getCellEditorValue()
		{
			return value;
		}
	}
}
