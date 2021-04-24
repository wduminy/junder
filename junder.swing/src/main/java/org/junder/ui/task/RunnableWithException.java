package org.junder.ui.task;

@FunctionalInterface
public interface RunnableWithException {
	void run() throws Exception;
}
