package org.junder.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

public class AdjacencyStructure {
	public AdjacencyStructure(Collection<? extends JavaPackage> list) {
		list.forEach((p) -> {
			if (p.isParsed() && p.isInDependencyGraph())
				packages.add(new AdjacencyPackage(p));
		});
		calculateDepths();
		sort(SortMethod.DeepDependenciesFirst);
		optimise();
	}
	
	
	private void optimise() {
		int cycles = -1; 
		for (int i = 0; i < size(); i++) 
			for (int j = i+1; j < size(); j++) {
				int ij = getPackage(i).countUsagesTo(getPackage(j));
				if (ij > 0) {
					int ji = getPackage(j).countUsagesTo(getPackage(i));
					if (ji < ij) {
						if (cycles == -1)
							cycles = cycleUsingCount();
						swap(i, j);
						int newCycles = cycleUsingCount();
						if (newCycles > cycles)
							swap(i,j); //swap back
						else
							cycles = newCycles; 
					}
				}
			}
		
	}

	public void swap(int i, int j) {
		AdjacencyPackage oldi = packages.set(i, packages.get(j));
		packages.set(j, oldi);
	}
	
	protected final ArrayList<AdjacencyPackage> packages = new ArrayList<>();
	private class AdjacencyPackage {

		public final JavaPackage inner;
		private int depth = -1;

		public AdjacencyPackage(JavaPackage p) {
			inner = p;
		}

		public void calculateDepth(int i,LinkedList<AdjacencyPackage> path)  {
			if (depth < i) {
				path.addLast(this);
				for (AdjacencyPackage p : dependancies()) {
					if (!path.contains(p)) {
						p.calculateDepth(i+1,path);
					}
				}
				path.removeLast();
				depth = i;			
			}
		}

		public int numberOfDependencies() {
			int r = 0;
			for (AdjacencyPackage p : packages)
				if (p != this)
					r += numberOfUsagesTo(p) > 0 ? 1 : 0;
			return r;
		}

		public ArrayList<AdjacencyPackage> dependancies() {
			ArrayList<AdjacencyPackage> r = new ArrayList<>();
			for (AdjacencyPackage p : packages)
				if (p != this && numberOfUsagesTo(p) > 0)
					r.add(p);
			return r;
		}

		private int numberOfUsagesTo(AdjacencyPackage p) {
			return inner.countUsagesTo(p.inner);
		}

		public int dependencyDepth() {
			return depth;
		}
	}
	protected void calculateDepths() {
		for (AdjacencyPackage p : packages) 
			p.calculateDepth(0, new LinkedList<>());
	}
	protected void sort(SortMethod v) {
		packages.sort(v.comparator);
	}
	public JavaPackage getPackage(int i) {
		return packages.get(i).inner;
	}
	public int size() {
		return packages.size();
	}
	
	public int cycleUsingCount() {
		int r = 0;
		for (int i = 0; i < size(); i++) 
			for (int j = i+1; j < size(); j++)
				r += getPackage(i).countUsagesTo(getPackage(j));
		return r;
	}
	
	public static enum SortMethod {
		DeepDependenciesFirst(
				(a,b) -> {
					return b.dependencyDepth() - a.dependencyDepth();
				}),
		NumberOfDependancies(
				(a,b) -> {
					return b.numberOfDependencies() - a.numberOfDependencies();
				}
		);
		public final Comparator<AdjacencyPackage> comparator;
		SortMethod(Comparator<AdjacencyPackage> c) {
			comparator = c;
		}
		

	}

	/**
	 * Collects all packages that has a path to the root.  
	 * @param included
	 */
	public Collection<? extends JavaPackage> linkedPackages(JavaPackage root) {
		HashSet<JavaPackage> result = new HashSet<>();
		LinkedList<JavaPackage> fringe = new LinkedList<JavaPackage>();
		fringe.add(root);
		while (!fringe.isEmpty()) {
			JavaPackage p = fringe.remove();
			if (!result.contains(p)) {
				result.add(p);
				for (JavaPackage e : p.usingPackages()) 
					if (!result.contains(e))
						fringe.add(e);
			}
		}
		return result;
	}
}