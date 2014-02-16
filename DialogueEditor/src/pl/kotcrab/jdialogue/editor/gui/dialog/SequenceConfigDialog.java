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

package pl.kotcrab.jdialogue.editor.gui.dialog;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.thoughtworks.xstream.XStream;

import pl.kotcrab.jdialogue.editor.Editor;
import pl.kotcrab.jdialogue.editor.project.Project;
import pl.kotcrab.jdialogue.editor.project.Sequence;

public class SequenceConfigDialog extends JDialog
{
	
	private static final long serialVersionUID = 1L;
	private SequenceConfigDialog instance;
	private final JPanel contentPanel = new JPanel();
	
	private Project project;
	
	JList<Sequence> list;
	
	/**
	 * Create the dialog.
	 */
	public SequenceConfigDialog(final Editor parrent, final Project project, final XStream xstream)
	{
		super(parrent, true);
		instance = this;
		this.project = project;
		setTitle("Sequences");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (450 / 2), parrent.getY() + (parrent.getHeight() / 2) - (300 / 2), 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		list = new JList<Sequence>();
		
		refreshList();
		
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(list);
		
		contentPanel.add(listScroller);
		JLabel lblSeq1 = new JLabel("Switch sequnce:");
		contentPanel.add(lblSeq1, BorderLayout.NORTH);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton btnCreateNew = new JButton("Create New");
		btnCreateNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new SequenceCreationDialog(instance, project, true, null, null);
				refreshList();
			}
		});
		buttonPane.add(btnCreateNew);
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(list.getSelectedValue() == null)
				{
					JOptionPane.showMessageDialog(parrent, "Please select sequence", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				setVisible(false);
				project.switchActiveSequence(list.getSelectedValue().getName());
				dispose();
			}
		});
		JButton btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(list.getSelectedValue() == null)
				{
					JOptionPane.showMessageDialog(parrent, "Please select sequence", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				new SequenceCreationDialog(instance, project, true, list.getSelectedValue(), xstream);
				refreshList();
			}
		});
		buttonPane.add(btnRename);
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(list.getModel().getSize() == 1)
				{
					JOptionPane.showMessageDialog(parrent, "This sequence cannnot be deleted because there are no other sequences", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		buttonPane.add(btnDelete);
		btnOk.setActionCommand("OK");
		buttonPane.add(btnOk);
		getRootPane().setDefaultButton(btnOk);
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void refreshList()
	{
		DefaultListModel<Sequence> listModel = new DefaultListModel<Sequence>();
		ArrayList<Sequence> sequences = project.getSequences();
		for(Sequence seq : sequences)
			listModel.addElement(seq);
		
		list.setModel(listModel);
	}
}
