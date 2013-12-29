/*******************************************************************************
 * Copyright 2013 Pawel Pastuszak
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

package pl.kotcrab.dialoguelib.editor.components;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class ChoiceComponent extends DComponent
{
	private ChoiceComponentTableModel tableModel;
	
	public ChoiceComponent(int x, int y, int id)
	{
		super("Choice", x, y, 1, 3, id);
		
		tableModel = new ChoiceComponentTableModel();
		
		tableModel.addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(TableModelEvent e)
			{
				resize(getInputs().length, (int) tableModel.getValueAt(0, 1));
			}
		});
	}
	
	@Override
	public ComponentTableModel getTableModel()
	{
		return tableModel;
	}
}

class ChoiceComponentTableModel extends ComponentTableModel
{
	private static final long serialVersionUID = 1L;
	
	public ChoiceComponentTableModel()
	{
		//@formatter:off
		data = new Object[][]
			{
			    {"Outputs", new Integer(3)},
			};
		//@formatter:on
	}
}