/*******************************************************************************
 * DialogueEditor
 * Copyright (C) 2013-2014 Pawel Pastuszak
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package pl.kotcrab.jdialogue.editor.components.types;

import pl.kotcrab.jdialogue.editor.Assets;
import pl.kotcrab.jdialogue.editor.KotcrabText;
import pl.kotcrab.jdialogue.editor.components.ChoiceComponentChoices;
import pl.kotcrab.jdialogue.editor.components.ComponentTableModel;
import pl.kotcrab.jdialogue.editor.components.DComponent;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class ChoiceComponent extends DComponent {
	private ChoiceComponentChoices choices;

	public ChoiceComponent (int x, int y) {
		super("Choice", x, y, 1, 3);

		choices = new ChoiceComponentChoices(3);
		tableModel = new ComponentTableModel(
//@formatter:off
			new Object[][]
					{
					    {"Outputs", new Integer(3)},
					    {"Text", "Set Text"},
					    {"Choices text", choices},
					}
			//@formatter:on
		);

		setListeners();
	}

	@Override
	protected void setListeners () {
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged (TableModelEvent e) {
				if (e.getFirstRow() == 0 && e.getLastRow() == 0) {
					resize(getInputs().length, (int) tableModel.getValueAt(0, 1));
					choices.resize(getOutputs().length);
					tableModel.fireTableRowsUpdated(3, 3);
				}
			}
		});
	}

	public ChoiceComponentChoices getChoices () {
		return choices;
	}

	public void setChoices (ChoiceComponentChoices choices) {
		this.choices = choices;
	}

	@Override
	public KotcrabText[] provideInputLabels () {
		return new KotcrabText[]{new KotcrabText(Assets.consolasFont, "In", false, 0, 0)};
	}

	@Override
	public KotcrabText[] provideOutputsLabels () {
		return new KotcrabText[]{new KotcrabText(Assets.consolasFont, "Out(s)", false, 0, 0)};
	}
}
