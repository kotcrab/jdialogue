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

package pl.kotcrab.jdialogue.editor.project;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pl.kotcrab.jdialogue.editor.Editor;

public class NewSequenceDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JTextField textName;
	private JButton btnCreate;
	private JLabel lblErrorLabel;
	
	/**
	 * Create the dialog.
	 */
	public NewSequenceDialog(Window parrent, final Project project, boolean cancelable)
	{
		super(parrent, ModalityType.APPLICATION_MODAL);
		// super(parrent, true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("New sequence");
		setResizable(false);
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (436 / 2), parrent.getY() + (parrent.getHeight() / 2) - (109 / 2), 436, 109);
		
		getContentPane().setLayout(null);
		
		textName = new JTextField();
		textName.setBounds(10, 21, 414, 24);
		getContentPane().add(textName);
		textName.setColumns(10);
		
		btnCreate = new JButton("Create");
		btnCreate.setEnabled(false);
		btnCreate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for(Sequence seq : project.getSequences())
				{
					if(seq.getName().equals(textName.getText()))
					{
						JOptionPane.showMessageDialog(Editor.window, "Sequences with this name already exist, pick a diffrent name!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				project.newSequence(textName.getText(), true);
				dispose();
			}
		});
		btnCreate.setBounds(335, 51, 89, 23);
		getContentPane().add(btnCreate);
		
		JLabel lblEnterSequenceName = new JLabel("Enter sequence name:");
		lblEnterSequenceName.setBounds(10, 6, 414, 14);
		getContentPane().add(lblEnterSequenceName);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		btnCancel.setBounds(236, 51, 89, 23);
		getContentPane().add(btnCancel);
		
		if(cancelable == false) btnCancel.setEnabled(cancelable);
		
		lblErrorLabel = new JLabel("Enter a sequence name.");
		lblErrorLabel.setForeground(Color.RED);
		lblErrorLabel.setBounds(10, 55, 216, 14);
		getContentPane().add(lblErrorLabel);
		
		textName.getDocument().addDocumentListener(new ChangeListener());
		
		setVisible(true);
	}
	
	public void checkConditions()
	{
		btnCreate.setEnabled(false);
		lblErrorLabel.setVisible(true);
		
		if(textName.getText().equals(""))
		{
			lblErrorLabel.setText("Enter a sequence name.");
			return;
		}
		
		btnCreate.setEnabled(true);
		lblErrorLabel.setVisible(false);
	}
	
	class ChangeListener implements DocumentListener
	{
		@Override
		public void insertUpdate(DocumentEvent e)
		{
			checkConditions();
		}
		
		@Override
		public void removeUpdate(DocumentEvent e)
		{
			checkConditions();
		}
		
		@Override
		public void changedUpdate(DocumentEvent e)
		{
			checkConditions();
		}
	}
}
