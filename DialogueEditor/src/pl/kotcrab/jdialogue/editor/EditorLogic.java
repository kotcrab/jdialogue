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

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.thoughtworks.xstream.XStream;
import pl.kotcrab.jdialogue.editor.components.ComponentTableModel;
import pl.kotcrab.jdialogue.editor.components.Connector;
import pl.kotcrab.jdialogue.editor.components.DComponentConverter;
import pl.kotcrab.jdialogue.editor.components.types.CallbackCheckComponent;
import pl.kotcrab.jdialogue.editor.components.types.CallbackComponent;
import pl.kotcrab.jdialogue.editor.components.types.ChoiceComponent;
import pl.kotcrab.jdialogue.editor.components.types.EndComponent;
import pl.kotcrab.jdialogue.editor.components.types.RandomComponent;
import pl.kotcrab.jdialogue.editor.components.types.RelayComponent;
import pl.kotcrab.jdialogue.editor.components.types.StartComponent;
import pl.kotcrab.jdialogue.editor.components.types.TextComponent;
import pl.kotcrab.jdialogue.editor.gui.AddComponentMenuItem;
import pl.kotcrab.jdialogue.editor.gui.StatusBar;
import pl.kotcrab.jdialogue.editor.gui.dialog.CallbacksConfigDialog;
import pl.kotcrab.jdialogue.editor.gui.dialog.CharactersConfigDialog;
import pl.kotcrab.jdialogue.editor.gui.dialog.NewProjectDialog;
import pl.kotcrab.jdialogue.editor.gui.dialog.SequenceConfigDialog;
import pl.kotcrab.jdialogue.editor.gui.dialog.SequenceCreationDialog;
import pl.kotcrab.jdialogue.editor.project.PCallback;
import pl.kotcrab.jdialogue.editor.project.PCharacter;
import pl.kotcrab.jdialogue.editor.project.Project;
import pl.kotcrab.jdialogue.editor.project.ProjectCallback;
import pl.kotcrab.jdialogue.editor.project.ProjectExport;
import pl.kotcrab.jdialogue.editor.project.Sequence;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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

public class EditorLogic {
	// ===============================================FROM EDITOR CLASS===========================================
	public Editor window;
	public PopupMenu popupMenu;
	public PropertyTable table;
	public StatusBar statusLabel;

	// ==============================================SPECYFIC TO EDITOR LOGIC====================================
	public WindowAdapter winAdapater;

	public XStream xstream;

	public LwjglCanvas canvas; // canvas is more gui thing but it is here because of some methods that requires it

	public Renderer renderer;
	public Project project;

	public ActionListener popupMenuListener;

	public ActionListener sequencesBtnListner;
	public ActionListener charactersBtnListner;
	public ActionListener callbacksBtnListner;

	public ActionListener menubarResetCameraListener;
	public ActionListener menubarRenderCurvesListener;
	public ActionListener menubarRenderDebugListener;

	public ActionListener menubarNewProjectListener;
	public ActionListener menubarExportProjectListener;

	public ActionListener saveButtonListener;

	public ActionListener toolbarLoadListener;
	public ActionListener toolbarUndoListener;
	public ActionListener toolbarRedoListener;

	public ProjectCallback projectCallback;

