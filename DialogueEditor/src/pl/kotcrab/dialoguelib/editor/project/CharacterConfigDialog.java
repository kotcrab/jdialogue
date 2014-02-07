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

package pl.kotcrab.dialoguelib.editor.project;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import pl.kotcrab.dialoguelib.editor.Editor;

public class CharacterConfigDialog extends JDialog
{
	
	private static final long serialVersionUID = 1L;
	private CharacterConfigDialog instance;
	private final JPanel contentPanel = new JPanel();
	
	/**
	 * Create the dialog.
	 */
	public CharacterConfigDialog(Editor parrent, final Project project)
	{
		super(parrent, true);
		instance = this;
		setTitle("Characters");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (450 / 2), parrent.getY() + (parrent.getHeight() / 2) - (300 / 2), 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			DefaultListModel<Sequence> listModel = new DefaultListModel<Sequence>();
			ArrayList<Sequence> sequences = project.getSequences();
			for(Sequence seq : sequences)
				listModel.addElement(seq);
			
			JList<Sequence> list = new JList<Sequence>(listModel);
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			JScrollPane listScroller = new JScrollPane(list);
			
			contentPanel.add(listScroller);
		}
		{
			JLabel lblSeq1 = new JLabel("Characters:");
			contentPanel.add(lblSeq1, BorderLayout.NORTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnCreateNew = new JButton("Create New");
				btnCreateNew.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						new NewSequenceDialog(instance, project, true);
					}
				});
				buttonPane.add(btnCreateNew);
			}
			{
				JButton okButton = new JButton("Edit");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						// TODO switch seq
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Close");
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
