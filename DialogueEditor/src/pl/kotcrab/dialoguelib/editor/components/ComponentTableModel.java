package pl.kotcrab.dialoguelib.editor.components;

import javax.swing.table.AbstractTableModel;

public abstract class ComponentTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = { "Property", "Value" };
	
	protected Object[][] data = null;
	
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
}