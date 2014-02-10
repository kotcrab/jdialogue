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

public class CharacterCreationDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JTextField textName;
	private JButton btnCreate;
	private JLabel lblErrorLabel;
	private JTextField textTextureName;
	
	/**
	 * Create the dialog.
	 */
	public CharacterCreationDialog(Window parrent, final Project project, Character character, final boolean newMode)
	{
		super(parrent, ModalityType.APPLICATION_MODAL);
		// super(parrent, true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if(newMode)
			setTitle("Character settings");
		else
			setTitle("New character");
		
		setResizable(false);
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (436 / 2), parrent.getY() + (parrent.getHeight() / 2) - (109 / 2), 436, 144);
		
		getContentPane().setLayout(null);
		
		textName = new JTextField();
		textName.setBounds(10, 21, 414, 24);
		getContentPane().add(textName);
		textName.setColumns(10);
		
		if(newMode)
			btnCreate = new JButton("Create");
		else
			btnCreate = new JButton("Save");
		
		btnCreate.setEnabled(false);
		btnCreate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				if(newMode)
					project.newCharacter(textName.getText(), textTextureName.getText());
				else
				{
					
					//TODO edit char
				}
				
				dispose();
			}
		});
		btnCreate.setBounds(335, 87, 89, 23);
		getContentPane().add(btnCreate);
		
		JLabel lblEnterSequenceName = new JLabel("Character name:");
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
		btnCancel.setBounds(236, 87, 89, 23);
		getContentPane().add(btnCancel);
		
		lblErrorLabel = new JLabel("Enter character name.");
		lblErrorLabel.setForeground(Color.RED);
		lblErrorLabel.setBounds(10, 91, 216, 14);
		getContentPane().add(lblErrorLabel);
		
		textTextureName = new JTextField();
		textTextureName.setColumns(10);
		textTextureName.setBounds(10, 60, 414, 24);
		getContentPane().add(textTextureName);
		
		JLabel lblCharacterTextureName = new JLabel("Character texture name:");
		lblCharacterTextureName.setBounds(10, 45, 414, 14);
		getContentPane().add(lblCharacterTextureName);
		
		ChangeListener changeListener = new ChangeListener();
		
		textName.getDocument().addDocumentListener(changeListener);
		textTextureName.getDocument().addDocumentListener(changeListener);
		
		setVisible(true);
	}
	
	public void checkConditions()
	{
		btnCreate.setEnabled(false);
		lblErrorLabel.setVisible(true);
		
		if(textName.getText().equals(""))
		{
			lblErrorLabel.setText("Enter character name.");
			return;
		}
		
		if(textTextureName.getText().equals(""))
		{
			lblErrorLabel.setText("Enter texture name or type none.");
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
