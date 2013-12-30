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


public class TextComponent extends DComponent
{
	TextComponentTableModel tableModel;
	
	public TextComponent(int x, int y, int id)
	{
		super("Show Text", x, y, 1, 1, id);
		tableModel = new TextComponentTableModel(id);
	}
	
	@Override
	public ComponentTableModel getTableModel()
	{
		return tableModel;
	}
}

class TextComponentTableModel extends ComponentTableModel
{
	private static final long serialVersionUID = 1L;
	
	public TextComponentTableModel(int id)
	{
		//@formatter:off
		data = new Object[][]
			{
				{"ID", new Integer(id)},
			    {"Text", "Set Text"},
			    {"Sayer", "TODO"}
			};
		//@formatter:on
	}
}