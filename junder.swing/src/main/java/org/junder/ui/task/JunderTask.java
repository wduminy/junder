package org.junder.ui.task;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.junder.ui.model.AppModel;

public abstract class JunderTask extends SwingWorker<Void,Void> {
	private final String name;

	protected JunderTask(String taskName) {
		AppModel.instance.tasksModel.startTask(name = taskName);
	}
	
	@Override
	protected void done() {
		try {
			get();
			AppModel.instance.tasksModel.endTask(name);
		} catch (ExecutionException ex) {
			ex.printStackTrace();
			AppModel.instance.tasksModel.endTaskWithError(name,ex.getCause().getMessage());			
		} catch (Exception ex) {
			ex.printStackTrace();
			AppModel.instance.tasksModel.endTaskWithError(name,ex.getMessage());			
		}
		super.done();
	}

}
