package org.junder.ui.model;

public class AppModel {
	public final static AppModel instance = new AppModel(); 
	public final TasksModel tasksModel = new TasksModel();
	public final Document document = new Document();
}
