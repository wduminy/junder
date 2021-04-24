package org.junder.ui.model;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.junder.util.Messenger;

final public class TasksModel extends Observable implements Messenger {
	private ArrayList<String> taskNames = new ArrayList<>();
	private String errorText = null;
	private String message = "";
	
	public void startTask(String name) {
		errorText = null; 
		message = "";
		taskNames.add(name);
		notifyChanged();
	}
	
	/**
	 * Return the name of the task last started and not yet done.
	 * If no task has been started, return nothing
	 * @return
	 */
	public Optional<String> getExecutingTaskName() {
		if (taskNames.isEmpty())
			return Optional.empty();
		else
			return Optional.of(taskNames.get(taskNames.size()-1));
	}

	public void endTask(String name) {
		taskNames.remove(taskNames.indexOf(name));
		message = "";
		notifyChanged();
	}

	public boolean hasError() {
		return errorText != null; 
	}

	public String getErrorText() {
		if (errorText == null)
			throw new IllegalAccessError("There is no error");
		return errorText;
	}

	public void endTaskWithError(String name, String error) {
		errorText = error;
		error(error);
		endTask(name);
	}

	public void setMessageAsync(String msg) {
		EventQueue.invokeLater(()->{
			message = msg;
			notifyChanged();
		});
	}
	
	public String getMessage() {
		return message;
	}

	private void notifyChanged() {
		setChanged();
		notifyObservers(ModelEvent.TasksChanged);
	}
	
	@Override
	public void warn(String msg) {
		System.out.println("WARNING:" + msg);
		setMessageAsync("WARNING:" + msg);
	}

	@Override
	public void info(String msg) {
		setMessageAsync(msg);
	}

	@Override
	public void error(String msg) {
		System.err.println("ERROR:" + msg);
		JOptionPane.showMessageDialog(null, msg, "A processing error occurred" ,JOptionPane.ERROR_MESSAGE);
	}

	public void exception(Exception e) {
		e.printStackTrace(System.err);
		error(e.getMessage());
	}

}
