package org.junder.structure;

import java.util.HashSet;

import org.junder.process.JavaClassName;

final class JavaClassImpl implements JavaClass {

	private final JavaPackageImpl container;
	private final HashSet<JavaClassImpl> usingClasses = new HashSet<>();
	private final HashSet<JavaClassImpl> usedByClasses = new HashSet<>();
	private final JavaClassName name;

	JavaClassImpl(JavaPackageImpl p, JavaClassName className) {
		container = p;
		name = className;
	}

	JavaPackageImpl getPackage() {
		return container;
	}

	public void addUsage(JavaClassImpl supplier) {
		if (usingClasses.add(supplier)) {
			container.addUsage(supplier.getPackage());
			supplier.usedByClasses.add(this);
		}
	}
	
	@Override
	public String toString() {
		return name.getClassName();
	}

	boolean uses(JavaPackage p) {
		return usingClasses.stream().anyMatch(
				(c)->{return c.container == p;});
	}

	public boolean usedBy(JavaPackage p) {
		return usedByClasses.stream().anyMatch(
				(c)->{return c.container == p;});
	}

}
