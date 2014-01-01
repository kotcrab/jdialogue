package pl.kotcrab.dialoguelib.editor;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class NewProjectDialog extends JDialog
{
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	/**
	 * Create the dialog.
	 */
	public NewProjectDialog(JFrame parrent)
	{
		super(parrent, true);
		setTitle("New Project");
		setResizable(false);
		
		setBounds(parrent.getX() + 100, parrent.getY() + 100, 450, 300); //TODO center dialog
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblProjectName = new JLabel("Project name:");
		lblProjectName.setBounds(10, 11, 89, 14);
		getContentPane().add(lblProjectName);
		
		textField = new JTextField();
		textField.setBounds(87, 8, 347, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(10, 36, 67, 14);
		getContentPane().add(lblLocation);
		
		textField_1 = new JTextField();
		textField_1.setBounds(87, 33, 248, 20);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("Browse...");
		btnNewButton.setBounds(345, 32, 89, 23);
		getContentPane().add(btnNewButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Use GZIP Compression on project files");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(10, 57, 424, 23);
		getContentPane().add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Use GZIP Compression on exported files");
		chckbxNewCheckBox_1.setSelected(true);
		chckbxNewCheckBox_1.setBounds(10, 83, 424, 23);
		getContentPane().add(chckbxNewCheckBox_1);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Use default output location");
		chckbxNewCheckBox_2.setSelected(true);
		chckbxNewCheckBox_2.setBounds(10, 109, 424, 23);
		getContentPane().add(chckbxNewCheckBox_2);
		
		JLabel label = new JLabel("Location:");
		label.setBounds(10, 140, 67, 14);
		getContentPane().add(label);
		
		textField_2 = new JTextField();
		textField_2.setEnabled(false);
		textField_2.setColumns(10);
		textField_2.setBounds(87, 137, 248, 20);
		getContentPane().add(textField_2);
		
		JButton button = new JButton("Browse...");
		button.setEnabled(false);
		button.setBounds(345, 136, 89, 23);
		getContentPane().add(button);
		
		JLabel lblNewLabel = new JLabel("<html>This can be changed for your project directory. For example: LibgdxGame-android/assets/dialog</html>");
		lblNewLabel.setBounds(10, 165, 424, 34);
		getContentPane().add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("Create");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setBounds(345, 238, 89, 23);
		getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Cancel");
		btnNewButton_2.setBounds(246, 238, 89, 23);
		getContentPane().add(btnNewButton_2);
		setVisible(true);
	}
}
