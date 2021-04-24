package org.junder.structure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;

public class AdjacencyStructureTest {

	@Test
	public void optimise() {
		AdjacencyStructure s = new AdjacencyStructure(
				new PackageList("A -> B, B -> C, C->A,C->A"));
		assertEquals(1, s.cycleUsingCount());
	}
	
	@Test
	public void subnetSimple() {
		PackageList plist = new PackageList("A -> B, B -> C, B -> D");
		AdjacencyStructure s = new AdjacencyStructure(plist);
		Collection<? extends JavaPackage> linked = s.linkedPackages(plist.obtain("B"));
		AdjacencyStructure sub = new AdjacencyStructure(linked);
		assertEquals(3, sub.size());
	}
	
	@Test
	public void subnetWithLoop() {
		PackageList plist = new PackageList("A -> B, B -> C, B -> D, D -> B");
		AdjacencyStructure s = new AdjacencyStructure(plist);
		Collection<? extends JavaPackage> linked = s.linkedPackages(plist.obtain("B"));
		AdjacencyStructure sub = new AdjacencyStructure(linked);
		assertEquals(3, sub.size());
	}

	
	private static class PackageList extends ArrayList<TestPackage> {

		private static final long serialVersionUID = -4706128258615378852L;

		public PackageList(String s) {
			String[] usages = s.split("\\,");
			for (String u : usages) {
				String[] p = u.split("->");
				String from = p[0].trim();
				String to = p[1].trim();
				TestPackage fromPackage = obtain(from);
				TestPackage toPackage = obtain(to);
				fromPackage.addUsage(toPackage);
			}
		}

		public TestPackage obtain(String name) {
			TestPackage n = null;
			TestPackage r = stream().
					filter((e) -> e.getName().equals(name)).findFirst()
					.orElse(n = new TestPackage(name));
			if (n != null)
				add(n);
			return r;
		}
		
	}
	
	private static class TestPackage implements JavaPackage {
		private String name;
		private HashMap<TestPackage, Integer> usings = new HashMap<>();

		TestPackage(String n) {
			name = n;
		}
		
		public void addUsage(TestPackage toPackage) {
			usings.put(toPackage, usings.getOrDefault(toPackage, 0)+1);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isParsed() {
			return true;
		}

		@Override
		public boolean isInDependencyGraph() {
			return true;
		}

		@Override
		public int countUsagesTo(JavaPackage p) {
			return usings.getOrDefault(p, 0);
		}

		@Override
		public Collection<JavaClass> classesUsedOf(JavaPackage p) {
			return null;
		}

		@Override
		public Collection<JavaClass> classedUsedBy(JavaPackage p) {
			return null;
		}

		@Override
		public Collection<? extends JavaPackage> usingPackages() {
			return usings.keySet();
		}

		
	}

}
