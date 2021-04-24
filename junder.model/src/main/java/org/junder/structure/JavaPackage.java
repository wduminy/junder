package org.junder.structure;

import java.util.Collection;

public interface JavaPackage {
	String getName();
	/**
	 * A package is considered as parsed when it contains one or more classes 
	 * that were parsed.
	 * @return
	 */
	boolean isParsed();
	boolean isInDependencyGraph();
	int countUsagesTo(JavaPackage javaPackage);
	Collection<JavaClass> classesUsedOf(JavaPackage p);
	Collection<JavaClass> classedUsedBy(JavaPackage p);
	
   Collection<? extends JavaPackage> usingPackages();
	
	default String shortName() {
		StringBuilder sb = new StringBuilder();
		String[] parts = getName().split("\\.");
		int truncateCount =  parts.length > 3 
				? (parts.length - 2) : (parts.length - 1);
		for (int i = 0; i < parts.length; i++) {
			if (i < truncateCount && parts[i].length() > 2)
				sb.append(parts[i].charAt(0));
		    else
		    	sb.append(parts[i]);
			if (i != parts.length -1)
				sb.append('.');
		}
		return sb.toString();
	}
	
}
