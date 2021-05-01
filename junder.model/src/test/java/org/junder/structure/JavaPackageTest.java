package org.junder.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;

public class JavaPackageTest {

	private static class TestPackage implements JavaPackage {

		private final String name;

		private TestPackage(String n) {
			name = n;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isParsed() {
			return false;
		}

		@Override
		public boolean isInDependencyGraph() {
			return false;
		}

		@Override
		public int countUsagesTo(JavaPackage javaPackage) {
			return 0;
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
		public Collection<JavaPackage> usingPackages() {
			return null;
		}

	}

	private String shortNameOf(String s) {
		return new TestPackage(s).shortName();
	}

	@Test
	public void test() {
		assertEquals("c.m.hello", shortNameOf("com.mycompany.hello"));
		assertEquals("c.hello", shortNameOf("com.hello"));
		assertEquals("hello", shortNameOf("hello"));
		assertEquals("c.me.hello", shortNameOf("com.me.hello"));
		assertEquals("c.m.butter.hello", shortNameOf("com.mycompany.butter.hello"));
	}

}
