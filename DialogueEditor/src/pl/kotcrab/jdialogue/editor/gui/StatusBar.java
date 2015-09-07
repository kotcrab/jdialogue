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

package pl.kotcrab.jdialogue.editor.gui;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusBar extends JLabel {
	private static final long serialVersionUID = 1L;

	private Timer displayTimer;

	public StatusBar (String text) {
		super.setText(text);

		displayTimer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				setText("");
				displayTimer.stop();
			}
		});

		displayTimer.setRepeats(false);
	}

	public void setStatusText (String text) {
		setText(text);
		displayTimer.start();
	}
}
