/*******************************************************************************
 * Copyright 2013 - 2014 Pawel Pastuszak
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

package pl.kotcrab.dialoguelib.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NewSequenceDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JTextField textName;
	private JButton btnCreate;
	private JLabel lblErrorLabel;
	
	/**
	 * Create the dialog.
	 */
	public NewSequenceDialog(Editor parrent, final Project project, boolean cancelable)
	{
		super(parrent, true);
		setTitle("New sequence");
		setResizable(false);
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (436 / 2), parrent.getY() + (parrent.getHeight() / 2) - (109 / 2), 436, 109);
		
		getContentPane().setLayout(null);
		
		textName = new JTextField();
		textName.setBounds(10, 26, 414, 20);
		getContentPane().add(textName);
		textName.setColumns(10);
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				project.newSequence(textName.getText(), true);
				dispose();
			}
		});
		btnCreate.setBounds(335, 51, 89, 23);
		getContentPane().add(btnCreate);
		
		JLabel lblEnterSequenceName = new JLabel("Enter sequence name:");
		lblEnterSequenceName.setBounds(10, 11, 414, 14);
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
