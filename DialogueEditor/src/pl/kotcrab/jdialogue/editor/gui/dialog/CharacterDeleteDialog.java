package pl.kotcrab.jdialogue.editor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.kotcrab.jdialogue.editor.project.PCharacter;
import pl.kotcrab.jdialogue.editor.project.Project;

public class CharacterDeleteDialog extends JDialog
{
	
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	
	/**
	 * Create the dialog.
	 */
	public CharacterDeleteDialog(Window parrent, final Project project, final PCharacter character)
	{
		super(parrent, ModalityType.APPLICATION_MODAL);
		
		setTitle("Character delete - " + character.getName());
		setBounds(parrent.getX() + (parrent.getWidth() / 2) - (450 / 2), parrent.getY() + (parrent.getHeight() / 2) - (191 / 2), 450, 191);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		JLabel lblLabel = new JLabel("<html>Are you sure you want to delete this character? <br> <br>WARNING: All components using this character won't be updated! Their configuration will be invalid and may cause problems during runtime. <br><br>It's recommended to not delete character</html>");
		contentPanel.add(lblLabel);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				project.deleteCharacter(character);
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
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
		
		setVisible(true);
	}
}