	public EditorLogic (final Editor window) {
		this.window = window;

		xstream = new XStream();
		xstream.autodetectAnnotations(true);

		xstream.alias("dText", TextComponent.class);
		xstream.alias("dChoice", ChoiceComponent.class);
		xstream.alias("dStart", StartComponent.class);
		xstream.alias("dEnd", EndComponent.class);
		xstream.alias("dRelay", RelayComponent.class);
		xstream.alias("dCallback", CallbackComponent.class);
		xstream.alias("dCallbackCheck", CallbackCheckComponent.class);
		xstream.alias("dRandom", RandomComponent.class);

		xstream.alias("connector", Connector.class);
		xstream.alias("project", Project.class);
		xstream.alias("projectExport", ProjectExport.class);

		xstream.alias("character", PCharacter.class);
		xstream.alias("callback", PCallback.class);

		xstream.registerConverter(new DComponentConverter());

		winAdapater = new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent e) {
				if (renderer.isDirty()) showSaveProjectDialog();
			}
		};

		popupMenuListener = new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				AddComponentMenuItem obj = (AddComponentMenuItem) e.getSource();
				renderer.addComponent(obj.getType());
			}
		};

		sequencesBtnListner = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run () {
						if (project != null)
							new SequenceConfigDialog(Editor.window, project, xstream);
						else
							JOptionPane.showMessageDialog(Editor.window, "Create or load project to edit sequences", "Error", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
		};

		charactersBtnListner = new ActionListener() {

			@Override
			public void actionPerformed (ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run () {
						if (project != null)
							new CharactersConfigDialog(Editor.window, project);
						else
							JOptionPane.showMessageDialog(Editor.window, "Create or load project to edit characters", "Error", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
		};

		callbacksBtnListner = new ActionListener() {

			@Override
			public void actionPerformed (ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run () {
						if (project != null)
							new CallbacksConfigDialog(Editor.window, project);
						else
							JOptionPane.showMessageDialog(Editor.window, "Create or load project to edit callbacks", "Error", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
		};

		projectCallback = new ProjectCallback() {
			@Override
			public void sequenceChanged (Sequence newSequence) {
				project.loadActiveSequence(xstream);
				window.setTitle("Dialogue Editor - " + newSequence.getName());
				renderer.setComponentList(newSequence.getComponentList());
				table.setModel(new DefaultTableModel());
			}
		};

		prepareToolbar();
		prepareMenuBarRenderer();
		prepareMenuBarFile();
	}

	private void prepareToolbar () {
		saveButtonListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (project != null) {
					project.save(xstream);
					renderer.setDirty(false);
				} else
					JOptionPane.showMessageDialog(Editor.window, "Create or load project before saving", "Error", JOptionPane.ERROR_MESSAGE);

			}
		};

		toolbarLoadListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				window.showLoadProjectDialog();
			}
		};

		toolbarUndoListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				renderer.undo();
			}
		};

		toolbarRedoListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				renderer.redo();
			}
		};
	}

	private void prepareMenuBarRenderer () {
		menubarRenderDebugListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				JCheckBoxMenuItem comp = (JCheckBoxMenuItem) e.getSource();
				renderer.setRenderDebug(comp.isSelected());
			}
		};

		menubarRenderCurvesListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				JCheckBoxMenuItem comp = (JCheckBoxMenuItem) e.getSource();
				renderer.setRenderCurves(comp.isSelected());
			}
		};

		menubarResetCameraListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				renderer.resetCamera();
			}
		};
	}

	private void prepareMenuBarFile () {
		menubarNewProjectListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run () {
						new NewProjectDialog(Editor.window);
					}
				});
			}
		};

		menubarExportProjectListener = new ActionListener() {

			@Override
			public void actionPerformed (ActionEvent e) {
				if (project != null) {
					project.exportProject(xstream, statusLabel);
				} else
					JOptionPane.showMessageDialog(Editor.window, "Create or load project before exporting", "Error", JOptionPane.ERROR_MESSAGE);
			}
		};
	}

	public void createRenderer () {
		// renderer
		renderer = new Renderer(new EditorListener() {
			@Override
			public void mouseRightClicked (int x, int y) {
				popupMenu.show(canvas.getCanvas(), x, y);
			}

			@Override
			public void showMsg (final String msg, final String title, final int msgType) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run () {
						JOptionPane.showMessageDialog(Editor.window, msg, title, msgType);
					}
				});
			}

			@Override
			public void changePropertyTableModel (ComponentTableModel tableModel) {
				if (tableModel == null) // if componet doesn't have table model set default
					table.setModel(new DefaultTableModel());
				else {
					table.setModel(tableModel);
					ColumnsAutoSizer.sizeColumnsToFit(table);
				}
			}

			@Override
			public void showSaveDialog () {
				showSaveProjectDialog();
			}
		}, xstream);

		canvas = new LwjglCanvas(renderer, true);
		canvas.getCanvas().add(popupMenu);
	}

	public void loadProject (File projectConfigFile) {
		Project project = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(projectConfigFile));
			project = (Project) xstream.fromXML(reader);
			project.setListener(projectCallback);
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (project != null) {
			this.project = project;
			project.loadProject(projectConfigFile, xstream);
			renderer.setProject(project);
			table.setProject(project);
			renderer.setComponentList(project.getActiveSequence().getComponentList());
			window.setTitle("Dialogue Editor - " + project.getActiveSequence().getName());
		} else {
			JOptionPane.showMessageDialog(window, "Failed to load project located under: " + projectConfigFile, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public void newProject (Project project) {
		this.project = project;
		renderer.setProject(project);
		table.setProject(project);

		new SequenceCreationDialog(Editor.window, project, false, null, null);

		project.save(xstream);
		project.setListener(projectCallback);

		renderer.setComponentList(project.getActiveSequence().getComponentList());
	}

	public void showSaveProjectDialog () {
		int n = JOptionPane.showConfirmDialog(window, "Project or sequence has been modified. Save changes?", "Save", JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.CLOSED_OPTION || n == JOptionPane.NO_OPTION) return;
		if (n == JOptionPane.YES_OPTION) {
			renderer.setDirty(false);
			project.save(xstream);
		}
	}
}
