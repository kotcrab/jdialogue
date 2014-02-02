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
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import pl.kotcrab.dialoguelib.editor.components.ComponentTableModel;
import pl.kotcrab.dialoguelib.editor.components.Connector;
import pl.kotcrab.dialoguelib.editor.components.DComponentConverter;
import pl.kotcrab.dialoguelib.editor.components.DComponentType;
import pl.kotcrab.dialoguelib.editor.components.types.CallbackComponent;
import pl.kotcrab.dialoguelib.editor.components.types.ChoiceComponent;
import pl.kotcrab.dialoguelib.editor.components.types.EndComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RandomComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RelayComponent;
import pl.kotcrab.dialoguelib.editor.components.types.StartComponent;
import pl.kotcrab.dialoguelib.editor.project.NewProjectDialog;
import pl.kotcrab.dialoguelib.editor.project.NewSequenceDialog;
import pl.kotcrab.dialoguelib.editor.project.Project;
import pl.kotcrab.dialoguelib.editor.project.SequenceSelectionDialog;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.thoughtworks.xstream.XStream;

public class Editor extends JFrame
{
	private static final long serialVersionUID = 7300428735708717490L;
	private static Editor window;
	
	private JPanel contentPane;
	private LwjglCanvas canvas;
	private Renderer renderer;
	private JSplitPane rendererSplitPane;
	private PropertyTable table;
	
	private XStream xstream;
	
	private Project project;
	
