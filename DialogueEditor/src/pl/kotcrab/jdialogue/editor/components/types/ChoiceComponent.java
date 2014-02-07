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

package pl.kotcrab.jdialogue.editor.components.types;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import pl.kotcrab.jdialogue.editor.components.ComponentTableModel;
import pl.kotcrab.jdialogue.editor.components.DComponent;

public class ChoiceComponent extends DComponent
{
	public ChoiceComponent(int x, int y, int id)
	{
		super("Choice", x, y, 1, 3, id);
		
		tableModel = new ComponentTableModel(
			//@formatter:off
			new Object[][]
					{
						{"ID", new Integer(id)},
					    {"Outputs", new Integer(3)},
					}
			//@formatter:on
		);
		
		setListeners();
	}
	
	@Override
	protected void setListeners()
	{
		tableModel.addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(TableModelEvent e)
			{
				resize(getInputs().length, (int) tableModel.getValueAt(0, 1));
			}
		});
	}
}
