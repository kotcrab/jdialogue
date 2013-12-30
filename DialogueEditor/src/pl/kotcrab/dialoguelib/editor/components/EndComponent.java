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

public class EndComponent extends DComponent
{
	
	private EndComponentTableModel tableModel;

	public EndComponent(int x, int y, int id)
	{
		super("End", x, y, 1, 0, id);
		tableModel = new EndComponentTableModel(id);
	}
	
	@Override
	public ComponentTableModel getTableModel()
	{
		return tableModel;
	}
}

class EndComponentTableModel extends ComponentTableModel
{
	private static final long serialVersionUID = 1L;
	
	public EndComponentTableModel(int id)
	{
		//@formatter:off
		data = new Object[][]
			{
				{"ID", new Integer(id)},
			};
		//@formatter:on
	}
}