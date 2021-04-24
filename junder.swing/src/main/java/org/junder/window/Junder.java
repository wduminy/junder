package org.junder.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.junder.structure.ProgramStructure;
import org.junder.structure.ProgramStructureFactory;
import org.junder.ui.model.AppModel;
import org.junder.ui.task.SimpleTask;
import org.junder.ui.view.DependencyView;
import org.junder.ui.view.MainMenuBar;
import org.junder.ui.view.StatusBar;

public class Junder {

	private JFrame frame;
	private DependencyView matrixContainer;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Invalid argument." );
			System.err.println("Usage: " + Junder.class.getName() + " <inputFileName>" );
			return;
		}
		// switch on anti-aliasing
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");		
		String osName = System.getProperty("os.name").toLowerCase();
		boolean isMac = osName.startsWith("mac os x");
		// try to enable the system look and feel
		if (isMac)
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e1) {
			// OK to ignore errors
		};
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Junder window = new Junder();
					MainMenuBar mainMenu = new MainMenuBar(AppModel.instance.document);
					ComponentAdapter listener = new ComponentAdapter() {
						@Override
						public void componentShown(ComponentEvent e) {
							new SimpleTask("Processing input ...", ()-> {
								ProgramStructure structure = ProgramStructureFactory.create(args[0], AppModel.instance.tasksModel);
								AppModel.instance.tasksModel.info("Minimising cycles");
								AppModel.instance.document.setStructure(structure);
							});
						}
					};
					window.frame.addComponentListener(listener);
					window.frame.setVisible(true);
					window.frame.setTitle("Junder - " + new File(args[0]).getAbsolutePath());
					window.frame.setJMenuBar(mainMenu);
				} catch (Exception e) {
					AppModel.instance.tasksModel.exception(e);
				}
			}
		});
	}

	public Junder() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 590, 449);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		matrixContainer = new DependencyView();
		frame.getContentPane().add(matrixContainer, BorderLayout.CENTER);
		StatusBar statusBar = new StatusBar(
				AppModel.instance.document,
				AppModel.instance.tasksModel);
		matrixContainer.add(statusBar, BorderLayout.SOUTH);
	}

	public DependencyView getMatrixPanel() {
		return matrixContainer;
	}
}
