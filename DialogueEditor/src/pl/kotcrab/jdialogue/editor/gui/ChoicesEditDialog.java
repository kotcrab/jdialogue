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

package pl.kotcrab.jdialogue.editor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import pl.kotcrab.jdialogue.editor.Editor;
import pl.kotcrab.jdialogue.editor.components.ChoiceComponentChoices;

public class ChoicesEditDialog extends JDialog
{
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private JTable table;
	private int tableLength;
	
	/**
	 * Create the dialog.
	 * 
	 * @param choicesComponent
	 */
	public ChoicesEditDialog(final ChoiceComponentChoices choicesComponent)
	{
		super(Editor.window, true);
		setTitle("Choices Editor");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Editor parrent = Editor.window;
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (450 / 2), parrent.getY() + (parrent.getHeight() / 2) - (300 / 2), 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			String[] choices = choicesComponent.getChoicesTable();
			tableLength = choices.length;
			
			String[][] tableModelData = new String[choices.length][1];
			
			for(int i = 0; i < choices.length; i++)
			{
				tableModelData[i][0] = choices[i];
			}
			
			String[] columnName = { "Values" };
			
			table = new JTable(tableModelData, columnName);
			table.setRowHeight(20);
			
			JScrollPane scrollPane = new JScrollPane(table);
			table.setFillsViewportHeight(true);
			table.getTableHeader().setReorderingAllowed(false);
			contentPanel.add(scrollPane);
		}
		{
			JLabel lblSeq1 = new JLabel("Choices                                                  (make sure to hit Enter after editing field)");
			contentPanel.add(lblSeq1, BorderLayout.NORTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String[] newChoices = new String[tableLength];
						for(int i = 0; i < newChoices.length; i++)
						{
							newChoices[i] = table.getModel().getValueAt(i, 0).toString();
						}
						
						choicesComponent.setChoices(newChoices);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
