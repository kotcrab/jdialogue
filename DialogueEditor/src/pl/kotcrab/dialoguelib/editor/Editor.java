/*******************************************************************************
 * Copyright 2013 Pawel Pastuszak
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

package pl.kotcrab.dialoguelib.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MenuItem;
import java.awt.PopupMenu;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import pl.kotcrab.dialoguelib.editor.components.DComponentType;

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Editor extends JFrame
{
	private static final long serialVersionUID = 7300428735708717490L;
	private JPanel contentPane;
	private JTable table;
	private LwjglCanvas canvas;
	private Renderer renderer;
	private JSplitPane splitPane;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Editor frame = new Editor();
					frame.setVisible(true);
					
					GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
					GraphicsDevice[] gd = ge.getScreenDevices();
					if(App.getScreenId() > -1 && App.getScreenId() < gd.length)
					{
						frame.setLocation(gd[App.getScreenId()].getDefaultConfiguration().getBounds().x, frame.getY());
					}
					else if(gd.length > 0)
					{
						frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
					}
					else
					{
						throw new RuntimeException("No Screens Found");
					}
					frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public Editor()
	{
		super("Dialogue Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton saveBtn = new JButton("Save");
		toolBar.add(saveBtn);
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.8);
		splitPane.setContinuousLayout(true);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		table = new JTable();
		splitPane.setRightComponent(table);
		
		
		MenuItem mAddText = new MenuItem("Add 'Text'");
		MenuItem mAddChoice = new MenuItem("Add 'Choice'");
		MenuItem mAddRandom = new MenuItem("Add 'Random'");
		
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
		
		final PopupMenu popupMenu = new PopupMenu();
		popupMenu.add(mAddText);
		popupMenu.add(mAddChoice);
		popupMenu.add(mAddRandom);

		
		
		renderer = new Renderer(new EditorListener()
		{
			@Override
			public void mouseRightClicked(int x, int y)
			{
				popupMenu.show(canvas.getCanvas(), x, y);
			}
		});
		
		canvas = new LwjglCanvas(renderer, true);
		canvas.getCanvas().add(popupMenu);
		splitPane.setLeftComponent(canvas.getCanvas());
		
	}
}