	private Preferences prefs;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		App.parseArguments(args);
		
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				window = new Editor();
				window.setVisible(true);
				
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] gd = ge.getScreenDevices();
				if(App.getScreenId() > -1 && App.getScreenId() < gd.length)
				{
					window.setLocation(gd[App.getScreenId()].getDefaultConfiguration().getBounds().x, window.getY());
				}
				else if(gd.length > 0)
				{
					window.setLocation(gd[0].getDefaultConfiguration().getBounds().x, window.getY());
				}
				else
				{
					throw new RuntimeException("No Screens Found");
				}
				
				window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				
				if(App.getProjectFile() != null) window.loadProject(App.getProjectFile());
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public Editor()
	{
		super("Dialogue Editor");
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if(renderer.isDirty())
					showSaveProjectDialog();
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("connector", Connector.class);
		
		xstream.alias("dText", TextComponent.class);
		xstream.alias("dChoice", ChoiceComponent.class);
		xstream.alias("dStart", StartComponent.class);
		xstream.alias("dEnd", EndComponent.class);
		xstream.alias("dRelay", RelayComponent.class);
		xstream.alias("dCallback", CallbackComponent.class);
		xstream.alias("dRandom", RandomComponent.class);
		xstream.alias("project", Project.class);
		
		xstream.registerConverter(new DComponentConverter());
		
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
				project.save(xstream);
				renderer.setDirty(false);
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
				renderer.undo();
			}
		});
		toolBar.add(btnUndo);
		
		JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				renderer.redo();
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
		final PopupMenu popupMenu = createPopupMenu();
		
		// renderer
		renderer = new Renderer(new EditorListener()
		{
			@Override
			public void mouseRightClicked(int x, int y)
			{
				popupMenu.show(canvas.getCanvas(), x, y);
			}
			
			@Override
			public void showMsg(final String msg, final String title, final int msgType)
			{
				EventQueue.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						JOptionPane.showMessageDialog(Editor.window, msg, title, msgType);
					}
				});
			}
			
			@Override
			public void changePropertyTableModel(ComponentTableModel tableModel)
			{
				if(tableModel == null) // if componet doesn't have table model set default
					table.setModel(new DefaultTableModel());
				else
				{
					table.setModel(tableModel);
					ColumnsAutoSizer.sizeColumnsToFit(table);
				}
			}
			
			@Override
			public void showSaveDialog()
			{
				showSaveProjectDialog();
			}
		}, xstream);
		
		canvas = new LwjglCanvas(renderer, true);
		canvas.getCanvas().add(popupMenu);
		
		rendererSplitPane.setLeftComponent(canvas.getCanvas());
		prefs = App.getPrefs();
		// renderer end
		
		// property table
		JSplitPane propertiesSplitPane = new JSplitPane();
		propertiesSplitPane.setResizeWeight(0.7);
		rendererSplitPane.setRightComponent(propertiesSplitPane);
		
		JPanel propertyPanel = new JPanel();
		propertiesSplitPane.setLeftComponent(propertyPanel);
		propertyPanel.setLayout(new BorderLayout());
		
		table = new PropertyTable(new DefaultTableModel());
		
		propertyPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		propertyPanel.add(table, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		propertiesSplitPane.setRightComponent(panel);
		
		JButton btnSequences = new JButton("Sequences");
		btnSequences.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						if(project != null)
							new SequenceSelectionDialog(Editor.window, project);
						else
							JOptionPane.showMessageDialog(Editor.window, "Create or load project to edit sequences", "Error", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
		});
		panel.add(btnSequences);
		// propertiesSplitPane.setLeftComponent(table);
	}
	
	private void showSaveProjectDialog()
	{
		// default icon, custom title
		int n = JOptionPane.showConfirmDialog(this, "Project or sequence has been modified. Save changes?", "Save", JOptionPane.YES_NO_OPTION);
		
		if(n == JOptionPane.CLOSED_OPTION || n == JOptionPane.NO_OPTION) return;
		if(n == JOptionPane.YES_OPTION) project.save(xstream);
	}
	
	public void newProject(final Project project)
	{
		this.project = project;
		renderer.setProject(project);
		
		// EventQueue.invokeLater(new Runnable()
		// {
		// @Override
		// public void run()
		// {
		new NewSequenceDialog(Editor.window, project, false);
		// }
		// });
		
		project.newProject();
		project.save(xstream);
		
		renderer.setComponentList(project.getActiveSequence().getComponentList());
	}
	
	public void loadProject(File projectConfigFile)
	{
		Project project = null;
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(projectConfigFile));
			project = (Project) xstream.fromXML(reader);
			reader.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		if(project != null)
		{
			this.project = project;
			project.loadProject(projectConfigFile, xstream);
			renderer.setComponentList(project.getActiveSequence().getComponentList());
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Failed to load project located under: " + projectConfigFile, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	@Override
	public void dispose()
	{
		renderer.dispose(); // we have to manulay dispose renderer from this thread, or we will get "No OpenGL context found in the current thread."
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
		menuNewProject.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						new NewProjectDialog(Editor.window);
					}
				});
			}
		});
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
		
		final JCheckBoxMenuItem chckRenderDebug = new JCheckBoxMenuItem("Render Debug Info");
		chckRenderDebug.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				renderer.setRenderDebug(chckRenderDebug.isSelected());
			}
		});
		rendererMenu.add(chckRenderDebug);
		
		final JCheckBoxMenuItem chckRenderCurves = new JCheckBoxMenuItem("Render Curves");
		chckRenderCurves.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				renderer.setRenderCurves(chckRenderCurves.isSelected());
			}
		});
		chckRenderCurves.setSelected(true);
		rendererMenu.add(chckRenderCurves);
		
		JMenuItem chckResetCamera = new JMenuItem("Reset Camera");
		chckResetCamera.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				renderer.resetCamera();
			}
		});
		rendererMenu.add(chckResetCamera);
	}
	
	public PopupMenu createPopupMenu()
	{
		PopupMenu popupMenu = new PopupMenu();
		
		MenuItem mAddText = new MenuItem("Add 'Text'");
		MenuItem mAddChoice = new MenuItem("Add 'Choice'");
		MenuItem mAddRandom = new MenuItem("Add 'Random'");
		MenuItem mAddCallback = new MenuItem("Add 'Callback'");
		MenuItem mAddRelay = new MenuItem("Add 'Relay'");
		MenuItem mAddEnd = new MenuItem("Add 'End'");
		
		mAddText.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				renderer.addComponent(DComponentType.TEXT);
			}
		});
		
		mAddChoice.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				renderer.addComponent(DComponentType.CHOICE);
			}
		});
		
		mAddRandom.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				renderer.addComponent(DComponentType.RANDOM);
			}
		});
		
		mAddCallback.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				renderer.addComponent(DComponentType.CALLBACK);
			}
		});
		
		mAddRelay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				renderer.addComponent(DComponentType.RELAY);
			}
		});
		
		mAddEnd.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				renderer.addComponent(DComponentType.END);
			}
		});
		
		popupMenu.add(mAddText);
		popupMenu.add(mAddChoice);
		popupMenu.add(mAddRandom);
		popupMenu.add(mAddCallback);
		popupMenu.add(mAddRelay);
		popupMenu.add(mAddEnd);
		
		return popupMenu;
	}
}
