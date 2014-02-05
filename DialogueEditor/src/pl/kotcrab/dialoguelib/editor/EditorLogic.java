package pl.kotcrab.dialoguelib.editor;

import java.awt.EventQueue;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import pl.kotcrab.dialoguelib.editor.components.ComponentTableModel;
import pl.kotcrab.dialoguelib.editor.components.Connector;
import pl.kotcrab.dialoguelib.editor.components.DComponentConverter;
import pl.kotcrab.dialoguelib.editor.components.types.CallbackComponent;
import pl.kotcrab.dialoguelib.editor.components.types.ChoiceComponent;
import pl.kotcrab.dialoguelib.editor.components.types.EndComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RandomComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RelayComponent;
import pl.kotcrab.dialoguelib.editor.components.types.StartComponent;
import pl.kotcrab.dialoguelib.editor.components.types.TextComponent;
import pl.kotcrab.dialoguelib.editor.gui.AddComponentMenuItem;
import pl.kotcrab.dialoguelib.editor.project.NewProjectDialog;
import pl.kotcrab.dialoguelib.editor.project.NewSequenceDialog;
import pl.kotcrab.dialoguelib.editor.project.Project;
import pl.kotcrab.dialoguelib.editor.project.SequenceSelectionDialog;

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.thoughtworks.xstream.XStream;

public class EditorLogic
{
	// ===============================================FROM EDITOR CLASS===========================================
	public Editor window;
	public PopupMenu popupMenu;
	public PropertyTable table;
	
	// ==============================================SPECYFIC TO EDITOR LOGIC====================================
	public WindowAdapter winAdapater;
	
	public XStream xstream;
	
	public LwjglCanvas canvas; // canvas is more gui thing but it is here because of some methods that requires it
	
	public Renderer renderer;
	public Project project;
	
	public ActionListener popupMenuListener;
	
	public ActionListener sequencesBtnListner;
	
	public ActionListener menubarResetCameraListener;
	public ActionListener menubarRenderCurvesListener;
	public ActionListener menubarRenderDebugListener;
	
	public ActionListener menubarNewProjectListener;
	
	public ActionListener toolbarSaveListener;
	public ActionListener toolbarLoadListener;
	public ActionListener toolbarUndoListener;
	public ActionListener toolbarRedoListener;
	
	public EditorLogic(Editor window)
	{
		this.window = window;
		
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
		
		winAdapater = new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if(renderer.isDirty()) showSaveProjectDialog();
			}
		};
		
		popupMenuListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AddComponentMenuItem obj = (AddComponentMenuItem) e.getSource();
				renderer.addComponent(obj.getType());
			}
		};
		
		sequencesBtnListner = new ActionListener()
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
		};
		
		prepareToolbar();
		prepareMenuBarRenderer();
		prepareMenuBarFile();
	}
	
	private void prepareToolbar()
	{
		toolbarSaveListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				project.save(xstream);
				renderer.setDirty(false);
			}
		};
		
		toolbarLoadListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO load
				
			}
		};
		
		toolbarUndoListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				renderer.undo();
			}
		};
		
		toolbarRedoListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				renderer.redo();
			}
		};
	}
	
	private void prepareMenuBarRenderer()
	{
		menubarRenderDebugListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JCheckBoxMenuItem comp = (JCheckBoxMenuItem) e.getSource();
				renderer.setRenderDebug(comp.isSelected());
			}
		};
		
		menubarRenderCurvesListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JCheckBoxMenuItem comp = (JCheckBoxMenuItem) e.getSource();
				renderer.setRenderCurves(comp.isSelected());
			}
		};
		
		menubarResetCameraListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				renderer.resetCamera();
			}
		};
	}
	
	private void prepareMenuBarFile()
	{
		menubarNewProjectListener = new ActionListener()
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
		};
	}
	
	public void createRenderer()
	{
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
			JOptionPane.showMessageDialog(window, "Failed to load project located under: " + projectConfigFile, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	public void newProject(Project project)
	{
		this.project = project;
		renderer.setProject(project);
		
		new NewSequenceDialog(Editor.window, project, false);
		
		project.newProject();
		project.save(xstream);
		
		renderer.setComponentList(project.getActiveSequence().getComponentList());
	}
	
	public void showSaveProjectDialog()
	{
		int n = JOptionPane.showConfirmDialog(window, "Project or sequence has been modified. Save changes?", "Save", JOptionPane.YES_NO_OPTION);
		
		if(n == JOptionPane.CLOSED_OPTION || n == JOptionPane.NO_OPTION) return;
		if(n == JOptionPane.YES_OPTION) project.save(xstream);
	}
}