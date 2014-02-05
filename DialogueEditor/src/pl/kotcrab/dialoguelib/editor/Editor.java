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

package pl.kotcrab.dialoguelib.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import pl.kotcrab.dialoguelib.editor.components.DComponentType;
import pl.kotcrab.dialoguelib.editor.gui.AddComponentMenuItem;
import pl.kotcrab.dialoguelib.editor.project.NewSequenceDialog;
import pl.kotcrab.dialoguelib.editor.project.Project;

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

public class Editor extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public static Editor window;
	
	private EditorLogic logic;
	
	private JPanel contentPane;
	
	private JSplitPane rendererSplitPane;
	private PropertyTable table;
	
	/**
	 * Create the frame.
	 */
	public Editor()
	{
		super("Dialogue Editor");
		
		logic = new EditorLogic(this);
		
		addWindowListener(logic.winAdapater);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		createMenuBar();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// toolbar
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				logic.project.save(logic.xstream);
				logic.renderer.setDirty(false);
			}
		});
		toolBar.add(btnSave);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO load
				
			}
		});
		toolBar.add(btnLoad);
		
		JButton btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				logic.renderer.undo();
			}
		});
		toolBar.add(btnUndo);
		
		JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				logic.renderer.redo();
			}
		});
		toolBar.add(btnRedo);
		// toolbar end
		
		rendererSplitPane = new JSplitPane();
		rendererSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rendererSplitPane.setResizeWeight(0.8);
		rendererSplitPane.setContinuousLayout(true);
		contentPane.add(rendererSplitPane, BorderLayout.CENTER);
		
		// popup menu
		PopupMenu popupMenu = createPopupMenu();
		logic.popupMenu = popupMenu;
		
		logic.createRenderer();
		
		logic.canvas = new LwjglCanvas(logic.renderer, true);
		logic.canvas.getCanvas().add(popupMenu);
		
		rendererSplitPane.setLeftComponent(logic.canvas.getCanvas());
		
		// property table
		JSplitPane propertiesSplitPane = new JSplitPane();
		propertiesSplitPane.setResizeWeight(0.7);
		rendererSplitPane.setRightComponent(propertiesSplitPane);
		
		JPanel propertyPanel = new JPanel();
		propertiesSplitPane.setLeftComponent(propertyPanel);
		propertyPanel.setLayout(new BorderLayout());
		
		table = new PropertyTable(new DefaultTableModel());
		logic.table = table;
		
		propertyPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		propertyPanel.add(table, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		propertiesSplitPane.setRightComponent(panel);
		
		JButton btnSequences = new JButton("Sequences");
		btnSequences.addActionListener(logic.sequencesBtnListner);
		panel.add(btnSequences);
		// propertiesSplitPane.setLeftComponent(table);
	}
	
	public void loadProject(File projectConfigFile)
	{
		logic.loadProject(projectConfigFile);
	}
	
	public void newProject(Project project)
	{
		logic.newProject(project);
	}
	
	@Override
	public void dispose()
	{
		logic.renderer.dispose(); // we have to manulay dispose renderer from this thread, or we will get "No OpenGL context found in the current thread."
		super.dispose();
	}
	
	private void createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.getPopupMenu().setLightWeightPopupEnabled(false); // without this menu will render under canvas
		menuBar.add(fileMenu);
		
		JMenuItem menuNewProject = new JMenuItem("New Project");
		menuNewProject.addActionListener(logic.menubarNewProjectListener);
		fileMenu.add(menuNewProject);
		
		JMenuItem menuLoadProject = new JMenuItem("Load Project");
		fileMenu.add(menuLoadProject);
		
		JMenuItem menuSaveProject = new JMenuItem("Save Project");
		fileMenu.add(menuSaveProject);
		
		JSeparator separator = new JSeparator();
		fileMenu.add(separator);
		
		JMenuItem menuExit = new JMenuItem("Exit");
		fileMenu.add(menuExit);
		
		JMenu rendererMenu = new JMenu("Renderer");
		rendererMenu.getPopupMenu().setLightWeightPopupEnabled(false); // without this menu will render under canvas
		menuBar.add(rendererMenu);
		
		JCheckBoxMenuItem chckRenderDebug = new JCheckBoxMenuItem("Render Debug Info");
		chckRenderDebug.addActionListener(logic.toolbarRenderDebugListener);
		rendererMenu.add(chckRenderDebug);
		
		JCheckBoxMenuItem chckRenderCurves = new JCheckBoxMenuItem("Render Curves");
		chckRenderCurves.addActionListener(logic.toolbarRenderCurvesListener);
		chckRenderCurves.setSelected(true);
		rendererMenu.add(chckRenderCurves);
		
		JMenuItem chckResetCamera = new JMenuItem("Reset Camera");
		chckResetCamera.addActionListener(logic.toolbarResetCameraListener);
		rendererMenu.add(chckResetCamera);
	}
	
	public PopupMenu createPopupMenu()
	{
		PopupMenu popupMenu = new PopupMenu();
		
		MenuItem mAddText = new AddComponentMenuItem("Add 'Text'", DComponentType.TEXT);
		MenuItem mAddChoice = new AddComponentMenuItem("Add 'Choice'", DComponentType.CHOICE);
		MenuItem mAddRandom = new AddComponentMenuItem("Add 'Random'", DComponentType.RANDOM);
		MenuItem mAddCallback = new AddComponentMenuItem("Add 'Callback'", DComponentType.CALLBACK);
		MenuItem mAddRelay = new AddComponentMenuItem("Add 'Relay'", DComponentType.RELAY);
		MenuItem mAddEnd = new AddComponentMenuItem("Add 'End'", DComponentType.END);
		
		mAddText.addActionListener(logic.popupMenuListener);
		mAddChoice.addActionListener(logic.popupMenuListener);
		mAddRandom.addActionListener(logic.popupMenuListener);
		mAddCallback.addActionListener(logic.popupMenuListener);
		mAddRelay.addActionListener(logic.popupMenuListener);
		mAddEnd.addActionListener(logic.popupMenuListener);
		
		popupMenu.add(mAddText);
		popupMenu.add(mAddChoice);
		popupMenu.add(mAddRandom);
		popupMenu.add(mAddCallback);
		popupMenu.add(mAddRelay);
		popupMenu.add(mAddEnd);
		
		return popupMenu;
	}
}
