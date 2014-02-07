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

package pl.kotcrab.jdialogue.editor.components;

import javax.swing.table.AbstractTableModel;

public class ComponentTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = { "Property", "Value" };
	
	protected Object[][] data = null;
	
	public ComponentTableModel(Object[][] tableData)
	{
		this.data = tableData;
		
		if(data == null)
		{
			//@formatter:off
			data = new Object[][]
					{
					    {"Info", "This component does not contain any properties"}
					};
			//@formatter:on
		}
	}
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	@Override
	public int getRowCount()
	{
		return data.length;
	}
	
	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}
	
	@Override
	public Class<?> getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		if(col == 1)
			return true;
		else
			return false;
		
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	public Object[][] getData()
	{
		return data;
	}
}