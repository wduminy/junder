package org.junder.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junder.ui.model.Document;
import org.junder.ui.model.ModelEvent;
import org.junder.ui.model.TasksModel;

@SuppressWarnings("deprecation")
public class StatusBar extends JPanel {

	private static final long serialVersionUID = 3469560721990241863L;
	private Observer modelObserver = new Observer() {

		@Override
		public void update(Observable o, Object arg) {
			if (arg == ModelEvent.TasksChanged) {
				if (tasks.hasError()) {
					statusLabel.setText(tasks.getErrorText());
					statusLabel.setForeground(Color.RED);
				} else {
					statusLabel.setText(tasks.getExecutingTaskName().orElse("Ready"));
					statusLabel.setForeground(Color.BLACK);
				}
				messageLabel.setText(tasks.getMessage());
			} else if (arg == ModelEvent.MatrixLoaded) {
				docStatus.setText(doc.statusText());
			}
		}
	};
	private JLabel statusLabel, messageLabel;
	private TasksModel tasks;
	private JLabel docStatus;
	private Document doc;

	public StatusBar(Document d, TasksModel t) {
		setLayout(new BorderLayout(5, 0));
		statusLabel = new JLabel("Waiting");
		messageLabel = new JLabel();
		docStatus = new JLabel(d.statusText());
		add(statusLabel, BorderLayout.WEST);
		add(messageLabel, BorderLayout.EAST);
		add(docStatus, BorderLayout.CENTER);
		tasks = t;
		doc = d;
		doc.addObserver(modelObserver);
		tasks.addObserver(modelObserver);
	}
}
