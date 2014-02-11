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

import pl.kotcrab.jdialogue.editor.components.ComponentTableModel;
import pl.kotcrab.jdialogue.editor.components.DComponent;
import pl.kotcrab.jdialogue.editor.project.Character;

public class TextComponent extends DComponent
{
	public TextComponent(int x, int y)
	{
		super("Show Text", x, y, 1, 1);
		tableModel = new ComponentTableModel(
//@formatter:off
		new Object[][]
				{
				    {"Text", "Set Text"},
				    {"Character", new Character("None (default character)", "none")}
				}
		//@formatter:on
		);
	}
	
}