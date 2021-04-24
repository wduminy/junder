package org.junder.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

final class JavaPackageImpl implements JavaPackage {

	private String name;
	private boolean parsed = false;
	private HashMap<JavaPackage, Integer> usageCounts = new HashMap<>();
	private HashMap<JavaPackage, Integer> usedCounts = new HashMap<>();
	private LinkedList<JavaClassImpl> classes = new LinkedList<>();

	JavaPackageImpl(String packageName) {
		name = packageName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isParsed() {
		return parsed;
	}
	
	void setParsed() {
		parsed = true;
	}

	@Override
	public int countUsagesTo(JavaPackage otherPackage) {
		return usageCounts.getOrDefault(otherPackage, 0);
	}

	/**
	 * Adds to the usage if the provider is a different package instance
	 * @param provider
	 */
	void addUsage(JavaPackageImpl provider) {
		if (provider == this)
			return;
		else {
			Integer old = usageCounts.getOrDefault(provider, 0);
			usageCounts.put(provider,old+1);
			provider.addUsedBy(this);
		}
	}
	
	
	/**
	 * Adds to the used if the provider is a different package instance
	 * @param provider
	 */
	private void addUsedBy(JavaPackage provider) {
		if (provider == this)
			return;
		else {
			Integer old = usedCounts.getOrDefault(provider, 0);
			usedCounts.put(provider,old+1);
		}
	}

	@Override
	public boolean isInDependencyGraph() {
		return !usageCounts.isEmpty() || !usedCounts.isEmpty();
	}

	@Override
	public Collection<JavaClass> classesUsedOf(JavaPackage p) {
		return classes.stream().filter((c)->{return c.uses(p);})
				.collect(ArrayList::new,ArrayList::add, ArrayList::addAll);
	}

	@Override
	public Collection<JavaClass> classedUsedBy(JavaPackage p) {
		return classes.stream().filter((c)->{return c.usedBy(p);})
				.collect(ArrayList::new,ArrayList::add, ArrayList::addAll);
	}

	void addClass(JavaClassImpl r) {
		classes.add(r);
	}

	public String toString() {
		return getName();
	}

	@Override
	public Collection<? extends JavaPackage> usingPackages() {
		return usageCounts.keySet();
	}


}
