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

public class RelayComponent extends DComponent
{
	private RelayComponentTableModel tableModel;
	
	public RelayComponent(int x, int y)
	{
		super("Relay", x, y, 3, 1);
		
		tableModel = new RelayComponentTableModel();
		
		tableModel.addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(TableModelEvent e)
			{
				resize((int) tableModel.getValueAt(0, 1), getOutputs().length);
			}
		});
	}
	
	@Override
	public ComponentTableModel getTableModel()
	{
		return tableModel;
	}
}

class RelayComponentTableModel extends ComponentTableModel
{
	private static final long serialVersionUID = 1L;
	
	public RelayComponentTableModel()
	{
		//@formatter:off
		data = new Object[][]
			{
			    {"Inputs", new Integer(3)},
			};
		//@formatter:on
	}
}