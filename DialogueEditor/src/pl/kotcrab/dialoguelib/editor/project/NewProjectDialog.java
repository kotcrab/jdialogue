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

package pl.kotcrab.dialoguelib.editor.project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pl.kotcrab.dialoguelib.editor.App;
import pl.kotcrab.dialoguelib.editor.Editor;

public class NewProjectDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private NewProjectDialog instance;
	private JTextField textProjectName;
	private JTextField textProjectLoc;
	private JTextField textCustomOutLoc;
	private JLabel lblErrorLabel;
	private JCheckBox chckUseCustomLoc;
	private JButton btnCreate;
	final JFileChooser fc = new JFileChooser();
	
	/**
	 * Create the dialog.
	 */
	public NewProjectDialog(final Editor parrent)
	{
		super(parrent, true);
		
		instance = this;
		
		setTitle("New Project");
		setResizable(false);
		
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (450 / 2), parrent.getY() + (parrent.getHeight() / 2) - (260 / 2), 450, 260);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		String path = App.getLastOpenedFolderPath();
		if(path != null) fc.setCurrentDirectory(new File(path));
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JLabel lblProjectName = new JLabel("Project name:");
		lblProjectName.setBounds(10, 11, 89, 14);
		getContentPane().add(lblProjectName);
		
		textProjectName = new JTextField();
		textProjectName.setBounds(87, 8, 347, 23);
		getContentPane().add(textProjectName);
		textProjectName.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(10, 36, 67, 14);
		getContentPane().add(lblLocation);
		
		textProjectLoc = new JTextField();
		textProjectLoc.setBounds(87, 33, 248, 23);
		getContentPane().add(textProjectLoc);
		textProjectLoc.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				textProjectLoc.setText(openSelecFolderDialog());
			}
		});
		btnBrowse.setBounds(345, 32, 89, 23);
		getContentPane().add(btnBrowse);
		
		final JCheckBox chckGzipProjectFiles = new JCheckBox("Use GZIP Compression on project files");
		chckGzipProjectFiles.setSelected(true);
		chckGzipProjectFiles.setBounds(10, 57, 424, 23);
		getContentPane().add(chckGzipProjectFiles);
		
		final JCheckBox chckGzipExportedFiles = new JCheckBox("Use GZIP Compression on exported files");
		chckGzipExportedFiles.setSelected(true);
		chckGzipExportedFiles.setBounds(10, 83, 424, 23);
		getContentPane().add(chckGzipExportedFiles);
		
		chckUseCustomLoc = new JCheckBox("Use default output location");
		
		chckUseCustomLoc.setSelected(true);
		chckUseCustomLoc.setBounds(10, 109, 424, 23);
		getContentPane().add(chckUseCustomLoc);
		
		JLabel label = new JLabel("Location:");
		label.setBounds(10, 140, 67, 14);
		getContentPane().add(label);
		
		textCustomOutLoc = new JTextField();
		textCustomOutLoc.setEnabled(false);
		textCustomOutLoc.setColumns(10);
		textCustomOutLoc.setBounds(87, 137, 248, 23);
		getContentPane().add(textCustomOutLoc);
		
		final JButton btnCustomLocBrowse = new JButton("Browse...");
		btnCustomLocBrowse.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				textCustomOutLoc.setText(openSelecFolderDialog());
			}
		});
		btnCustomLocBrowse.setEnabled(false);
		btnCustomLocBrowse.setBounds(345, 136, 89, 23);
		getContentPane().add(btnCustomLocBrowse);
		
		JLabel lblOutInfo = new JLabel("<html>This can be changed for your project directory. For example: LibgdxGame-android/assets/dialog</html>");
		lblOutInfo.setBounds(10, 165, 424, 34);
		getContentPane().add(lblOutInfo);
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Project project = new Project(textProjectName.getText(), textProjectLoc.getText(), chckGzipProjectFiles.isSelected(), chckGzipExportedFiles.isSelected());

				if(chckUseCustomLoc.isSelected())
					project.setCustomOut(textCustomOutLoc.getText());
				
				parrent.newProject(project);
				instance.dispose();
			}
		});
		btnCreate.setEnabled(false);
		btnCreate.setBounds(345, 205, 89, 23);
		getContentPane().add(btnCreate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				instance.dispose();
			}
		});
		btnCancel.setBounds(246, 205, 89, 23);
		getContentPane().add(btnCancel);
		
		lblErrorLabel = new JLabel("Enter a project name");
		lblErrorLabel.setForeground(Color.RED);
		lblErrorLabel.setBounds(10, 208, 224, 16);
		getContentPane().add(lblErrorLabel);
		
		chckUseCustomLoc.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(chckUseCustomLoc.isSelected())
				{
					textCustomOutLoc.setEnabled(false);
					btnCustomLocBrowse.setEnabled(false);
				}
				else
				{
					textCustomOutLoc.setEnabled(true);
					btnCustomLocBrowse.setEnabled(true);
				}
				
				checkConditions();
			}
		});
		
		ChangeListener changeListener = new ChangeListener();
		
		textCustomOutLoc.getDocument().addDocumentListener(changeListener);
		textProjectLoc.getDocument().addDocumentListener(changeListener);
		textProjectName.getDocument().addDocumentListener(changeListener);
		
		setVisible(true);
	}
	
	public String openSelecFolderDialog()
	{
		int returnVal = fc.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String path = fc.getSelectedFile().getAbsolutePath();
			App.setLastOpenedFolder(path);
			return path;
		}
		else
			return null;
		
	}
	
	public void checkConditions()
	{
		btnCreate.setEnabled(false);
		lblErrorLabel.setVisible(true);
		
		if(textProjectName.getText().equals(""))
		{
			lblErrorLabel.setText("Enter a project name.");
			return;
		}
		
		if(textProjectLoc.getText().equals(""))
		{
			lblErrorLabel.setText("Enter a project location.");
			return;
		}
		
		if(new File(textProjectLoc.getText()).isDirectory() == false)
		{
			lblErrorLabel.setText("Entered project location is invalid.");
			return;
		}
		
		if(chckUseCustomLoc.isSelected() == false)
		{
			if(textCustomOutLoc.getText().equals(""))
			{
				lblErrorLabel.setText("Enter a project output location.");
				return;
			}
			
			if(new File(textCustomOutLoc.getText()).isDirectory() == false)
			{
				lblErrorLabel.setText("Entered project output location is invalid.");
				
			}
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
