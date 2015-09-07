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

package pl.kotcrab.jdialogue.editor;

import pl.kotcrab.jdialogue.editor.components.DComponentType;
import pl.kotcrab.jdialogue.editor.gui.AddComponentMenuItem;
import pl.kotcrab.jdialogue.editor.gui.StatusBar;
import pl.kotcrab.jdialogue.editor.project.Project;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.io.File;

public class Editor extends JFrame {
	private static final long serialVersionUID = 1L;

	public static Editor window;

	private EditorLogic logic;

	private JPanel contentPane;

	private JSplitPane rendererSplitPane;
	private PropertyTable table;

	private JFileChooser loadProjectFileChooser;

	/**
	 * Create the frame.
	 */
	public Editor () {
		super("Dialogue Editor");

		logic = new EditorLogic(this);

		addWindowListener(logic.winAdapater);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		setMinimumSize(new Dimension(950, 425));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		createMenuBar();
		createToolbar();
		createPopupMenu();

		logic.createRenderer();

		loadProjectFileChooser = new JFileChooser(App.getLastOpenedFolderPath());
		loadProjectFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		loadProjectFileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription () {
				return "Dialogue Editor XML file";
			}

			@Override
			public boolean accept (File f) {
				if (f != null) {
					if (f.isDirectory()) {
						return true;
					}

					return f.getName().equals("project.xml");
				}

				return false;
			}
		});

		rendererSplitPane = new JSplitPane();
		rendererSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rendererSplitPane.setResizeWeight(1);
		rendererSplitPane.setContinuousLayout(true);
		contentPane.add(rendererSplitPane, BorderLayout.CENTER);

		JSplitPane propertiesSplitPane = new JSplitPane();
		propertiesSplitPane.setResizeWeight(1);

		JPanel propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());

		table = new PropertyTable(new DefaultTableModel());
		table.setRowHeight(20);
		logic.table = table;

		propertyPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		propertyPanel.add(table, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		GridBagLayout gbl_buttonsPanel = new GridBagLayout();
		gbl_buttonsPanel.columnWidths = new int[]{85, 0};
		gbl_buttonsPanel.rowHeights = new int[]{0, 23, 0, 0};
		gbl_buttonsPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_buttonsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		buttonsPanel.setLayout(gbl_buttonsPanel);
		JButton btnCharacters = new JButton("Characters");
		btnCharacters.addActionListener(logic.charactersBtnListner);

		JButton btnSequences = new JButton("Sequences");

		btnSequences.addActionListener(logic.sequencesBtnListner);
		GridBagConstraints gbc_btnSequences = new GridBagConstraints();
		gbc_btnSequences.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSequences.anchor = GridBagConstraints.NORTH;
		gbc_btnSequences.insets = new Insets(0, 0, 5, 0);
		gbc_btnSequences.gridx = 0;
		gbc_btnSequences.gridy = 0;
		buttonsPanel.add(btnSequences, gbc_btnSequences);

		GridBagConstraints gbc_btnCharacters = new GridBagConstraints();
		gbc_btnCharacters.fill = GridBagConstraints.BOTH;
		gbc_btnCharacters.insets = new Insets(0, 0, 5, 0);
		gbc_btnCharacters.gridx = 0;
		gbc_btnCharacters.gridy = 1;
		buttonsPanel.add(btnCharacters, gbc_btnCharacters);

		propertiesSplitPane.setRightComponent(buttonsPanel);

		JButton btnCallbacks = new JButton("Callbacks");
		btnCallbacks.addActionListener(logic.callbacksBtnListner);
		GridBagConstraints gbc_btnCallbacks = new GridBagConstraints();
		gbc_btnCallbacks.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCallbacks.anchor = GridBagConstraints.NORTH;
		gbc_btnCallbacks.gridx = 0;
		gbc_btnCallbacks.gridy = 2;
		gbc_btnCallbacks.weightx = 1;
		buttonsPanel.add(btnCallbacks, gbc_btnCallbacks);
		propertiesSplitPane.setLeftComponent(propertyPanel);
		rendererSplitPane.setRightComponent(propertiesSplitPane);
		rendererSplitPane.setLeftComponent(logic.canvas.getCanvas());

		// create the status bar panel and shove it down the bottom of the frame
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		// statusPanel.setBorder();
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(getWidth(), 19));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		StatusBar statusLabel = new StatusBar("Ready");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		logic.statusLabel = statusLabel;
	}

	private void createToolbar () {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		contentPane.add(toolBar, BorderLayout.NORTH);

		JButton btnSave = new JButton("Save");
		JButton btnLoad = new JButton("Load");
		JButton btnUndo = new JButton("Undo");
		JButton btnRedo = new JButton("Redo");
		JButton btnRun = new JButton("Run");

		btnSave.addActionListener(logic.saveButtonListener);
		btnLoad.addActionListener(logic.toolbarLoadListener);
		btnUndo.addActionListener(logic.toolbarUndoListener);
		btnRedo.addActionListener(logic.toolbarRedoListener);
		btnRun.addActionListener(null); // TODO setup listener

		toolBar.add(btnSave);
		toolBar.add(btnLoad);
		toolBar.add(btnUndo);
		toolBar.add(btnRedo);
		toolBar.add(btnRun);
	}

	private void createMenuBar () {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		fileMenu.getPopupMenu().setLightWeightPopupEnabled(false); // without this menu will render under canvas
		menuBar.add(fileMenu);

		JMenuItem menuNewProject = new JMenuItem("New Project");
		JMenuItem menuLoadProject = new JMenuItem("Load Project");
		JMenuItem menuSaveProject = new JMenuItem("Save Project");
		JMenuItem menuExportProject = new JMenuItem("Export Project");
		JMenuItem menuExit = new JMenuItem("Exit");

		menuNewProject.addActionListener(logic.menubarNewProjectListener);
		menuLoadProject.addActionListener(logic.menubarLoadProjectListener);
		menuSaveProject.addActionListener(logic.saveButtonListener);
		menuExportProject.addActionListener(logic.menubarExportProjectListener);

		fileMenu.add(menuNewProject);
		fileMenu.add(menuLoadProject);
		fileMenu.add(menuSaveProject);
		fileMenu.add(new JSeparator());
		fileMenu.add(menuExportProject);
		fileMenu.add(new JSeparator());
		fileMenu.add(menuExit);

		JMenu rendererMenu = new JMenu("Renderer");
		rendererMenu.getPopupMenu().setLightWeightPopupEnabled(false); // without this menu will render under canvas
		menuBar.add(rendererMenu);

		JCheckBoxMenuItem chckRenderDebug = new JCheckBoxMenuItem("Render Debug Info");
		JCheckBoxMenuItem chckRenderCurves = new JCheckBoxMenuItem("Render Curves");
		JMenuItem chckResetCamera = new JMenuItem("Reset Camera");

		chckRenderCurves.setSelected(true);

		chckRenderDebug.addActionListener(logic.menubarRenderDebugListener);
		chckRenderCurves.addActionListener(logic.menubarRenderCurvesListener);
		chckResetCamera.addActionListener(logic.menubarResetCameraListener);

		rendererMenu.add(chckRenderDebug);
		rendererMenu.add(chckRenderCurves);
		rendererMenu.add(chckResetCamera);
	}

	public void createPopupMenu () {
		PopupMenu popupMenu = new PopupMenu();

		MenuItem mAddText = new AddComponentMenuItem("Add 'Text'", DComponentType.TEXT);
		MenuItem mAddChoice = new AddComponentMenuItem("Add 'Choice'", DComponentType.CHOICE);
		MenuItem mAddRandom = new AddComponentMenuItem("Add 'Random'", DComponentType.RANDOM);
		MenuItem mAddCallback = new AddComponentMenuItem("Add 'Callback'", DComponentType.CALLBACK);
		MenuItem mAddCallbackCheck = new AddComponentMenuItem("Add 'Callback Check'", DComponentType.CBCHECK);
		MenuItem mAddRelay = new AddComponentMenuItem("Add 'Relay'", DComponentType.RELAY);
		MenuItem mAddEnd = new AddComponentMenuItem("Add 'End'", DComponentType.END);

		mAddText.addActionListener(logic.popupMenuListener);
		mAddChoice.addActionListener(logic.popupMenuListener);
		mAddRandom.addActionListener(logic.popupMenuListener);
		mAddCallback.addActionListener(logic.popupMenuListener);
		mAddCallbackCheck.addActionListener(logic.popupMenuListener);
		mAddRelay.addActionListener(logic.popupMenuListener);
		mAddEnd.addActionListener(logic.popupMenuListener);

		popupMenu.add(mAddText);
		popupMenu.add(mAddChoice);
		popupMenu.add(mAddRandom);
		popupMenu.add(mAddCallback);
		popupMenu.add(mAddCallbackCheck);
		popupMenu.add(mAddRelay);
		popupMenu.add(mAddEnd);

		logic.popupMenu = popupMenu;
	}

	public void loadProject (File projectConfigFile) {
		logic.loadProject(projectConfigFile);
	}

	public void newProject (Project project) {
		logic.newProject(project);
	}

	public void showLoadProjectDialog () {
		String lastPath = App.getLastOpenedFolderPath();
		if (lastPath != null)
			loadProjectFileChooser.setCurrentDirectory(new File(lastPath));
		int returnVal = loadProjectFileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) loadProject(loadProjectFileChooser.getSelectedFile());
	}

	@Override
	public void dispose () {
		logic.renderer.dispose(); // we have to manulay dispose renderer from this thread, or we will get "No OpenGL context found in the current thread."
		super.dispose();
	}
}
