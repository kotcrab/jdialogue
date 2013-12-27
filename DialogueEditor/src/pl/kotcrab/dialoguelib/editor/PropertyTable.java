package pl.kotcrab.dialoguelib.editor;

import java.util.Date;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Table with custom renderer
 * @author Pawel Pastuszak
 */
public class PropertyTable extends JTable
{
	private static final long serialVersionUID = 1L;

	public PropertyTable()
	{
		// TODO Auto-generated constructor stub
	}
	
	public PropertyTable(TableModel dm)
	{
		super(dm);
		// TODO Auto-generated constructor stub
	}
	
	public PropertyTable(TableModel dm, TableColumnModel cm)
	{
		super(dm, cm);
		// TODO Auto-generated constructor stub
	}
	
	public PropertyTable(int numRows, int numColumns)
	{
		super(numRows, numColumns);
		// TODO Auto-generated constructor stub
	}
	
	public PropertyTable(Vector rowData, Vector columnNames)
	{
		super(rowData, columnNames);
		// TODO Auto-generated constructor stub
	}
	
	public PropertyTable(Object[][] rowData, Object[] columnNames)
	{
		super(rowData, columnNames);
		// TODO Auto-generated constructor stub
	}
	
	public PropertyTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
	{
		super(dm, cm, sm);
		// TODO Auto-generated constructor stub
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
		if(value instanceof Integer)
		{
			return getDefaultEditor(Integer.class);
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
		if(value instanceof Date)
		{
			return getDefaultRenderer(Date.class);
		}
		if(value instanceof Integer)
		{
			return getDefaultRenderer(Integer.class);
		}
		// no special case
		return super.getCellRenderer(row, column);
	}
	
}
