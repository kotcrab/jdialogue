package pl.kotcrab.dialoguelib.editor;

import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Table with custom renderer
 * @author Pawel Pastuszak
 */
public class PropertyTable extends JTable
{
	private static final long serialVersionUID = 1L;

	public PropertyTable(TableModel dm)
	{
		super(dm);
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
