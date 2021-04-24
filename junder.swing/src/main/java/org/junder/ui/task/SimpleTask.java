package org.junder.ui.task;

public class SimpleTask extends JunderTask {

	private RunnableWithException worker;

	public SimpleTask(String name, RunnableWithException work) {
		super(name);
		worker = work;
		execute();
	}

	@Override
	protected Void doInBackground() throws Exception {
		worker.run();
		return null;
	}

}
